package spark.extract.features;

import com.google.common.base.*;
import com.google.common.base.Optional;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
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

import static spark.extract.features.constant.Constants.DISCOUNT_200_RATE;

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
        Map<String, String> options = new HashMap<String, String>();
        options.put("header", "true");
        options.put("path", Constants.LESS_OFFLINE_DATA_PATH);
        DataFrame df = sqlContext.load("com.databricks.spark.csv", options);

        df.registerTempTable("offline_counsume");
        JavaPairRDD<String, Row> rawDataRDD = df.toJavaRDD().mapToPair(new PairFunction<Row, String, Row>() {

            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(0), row);
            }
        }).persist(StorageLevel.MEMORY_AND_DISK());
        JavaPairRDD<String, String> consumerNoCouponConsumeRateRDD = getConsumerNoCouponConsumeRate(rawDataRDD, false);
        JavaPairRDD<String, String> consumerCouponMerchantCntRDD = getConsumerCouponMerchantCnt(rawDataRDD, false);
        JavaPairRDD<String, String> diffCouponUseRDD = getUserDiffCouponUse(jsc, rawDataRDD, false);



//        consumerNoCouponConsumeRateRDD.fullOuterJoin(consumerCouponMerchantCntRDD).fullOuterJoin(diffCouponUseRDD)
//                .mapToPair(new PairFunction<Tuple2<String,Tuple2<Optional<Tuple2<Optional<String>,Optional<String>>>,Optional<String>>>, String, String>() {
//
//
//                    @Override
//                    public Tuple2<String, String> call(Tuple2<String, Tuple2<Optional<Tuple2<Optional<String>, Optional<String>>>, Optional<String>>> t) throws Exception {
//                        return new Tuple2<String, String>(t._1(),t._2._1()+"|"+t._2()._2());
//                    }
//                }).foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println(tuple2._1()+"="+tuple2._2());
//
//            }
//        });

        jsc.stop();


    }


    /**
     * 获得用户在没有优惠券的情况下的消费率
     *
     * @param rawDataRDD 原始数据RDD
     * @param online     是否线上消费.
     */
    private static JavaPairRDD<String, String> getConsumerNoCouponConsumeRate(JavaPairRDD<String, Row> rawDataRDD, Boolean online) {


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
                    if (StringUtils.notEmpty(couponId) && StringUtils.isEmpty(datePay)) {
                        hasCouponNoUse++;
                    }
                    // 有消费券并且已经使用
                    if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId)) {
                        hasCouponUse++;
                        if (StringUtils.notEmpty(discountRate) && discountRate.indexOf(':') == -1) {
                            directDiscountCnt++;
                        }
                    }

                    if (StringUtils.notEmpty(dateRecevied) && StringUtils.notEmpty(datePay)) {
                        if (DateUtils.getDayTimeInterval(dateRecevied, datePay) <= 15) {
                            waitTime.add(1.f - Float.valueOf(DateUtils.getDayTimeInterval(dateRecevied, datePay) / 15.f));

                        } else {
                            waitTime.add(Float.valueOf(0.f));

                        }

                    }


                }

                normalCounsumeRate = String.format("%.3f", normalConsumeCnt / count);
                hasCouponNoUseRate = String.format("%.3f", hasCouponNoUse / count);
                hasCouponUseRate = String.format("%.3f", hasCouponUse / count);
                directDiscountRate = String.format("%.3f", directDiscountCnt / hasCouponUse);
                waitAverageTime = String.valueOf(Calculator.getAverage(waitTime));
                String userFeatures = Constants.USER_NORMAL_CONSUME_RATE + "=" + normalCounsumeRate + "|"
                        + Constants.USER_HAS_COUPON_NOUSE_RATE + "=" + hasCouponNoUseRate + "|"
                        + Constants.USER_HAS_COUPON_USE_RATE + "=" + hasCouponUseRate + "|"
                        + Constants.USER_DIRECT_DISCOUNT_RATE + "=" + directDiscountRate + "|"
                        + Constants.USER_COUPON_USE_TIPME_INTERVAL + "=" + waitAverageTime;
                return new Tuple2<String, String>(tuple._1(), userFeatures);
            }
        });


       return userNormalCosumeRateRDD;

    }

    /**
     * 这里计算了用户使用优惠券的数量以及 消费商家的数量
     *
     * @param rawDataRDD
     * @param online
     */
    private static JavaPairRDD<String, String> getConsumerCouponMerchantCnt(JavaPairRDD<String, Row> rawDataRDD, Boolean online) {
        final JavaPairRDD<String, Row> userCouponRDD = rawDataRDD.filter(new Function<Tuple2<String, Row>, Boolean>() {
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
        JavaPairRDD<String, String> userMerchantCntRDD = userCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, String>() {

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                String userId = tuple._1();
                String merchantId = tuple._2().getString(1);

                return new Tuple2<String, String>(userId, merchantId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> t) throws Exception {
                return new Tuple2<String, Long>(t._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> t) throws Exception {
                return new Tuple2<String, String>(t._1(), Constants.USER_COUPON_MERCHANT_COUNT + "=" + t._2());
            }
        });


        // 用户使用消费券的数量  JavaPairRDD<String,Long> userCouponCntRDD
        JavaPairRDD<String, String> userCouponCntRDD = userCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, String>() {


            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> t) throws Exception {
                return new Tuple2<String, String>(t._1(), t._2().getString(2));
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {


            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple2) throws Exception {
                return new Tuple2<String, Long>(tuple2._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> t) throws Exception {
                return new Tuple2<String, String>(t._1(), Constants.USER_COUPON_COUNT + "=" + t._2());
            }
        });

          JavaPairRDD<String,String> fullMerchantCouponCntRDD = userMerchantCntRDD.fullOuterJoin(userCouponCntRDD).mapToPair(new PairFunction<Tuple2<String,Tuple2<Optional<String>,
                             Optional<String>>>, String, String>() {
             @Override
             public Tuple2<String, String> call(Tuple2<String, Tuple2<Optional<String>, Optional<String>>> tuple) throws Exception {


                 return new Tuple2<String, String>(tuple._1(),tuple._2()._1()+"|"+tuple._2()._2());
             }



         });

//        fullMerchantCouponCntRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println(tuple2._1()+"->"+tuple2._2());
//            }
//        });
           return  fullMerchantCouponCntRDD;

    }

    //这里计算不同折扣的消费数量和消费率
    private static JavaPairRDD<String, String> getUserDiffCouponUse(JavaSparkContext jsc, final JavaPairRDD<String, Row> rawDataRDD, Boolean online) {

        Map<String, String> userIdConsumeCntMap = rawDataRDD.groupByKey().mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> t) throws Exception {

                long count = 0;
                String userId = t._1();

                Iterator<Row> it = t._2().iterator();

                List<Float> rateList = new ArrayList<Float>();
                while (it.hasNext()) {

                    Row row = it.next();
                    count++;
                    // 消费券使用日
                    String datePay = row.getString(6);
                    // 优惠券id
                    String couponId = row.getString(2);
                    // 折扣
                    String discountRate = row.getString(3);
                    if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId) &&
                            StringUtils.notEmpty(discountRate) && !"fixed".equals(discountRate)) {
                        if (discountRate.indexOf(":")!=-1){
                            float rate = Float.valueOf(discountRate.split(":")[1])/
                                    Float.valueOf(discountRate.split(":")[0]);
                            rateList.add(rate);
                        }else{
                            rateList.add(Float.valueOf(discountRate));
                        }

                    }


                }
                float averateDiscount = Calculator.getAverage(rateList);

                String cnt = Constants.USER_CONSUME_COUNT + "=" + count + "|" +
                        Constants.DISCOUNT_AVERATE+"="+averateDiscount;
                return new Tuple2<String, String>(userId, cnt);
            }
        }).collectAsMap();

        JavaPairRDD<String, String> userId2DiscountRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, Long>() {

            @Override
            public Tuple2<String, Long> call(Tuple2<String, Row> tuple) throws Exception {
                String userId = tuple._1();
                Row row = tuple._2();
                // 优惠券id
                String couponId = row.getString(2);
                // 折扣
                String discountRate = row.getString(3);
                // 消费券使用日
                String datePay = row.getString(6);
                String userId2DiscountFormat = null;
                if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId)) {
                    if (StringUtils.notEmpty(discountRate) && discountRate.indexOf(':') == -1) {
                        if (discountRate == "fixed") {
                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_FIXED_COUNT + "-" +
                                    Constants.DISCOUNT_FIXED_RATE;
                        } else {
                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_DIRECT_COUNT + "-" +
                                    Constants.DISCOUNT_DIRECT_RATE;
                        }


                    } else {

                        int fullDis = Integer.valueOf(discountRate.split(":")[0]);
                        if (fullDis < 50) {

                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_50_COUNT + "-" +
                                    Constants.DISCOUNT_50_RATE;

                        } else if (fullDis < 200) {
                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_200_COUNT + "-" +
                                    Constants.DISCOUNT_200_RATE;
                        } else if (fullDis < 500) {
                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_500_COUNT + "-" +
                                    Constants.DISCOUNT_500_RATE;
                        } else {
                            userId2DiscountFormat = userId + "-" + Constants.DISCOUNT_MORE_COUNT + "-" +
                                    Constants.DISCOUNT_MORE_RATE;
                        }

                    }

                    return new Tuple2<String, Long>(userId2DiscountFormat, 1L);
                }

                return new Tuple2<String, Long>(userId, 0L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> tuple2) throws Exception {
                if (StringUtils.hasDelimiter(tuple2._1(),"\\|")){
                    String userId = tuple2._1().split("-")[0];
                    String disCountFormat = tuple2._1().split("-")[1];

                    return new Tuple2<String, String>(userId,disCountFormat+"="+tuple2._2());
                }
                return new Tuple2<String, String>(tuple2._1(),"0");

            }


        });
        userId2DiscountRDD.groupByKey().foreach(new VoidFunction<Tuple2<String, Iterable<String>>>() {
            @Override
            public void call(Tuple2<String, Iterable<String>> tuple2) throws Exception {
                System.out.println(tuple2._1() + "==" + tuple2._2());
            }
        });
        final Broadcast<Map<String, String>> broadcast = jsc.broadcast(userIdConsumeCntMap);

