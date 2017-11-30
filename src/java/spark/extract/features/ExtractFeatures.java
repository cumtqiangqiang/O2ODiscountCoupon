package spark.extract.features;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import spark.extract.features.constant.Constants;
import utils.StringUtils;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        getConsumerNoCouponConsumeRate(df,false);
        jsc.stop();



    }

    /**
     * 获得用户在没有优惠券的情况下的消费率
     * @param df
     * @param online  是否线上消费.
     */
    private  static void getConsumerNoCouponConsumeRate(DataFrame df,Boolean online){


        JavaPairRDD<String, Row> dataRaw = df.toJavaRDD().mapToPair(new PairFunction<Row, String, Row>() {

            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(0),row);
            }
        }).persist(StorageLevel.MEMORY_AND_DISK());

        JavaPairRDD<String, String> userNormalCosumeRateRDD = dataRaw.groupByKey().mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {

            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                Iterator<Row> userConsumeIt = tuple._2().iterator();
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

                while (userConsumeIt.hasNext()) {
                    count++;
                    Row row = userConsumeIt.next();
                    // 消费券获得日
                    String dateRecevied = row.getString(5);
                    // 消费券使用日
                    String datePay = row.getString(6);
                    // 优惠券id
                    String couponId = row.getString(2);
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
                    }


                }
                normalCounsumeRate = String.format("%.3f",normalConsumeCnt / count);
                hasCouponNoUseRate = String.format("%.3f",hasCouponNoUse / count);
                hasCouponUseRate = String.format("%.3f", hasCouponUse / count);
                return  new Tuple2<String, String>(tuple._1(),hasCouponUseRate);
//                return new Tuple2<String, String>(tuple._1(),
//                        StringUtils.jointString(new String[]{normalCounsumeRate,
//                        hasCouponNoUseRate,hasCouponUseRate}));
            }
        });


        Map<String,String> userNormalCosumeRateMap = userNormalCosumeRateRDD.collectAsMap();

        for (Map.Entry<String,String> entry:userNormalCosumeRateMap.entrySet()) {
            System.out.println("userid = "+entry.getKey() + " rate = "+entry.getValue());
        }

    }



}
