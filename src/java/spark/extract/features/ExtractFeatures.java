package spark.extract.features;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import spark.extract.features.constant.Constants;
import utils.Calculator;
import utils.DateUtils;
import utils.PrintMap;
import utils.StringUtils;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * Created by UC227911 on 11/30/2017.
 */
public class ExtractFeatures {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                   .setAppName("O2OCoupon")
                  .setMaster("local");
        Logger.getLogger("org").setLevel(Level.ERROR);
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(jsc.sc());
        Map<String,String> options = new HashMap<String, String>();
        options.put("header","true");
        options.put("path", Constants.LESS_OFFLINE_DATA_PATH);
        DataFrame df = sqlContext.load("com.databricks.spark.csv",options);

        df.registerTempTable("offline_counsume");
        JavaPairRDD<String, Row> rawDataRDD = df.toJavaRDD().mapToPair(new PairFunction<Row, String, Row>() {

            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(0),row);
            }
        }).persist(StorageLevel.MEMORY_AND_DISK());
//        getConsumerNoCouponConsumeRate(rawDataRDD,false);
        getConsumerCouponMerchantCnt(rawDataRDD,false);
        jsc.stop();



    }



    /**
     * 获得用户在没有优惠券的情况下的消费率
     * @param rawDataRDD 原始数据RDD
     * @param online  是否线上消费.
     */
    private  static void getConsumerNoCouponConsumeRate(JavaPairRDD<String,Row> rawDataRDD,Boolean online){


        JavaPairRDD<String, String> userNormalCosumeRateRDD = rawDataRDD.groupByKey().mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {

            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                Iterator<Row> userConsumeIt = tuple._2().iterator();
                String userId = tuple._1();
                // 总的消费次数
                long count = 0;
                // 正常消费
                float normalConsumeCnt = 0.f;
                // 正常消费率
                String normalCounsumeRate = "";

                // 获得消费券 但是没有使用 即负样本
                float hasCouponNoUse = 0.f;
                String hasCouponNoUseRate = "";
                // 有消费券并且已经使用
                float hasCouponUse = 0.f;
                String hasCouponUseRate = "";
                // 使用优惠券  直接折扣，不是满减类消费
                float directDiscountCnt = 0.f;
                String directDiscountRate = "";

                // 获得优惠券使用时间间隔
                List<Float> waitTime = new ArrayList<Float>();
                String waitAverageTime = "";
                while (userConsumeIt.hasNext()) {
                    count++;
                    Row row = userConsumeIt.next();
                    // 消费券获得日
                    String dateRecevied = row.getString(5);
                    // 消费券使用日
                    String datePay = row.getString(6);
                    // 优惠券id
                    String couponId = row.getString(2);
                    // 折扣
                    String discountRate = row.getString(3);
                    // 正常消费
                    if (StringUtils.isEmpty(dateRecevied) && !StringUtils.isEmpty(datePay)) {
                        normalConsumeCnt++;
                    }
                    // 获得消费券 但是没有使用 即负样本
                    if (!StringUtils.isEmpty(couponId) && StringUtils.isEmpty(datePay)){
                        hasCouponNoUse++;
                    }
                    // 有消费券并且已经使用
                    if (!StringUtils.isEmpty(datePay) && !StringUtils.isEmpty(couponId)){
                        hasCouponUse++;
                        if (StringUtils.notEmpty(discountRate) && discountRate.indexOf(':') ==-1){
                            directDiscountCnt++;
                        }
                    }

                    if (StringUtils.notEmpty(dateRecevied) && StringUtils.notEmpty(datePay)){
                            if (DateUtils.getDayTimeInterval(dateRecevied,datePay) <= 15){
                                  waitTime.add(1.f - Float.valueOf(DateUtils.getDayTimeInterval(dateRecevied,datePay)/15.f));

                            }else {
                                waitTime.add(Float.valueOf(0.f));

                            }


                    }


                }

                normalCounsumeRate = String.format("%.3f",normalConsumeCnt / count);
                hasCouponNoUseRate = String.format("%.3f",hasCouponNoUse / count);
                hasCouponUseRate = String.format("%.3f", hasCouponUse / count);
                directDiscountRate = String.format("%.3f",directDiscountCnt / hasCouponUse);
                waitAverageTime = String.valueOf(Calculator.getAverage(waitTime));
                String userFeatures = Constants.USER_NORMAL_CONSUME_RATE +"=" + normalCounsumeRate + "|"
                                     + Constants.USER_HAS_COUPON_NOUSE_RATE + "=" + hasCouponNoUseRate + "|"
                                     + Constants.USER_HAS_COUPON_USE_RATE + "=" + hasCouponUseRate + "|"
                                     + Constants.USER_DIRECT_DISCOUNT_RATE + "=" + directDiscountRate +"|"
                                     + Constants.USER_COUPON_USE_TIPME_INTERVAL +"=" + waitAverageTime;
                return new Tuple2<String, String>(tuple._1(),userFeatures);
            }
        });





        Map<String,String> userNormalCosumeRateMap = userNormalCosumeRateRDD.collectAsMap();

        for (Map.Entry<String,String> entry:userNormalCosumeRateMap.entrySet()) {
            System.out.println("userid = "+entry.getKey() + " rate = "+entry.getValue());
        }

    }

    private  static void getConsumerCouponMerchantCnt(JavaPairRDD<String,Row> rawDataRDD,Boolean online){
        final JavaPairRDD<String, Row> userCouponRDD= rawDataRDD.filter(new Function<Tuple2<String, Row>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Row> v1) throws Exception {
                Row row = v1._2();
                // 消费券使用日
                String datePay = row.getString(6);
                // 优惠券id
                String couponId = row.getString(2);
                if (StringUtils.notEmpty(couponId) && StringUtils.notEmpty(datePay)) {
                    return true;
                }

                return false;
            }
        }).persist(StorageLevel.MEMORY_AND_DISK());

        // 用户使用优惠券消费的商家数量
        JavaPairRDD<String, String> userMerchantCntRDD = userCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, Long>() {

            @Override
            public Tuple2<String, Long> call(Tuple2<String, Row> tuple) throws Exception {
                String userId = tuple._1();
                String merchantId = tuple._2().getString(1);
                String userMechant = userId + "-" + merchantId;

                return new Tuple2<String, Long>(userMechant, 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1+v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> tuple) throws Exception {
                return new Tuple2<String, String>(tuple._1().split("-")[0],
                         Constants.USER_COUPON_MERCHANT_COUNT+"="+tuple._2());
            }
        });



        // 用户使用消费券的数量
       JavaPairRDD<String,Long> userCouponCntRDD = userCouponRDD.mapToPair(new PairFunction<Tuple2<String,Row>, String, Long>() {

            @Override
            public Tuple2<String, Long> call(Tuple2<String, Row> t) throws Exception {

                return new Tuple2<String, Long>(t._1()+"-"+t._2().getString(2),1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
           @Override
           public Long call(Long v1, Long v2) throws Exception {
               return v1+v2;
           }
       });

//               .mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
//           @Override
//           public Tuple2<String, String> call(Tuple2<String, Long> tuple) throws Exception {
//               return new Tuple2<String, String>(tuple._1().split("-")[0],
//                       Constants.USER_COUPON_COUNT+"="+String.valueOf(tuple._2()));
//           }
//       });

//        PrintMap.printMap(Constants.USER_COUPON_MERCHANT_COUNT,userMerchantCntRDD.collectAsMap());

        PrintMap.printMap(Constants.USER_COUPON_COUNT,userCouponCntRDD.collectAsMap());



    }



}