//        JavaPairRDD<String, String> diffDisCountRDD = userId2DiscountRDD.mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
//
//
//            @Override
//            public Tuple2<String, String> call(Tuple2<String, Long> tuple) throws Exception {
//              // <userid -> userconsumeCount | userUseCouponConsumeCount | averageCount>
//                Map<String, String> broadcastValue = broadcast.getValue();
//                if (tuple._1().indexOf('-') != -1) {
//                    String[] splits = tuple._1().split("-");
//                    // 总消费数目
//                    String value = broadcastValue.get(splits[0]);
//                    String countV = value.split("\\|")[0];
//                    String cnt = countV.split("=")[1];
//
//                    // 对应折扣的消费数目
//                    long discountCnt = tuple._2();
//
//                    float rate = Float.valueOf(discountCnt) / Float.valueOf(cnt);
//
//                    String cntAndRateValue = splits[1] + "=" + discountCnt + "|" +
//                            splits[2] + "=" + rate + "|"+ value.split("\\|")[1];
//
//                    return new Tuple2<String, String>(splits[0], cntAndRateValue);
//
//                }
//                return new Tuple2<String, String>(tuple._1(), "0");
//            }
//        });

//        System.out.println("count-====="+diffDisCountRDD.count());
//
//        diffDisCountRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//
//                System.out.println(tuple2._1() + "==" + tuple2._2());
//
//            }
//        });

       return userId2DiscountRDD;

    }


}
