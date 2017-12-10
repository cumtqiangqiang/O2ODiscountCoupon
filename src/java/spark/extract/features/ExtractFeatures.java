package spark.extract.features;

import com.google.common.base.Optional;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import spark.extract.features.constant.Constants;
import utils.Calculator;
import utils.CouponType;
import utils.DateUtils;
import utils.StringUtils;

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

        final Accumulator<String> accumulator = jsc.accumulator("",new MerchantAggrAccumulator());
        getMerchantConsume(rawDataRDD,accumulator,false);
//        JavaPairRDD<String, String> consumerNoCouponConsumeRateRDD = getConsumerNoCouponConsumeRate(rawDataRDD, false);
//        JavaPairRDD<String, String> consumerCouponMerchantCntRDD = getConsumerCouponMerchantCnt(rawDataRDD, false);
//        JavaPairRDD<String, String> diffCouponUseRDD = getUserDiffCouponUse(jsc, rawDataRDD, false);




        jsc.stop();


    }

    private  static void getUserConsumeFeatures(JavaPairRDD<String, Row> rawDataRDD, Boolean online){


        rawDataRDD.mapToPair(new PairFunction<Tuple2<String,Row>, String, String>() {


            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                // 消费券获得日
                String dateRecevied = row.getString(5);
                // 消费券使用日
                String datePay = row.getString(6);
                // 优惠券id
                String couponId = row.getString(2);
                // 折扣
                String discountRate = row.getString(3);



                return null;
            }
        });





    }


    /**
     * 获得用户在没有优惠券的情况下的消费率
     *
     * @param rawDataRDD 原始数据RDD
     * @param online     是否线上消费.
     */
    private static JavaPairRDD<String, String> getConsumerNoCouponConsumeRate(JavaPairRDD<String, Row> rawDataRDD, Boolean online) {


        JavaPairRDD<String, String> userNormalCosumeRateRDD = rawDataRDD.groupByKey().mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {

            private static final long serialVersionUID = 7095759952436551348L;

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
            private static final long serialVersionUID = 7267123752154076088L;

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

            private static final long serialVersionUID = -2838142263946526031L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                String userId = tuple._1();
                String merchantId = tuple._2().getString(1);

                return new Tuple2<String, String>(userId, merchantId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            private static final long serialVersionUID = -650744009038281544L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> t) throws Exception {
                return new Tuple2<String, Long>(t._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = -2559276693655973000L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            private static final long serialVersionUID = 7959581893957713246L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> t) throws Exception {
                return new Tuple2<String, String>(t._1(), Constants.USER_COUPON_MERCHANT_COUNT + "=" + t._2());
            }
        });


        // 用户使用消费券的数量  JavaPairRDD<String,Long> userCouponCntRDD
        JavaPairRDD<String, String> userCouponCntRDD = userCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, String>() {


            private static final long serialVersionUID = -5622750387705793200L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> t) throws Exception {
                return new Tuple2<String, String>(t._1(), t._2().getString(2));
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {


            private static final long serialVersionUID = -2392405283969984175L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple2) throws Exception {
                return new Tuple2<String, Long>(tuple2._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = 8982009813497966326L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {
            private static final long serialVersionUID = 4766990857360250840L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> t) throws Exception {
                return new Tuple2<String, String>(t._1(), Constants.USER_COUPON_COUNT + "=" + t._2());
            }
        });

        JavaPairRDD<String, String> fullMerchantCouponCntRDD = userMerchantCntRDD.fullOuterJoin(userCouponCntRDD)
                .mapToPair(new PairFunction<Tuple2<String, Tuple2<Optional<String>,
                Optional<String>>>, String, String>() {
                    private static final long serialVersionUID = 1281842562126417305L;

                    @Override
            public Tuple2<String, String> call(Tuple2<String, Tuple2<Optional<String>, Optional<String>>> tuple) throws Exception {


                return new Tuple2<String, String>(tuple._1(), tuple._2()._1() + "|" + tuple._2()._2());
            }


        });

//        fullMerchantCouponCntRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println(tuple2._1()+"->"+tuple2._2());
//            }
//        });
        return fullMerchantCouponCntRDD;

    }

    /**
     * 计算了不同折扣的使用次数以及使用率和平均折扣率
     * @param jsc
     * @param rawDataRDD
     * @param online
     * @return
     */
    private static JavaPairRDD<String, String> getUserDiffCouponUse(JavaSparkContext jsc, final JavaPairRDD<String, Row> rawDataRDD, Boolean online) {

        //每个用户的 平均折扣率
        rawDataRDD.combineByKey(new Function<Row, Tuple2<Float, Integer>>() {

            private static final long serialVersionUID = -7817596185734575057L;

            @Override
            public Tuple2<Float, Integer> call(Row row) throws Exception {

                // 消费券使用日
                String datePay = row.getString(6);
                // 优惠券id
                String couponId = row.getString(2);
                // 折扣
                String discountRate = row.getString(3);
                float rate = 0.f;
                if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId) &&
                        StringUtils.notEmpty(discountRate) && !"fixed".equals(discountRate)) {
                    if (discountRate.contains(":")) {
                        rate = Float.valueOf(discountRate.split(":")[1]) /
                                Float.valueOf(discountRate.split(":")[0]);

                    } else {
                        rate = Float.valueOf(discountRate);
                    }
                }


                return new Tuple2<Float, Integer>(rate, 1);
            }

        }, new Function2<Tuple2<Float, Integer>, Row, Tuple2<Float, Integer>>() {
            private static final long serialVersionUID = -2791986475933532657L;

            @Override
            public Tuple2<Float, Integer> call(Tuple2<Float, Integer> v1, Row row) throws Exception {

                // 消费券使用日
                String datePay = row.getString(6);
                // 优惠券id
                String couponId = row.getString(2);
                // 折扣
                String discountRate = row.getString(3);
                float rate = 0.f;
                if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId) &&
                        StringUtils.notEmpty(discountRate) && !"fixed".equals(discountRate)) {
                    if (discountRate.contains(":")) {
                        rate = Float.valueOf(discountRate.split(":")[1]) /
                                Float.valueOf(discountRate.split(":")[0]);

                    } else {
                        rate = Float.valueOf(discountRate);
                    }
                }


                return new Tuple2<Float, Integer>(v1._1() + rate, v1._2() + 1);
            }
        }, new Function2<Tuple2<Float, Integer>, Tuple2<Float, Integer>, Tuple2<Float, Integer>>() {
            private static final long serialVersionUID = -7613381077352106726L;

            @Override
            public Tuple2<Float, Integer> call(Tuple2<Float, Integer> v1, Tuple2<Float, Integer> v2) throws Exception {
                return new Tuple2<Float, Integer>(v1._1()+v2._1(),v1._2()+v2._2());
            }
        }).foreach(new VoidFunction<Tuple2<String, Tuple2<Float, Integer>>>() {
            private static final long serialVersionUID = 973345503965707444L;

            @Override
            public void call(Tuple2<String, Tuple2<Float, Integer>> tuple) throws Exception {
                System.out.println(tuple._1()+"="+tuple._2()._1()/tuple._2()._2());

            }
        });

        System.out.println("------------------------------------------------------------------");

        JavaPairRDD<String, Long> userIdDisFormat2CntRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, Long>() {


            @Override
            public Tuple2<String, Long> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                String userId = tuple._1();
                // 消费券使用日
                String datePay = row.getString(6);
                // 优惠券id
                String couponId = row.getString(2);
                // 折扣
                String discountRate = row.getString(3);
                String discountFormat = "";
                if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId) &&
                        StringUtils.notEmpty(discountRate) && !"fixed".equals(discountRate)) {
                    if (discountRate.contains(":")) {
                        int discountFull = Integer.valueOf(discountRate.split(":")[0]);
                        if (discountFull <= 50) {
                            discountFormat = Constants.DISCOUNT_50_COUNT;
                        } else if (discountFull <= 200) {
                            discountFormat = Constants.DISCOUNT_200_COUNT;
                        } else if (discountFull <= 500) {
                            discountFormat = Constants.DISCOUNT_500_COUNT;
                        } else {
                            discountFormat = Constants.DISCOUNT_MORE_COUNT;
                        }
                    } else {
                        discountFormat = Constants.DISCOUNT_DIRECT_COUNT;
                    }

                    return new Tuple2<String, Long>(userId + "-" + discountFormat, 1L);
                }


                return new Tuple2<String, Long>(userId,1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = -1981800031350592684L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });

        JavaPairRDD<String, String> userId2DisFormatCntRDD = userIdDisFormat2CntRDD.mapToPair(new PairFunction<Tuple2<String, Long>, String, String>() {


            @Override
            public Tuple2<String, String> call(Tuple2<String, Long> tuple) throws Exception {

                String[] splits = tuple._1().split("-");
                if (splits.length == 2){
                    String userId = splits[0];
                    String disFormat = splits[1];
                    return new Tuple2<String, String>(userId, disFormat + "=" + tuple._2());
                }

                return new Tuple2<String, String>(tuple._1(),String.valueOf(tuple._2()));

            }
        });

        userId2DisFormatCntRDD.groupByKey().mapToPair(new PairFunction<Tuple2<String,Iterable<String>>,
                                    String, String>() {


            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<String>> v) throws Exception {
                String userId = v._1();
                Iterator<String> discounts = v._2().iterator();
                StringBuilder fullDis = new StringBuilder();
                long cnt = 0L;
                while (discounts.hasNext()){
                     String dis = discounts.next();
                     if (!dis.contains("=")){
                         cnt += Long.valueOf(dis);
                     }else {
                         cnt += Long.valueOf((dis.split("=")[1]));

                         fullDis.append(dis + "|");
                     }


                 }

                return new Tuple2<String, String>(userId,
                        fullDis.toString()+"cnt="+cnt);
            }
        }).mapValues(new Function<String, String>() {

            @Override
            public String call(String v1) throws Exception {
                String cnt = StringUtils.getFieldFromConcatString(v1,"\\|","cnt");

                String dis50 = StringUtils.getFieldFromConcatString(v1,"\\|",Constants.DISCOUNT_50_COUNT);
                String dis200 = StringUtils.getFieldFromConcatString(v1,"\\|",Constants.DISCOUNT_200_COUNT);
                String dis500 = StringUtils.getFieldFromConcatString(v1,"\\|",Constants.DISCOUNT_500_COUNT);
                String dis500more = StringUtils.getFieldFromConcatString(v1,"\\|",Constants.DISCOUNT_MORE_COUNT);
                String disDirect = StringUtils.getFieldFromConcatString(v1,"\\|",Constants.DISCOUNT_DIRECT_COUNT);

                float dis50Rate = StringUtils.notEmpty(dis50) ? Float.valueOf(dis50) / Float.valueOf(cnt) : 0.f;
                float dis200Rate = StringUtils.notEmpty(dis200) ? Float.valueOf(dis200) / Float.valueOf(cnt) : 0.f;
                float dis500Rate = StringUtils.notEmpty(dis500) ? Float.valueOf(dis500) / Float.valueOf(cnt) : 0.f;
                float dis500MoreRate = StringUtils.notEmpty(dis500more) ? Float.valueOf(dis500more) / Float.valueOf(cnt) : 0.f;
                float disDirectRate = StringUtils.notEmpty(disDirect) ? Float.valueOf(disDirect) / Float.valueOf(cnt) : 0.f;

                return v1+"|"+Constants.DISCOUNT_50_RATE + "=" + dis50Rate + "|" +
                        Constants.DISCOUNT_200_RATE + "=" + dis200Rate + "|" +
                        Constants.DISCOUNT_500_RATE + "=" + dis500Rate + "|" +
                        Constants.DISCOUNT_MORE_RATE + "=" + dis500MoreRate + "|" +
                        Constants.DISCOUNT_DIRECT_RATE + "=" + disDirectRate;

            }
        }).sortByKey().foreach(new VoidFunction<Tuple2<String, String>>() {
            @Override
            public void call(Tuple2<String, String> tuple) throws Exception {
                System.out.println(tuple._1()+"="+tuple._2());
            }
        });



        return null;

    }

    /**
     * merchant
     * @param rawDataRDD
     * @param accumulator
     * @param online
     */
    private  static  void getMerchantConsume(final JavaPairRDD<String, Row> rawDataRDD,Accumulator<String> accumulator, Boolean online){

        /**
         * 商户消费的不同用户量
         */
        JavaPairRDD<String, Long> merchantId2uniqueUserRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, String>() {
            private static final long serialVersionUID = 6813455970266645101L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {

                Row row = tuple._2();
                // 优惠券id
                String userId = row.getString(0);
                //商户id
                String merchantId = row.getString(1);

                return new Tuple2<String, String>(merchantId, merchantId + "-" + userId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {


            private static final long serialVersionUID = 3612407545251726586L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                return new Tuple2<String, Long>(tuple._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = -5832822333208648758L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });

        rawDataRDD.filter(new Function<Tuple2<String, Row>, Boolean>() {
            private static final long serialVersionUID = -6164994096654132755L;

            @Override
            public Boolean call(Tuple2<String, Row> v1) throws Exception {
                Row row = v1._2();
                String couponId = row.getString(2);
                if (StringUtils.isEmpty(couponId)){
                    return false;
                }else {
                    return true;
                }

            }
        }).mapToPair(new PairFunction<Tuple2<String,Row>, String, String>() {
            private static final long serialVersionUID = -7353585028640054067L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple2) throws Exception {
                Row row = tuple2._2();
                String merchantId = row.getString(1);
                String couponId = row.getString(2);

                return new Tuple2<String, String>(merchantId,merchantId+"-"+couponId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String,String>, String, Long>() {


            private static final long serialVersionUID = -7961139007587765808L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple2) throws Exception {
                return new Tuple2<String, Long>(tuple2._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = 617014754989552625L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }).foreach(new VoidFunction<Tuple2<String, Long>>() {
            private static final long serialVersionUID = 2806043266590287736L;

            @Override
            public void call(Tuple2<String, Long> tuple2) throws Exception {
                System.out.println("merchant :" + tuple2._1() + "cnt:"+tuple2._2());
            }
        });

        System.out.println("-----------------------------------------------");
        /**
         *
         * 商户消费的不同折扣量计算
         */
        JavaPairRDD<String, String> merchantIdCntRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
                String, String>() {


            private static final long serialVersionUID = 6813455970266645101L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                //商户id
                String merchantId = row.getString(1);

                String cntValue = getValuefeatureMapHelper(row,true);

                return new Tuple2<String, String>(merchantId, cntValue);
            }
        });


        JavaPairRDD<String, String> merchantId2AggraRDD = merchantIdCntRDD.reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                return StringUtils.aggregate(v1, v2);
            }
        });

        merchantId2AggraRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
            @Override
            public void call(Tuple2<String, String> tuple2) throws Exception {
                System.out.println("merchantId :" +tuple2._1()+" value:"+tuple2._2());
            }
        });

        System.out.println("---------------------------------------------------------------");
        /**
         * 不同折扣率计算
         */
        merchantId2AggraRDD.mapValues(new Function<String, String>() {

            private static final long serialVersionUID = 5209174191613684516L;

            @Override
            public String call(String v1) throws Exception {
                // 未使用优惠券
                long hasCouponNoUsedCnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT));
                // 使用优惠券
                long couponUsedCnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT));
                // 总消费次数
                long cnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_COUNT));

                long dis50cnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_50_COUNT));
                long dis200cnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_200_COUNT));

                long dis500cnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_500_COUNT));
                long disMoreCnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_MORE_COUNT));
                long disDirectcnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_DIRECT_COUNT));

                long disFixedcnt = Long.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_FIXED_COUNT));

                int disLess15cnt = Integer.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|",Constants.MERCHANT_DISCOUNT_LESS15_COUNT));

                // 优惠券发放量
                long couponCnt = hasCouponNoUsedCnt + couponUsedCnt;
                String initialRate = Constants.MERCHANT_DISCOUNT_INITIAL_RATE;
                Map<String,Float> rateMap = new HashMap<String, Float>();
                if (couponCnt > 0 && couponUsedCnt > 0){
                    // 核销率
                    float couponChargeOffRate = Float.valueOf(couponUsedCnt)/Float.valueOf(couponCnt);
                    rateMap.put(Constants.MERCHANT_COUPON_CHARGEOFF_RATE,couponChargeOffRate);

                }
                if (dis50cnt > 0 ){

                    float dis50Rate = Float.valueOf(dis50cnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_50_RATE,dis50Rate);
                }
                if (dis200cnt > 0){
                    float dis200Rate = Float.valueOf(dis200cnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_200_RATE,dis200Rate);

                }
                if (dis500cnt > 0){
                    float dis500Rate = Float.valueOf(dis500cnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_500_RATE,dis500Rate);
                }
                if (disMoreCnt > 0){

                    float disMoreRate = Float.valueOf(disMoreCnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_MORE_RATE,disMoreRate);
                }

                if (disDirectcnt > 0){
                    float disDirctRate = Float.valueOf(disDirectcnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_DIRECT_RATE,disDirctRate);

                }
                if (disFixedcnt > 0){
                    float disFixRate=Float.valueOf(disDirectcnt)/Float.valueOf(cnt);
                    rateMap.put(Constants.MERCHANT_DISCOUNT_FIXED_RATE,disFixRate);
                }

                if (disLess15cnt > 0){
                    float disLess15Rate= 1 - Float.valueOf(disLess15cnt)/15;
                    rateMap.put(Constants.MERCHANT_DISCOUNT_LESS15_RATE,disLess15Rate);

                }


                for (Map.Entry<String,Float> entry : rateMap.entrySet()) {

                    initialRate = StringUtils.setFieldInConcatString(initialRate, "\\|",
                            entry.getKey(), String.valueOf(entry.getValue()));
                }



                return initialRate;
            }
        }).sortByKey().foreach(new VoidFunction<Tuple2<String, String>>() {
            @Override
            public void call(Tuple2<String, String> tuple2) throws Exception {
                System.out.println("merchantId:"+tuple2._1()+" users:"+tuple2._2());
            }
        });




//        JavaPairRDD<String, Iterable<Row>> merchantId2RowsRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
//                String, Row>() {
//
//
//            private static final long serialVersionUID = 6813455970266645101L;
//
//            @Override
//            public Tuple2<String, Row> call(Tuple2<String, Row> tuple) throws Exception {
//                Row row = tuple._2();
//                String merchantId = row.getString(1);
//                return new Tuple2<String, Row>(merchantId, row);
//            }
//        }).groupByKey().persist(StorageLevel.MEMORY_ONLY());
//
//        JavaPairRDD<String, String> merchantIdCouponContRDD = merchantId2RowsRDD.mapToPair(new PairFunction<Tuple2<String,
//                Iterable<Row>>, String, String>() {
//
//            private static final long serialVersionUID = 3612407545251726586L;
//
//            @Override
//            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
//                // 来店里消费的总数
//
//                Iterator<Row> rows = tuple._2().iterator();
//                // 全部消费
//                long cnt = 0L;
//                long normalCnt = 0L;
//                long couponUsedCnt = 0L;
//                long hasCouponNoUsedCnt = 0L;
//                long dis50Cnt = 0L;
//                long dis200Cnt = 0L;
//                long dis500Cnt = 0L;
//                long dis500MoreCnt = 0L;
//                long disFixedCnt = 0L;
//                long disDirectCnt = 0L;
//                Map<String,Long> cntMap = new HashMap<String, Long>();
//                String cntValueInit = Constants.MERCHANT_DISCOUNT_INITIAL_COUNT;
//                while (rows.hasNext()) {
//
//                    cnt++;
//                    Row row = rows.next();
//                    // 消费券获得日
//                    String dateRecevied = row.getString(5);
//                    // 消费券使用日
//                    String datePay = row.getString(6);
//                    // 优惠券id
//                    String couponId = row.getString(2);
//                    // 折扣
//                    String discountRate = row.getString(3);
//                    if (StringUtils.isEmpty(dateRecevied) && !StringUtils.isEmpty(datePay)) {
//                        normalCnt++;
//                        cntMap.put(Constants.MERCHANT_NORMAL_CONSUME_COUNT,normalCnt);
//                    }
//                    // 获得消费券 但是没有使用 即负样本
//                    if (StringUtils.notEmpty(couponId) && StringUtils.isEmpty(datePay)) {
//                        hasCouponNoUsedCnt++;
//                        cntMap.put(Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT,hasCouponNoUsedCnt);
//                    }
//                    // 有消费券并且已经使用
//                    if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId)) {
//                        couponUsedCnt++;
//                        cntMap.put(Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT,couponUsedCnt);
//                        int coupponType = CouponType.couponDiscountType(discountRate);
//                        switch (coupponType) {
//
//                            case Constants.COUPON_DIRECT:
//                                disDirectCnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_DIRECT_COUNT,disDirectCnt);
//                                break;
//                            case Constants.COUPON_FIXED:
//                                disFixedCnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_FIXED_COUNT,disFixedCnt);
//                                break;
//                            case Constants.COUPON_50:
//                                dis50Cnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_50_COUNT,dis50Cnt);
//                                break;
//                            case Constants.COUPON_200:
//                                dis200Cnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_200_COUNT,dis200Cnt);
//                                break;
//                            case Constants.COUPON_500:
//                                dis500Cnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_500_COUNT,dis500Cnt);
//                                break;
//                            case Constants.COUPON_500_MORE:
//                                dis500MoreCnt++;
//                                cntMap.put(Constants.MERCHANT_DISCOUNT_MORE_COUNT,dis500MoreCnt);
//
//                        }
//
//
//                    }
//
//
//                }
//
//                cntMap.put(Constants.MERCHANT_COUNT,cnt);
//                for (Map.Entry<String,Long> entry : cntMap.entrySet()) {
//
//                    cntValueInit = StringUtils.setFieldInConcatString(cntValueInit, "\\|",
//                            entry.getKey(), String.valueOf(entry.getValue()));
//                }
//
//
//
//                return new Tuple2<String, String>(tuple._1(), cntValueInit);
//
//            }
//        });
//
//
//        JavaPairRDD<String, String> merichantAggCntRDD = merchantIdCouponContRDD.reduceByKey(new Function2<String, String, String>() {
//            private static final long serialVersionUID = 1041297081176697626L;
//
//            @Override
//            public String call(String v1, String v2) throws Exception {
//                return StringUtils.aggregate(v1, v2);
//            }
//        }).persist(StorageLevel.MEMORY_ONLY());
//        merichantAggCntRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            private static final long serialVersionUID = -2665112235642262045L;
//
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println("merchantId :" +tuple2._1()+" value:"+tuple2._2());
//            }
//        });
//
//        System.out.println("---------------------------------------------------------------------");

    }

    private  static String getValuefeatureMapHelper(Row row,boolean isMerahcnt){

        CouponFeatures feature = null;
        if (isMerahcnt){
             feature = new MerchantFeatures();
        }else {
            feature = new UserFeatures();
        }
        // 优惠券id
        String couponId = row.getString(2);
        // 折扣
        String discountRate = row.getString(3);

        // 消费券获得日
        String dateRecevied = row.getString(5);
        // 消费券使用日
        String datePay = row.getString(6);

        String cntValue =feature.getInitialCountValue();
        cntValue = StringUtils.setFieldInConcatString(cntValue,
                "\\|", feature.getMerchangCnt(), "1");


        if (StringUtils.isEmpty(couponId) && !StringUtils.isEmpty(datePay)) {
            cntValue = StringUtils.setFieldInConcatString(cntValue,
                    "\\|",feature.getNormalConsumeCnt(), "1");
        }
        // 获得消费券 但是没有使用 即负样本
        if (StringUtils.notEmpty(couponId) && StringUtils.isEmpty(datePay)) {
            cntValue = StringUtils.setFieldInConcatString(cntValue,
                    "\\|", feature.getHasCouponNoUsedCnt(), "1");

        }
        // 有消费券并且已经使用
        if (StringUtils.notEmpty(datePay) && StringUtils.notEmpty(couponId)) {
            cntValue = StringUtils.setFieldInConcatString(cntValue,
                    "\\|",feature.getHasCouponUsedCnt(), "1");
            int interval = DateUtils.getDayTimeInterval(dateRecevied,datePay);
            if (interval < 15){
                cntValue = StringUtils.setFieldInConcatString(cntValue,
                        "\\|",feature.getLess15ConsumeCnt(), "1");
            }

            int coupponType = CouponType.couponDiscountType(discountRate);
            switch (coupponType) {

                case Constants.COUPON_DIRECT:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDirectDiscountCnt(), "1");

                    break;
                case Constants.COUPON_FIXED:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getFixedDiscountCnt(), "1");

                    break;
                case Constants.COUPON_50:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount50Cnt(), "1");

                    break;
                case Constants.COUPON_200:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount200Cnt(), "1");

                    break;
                case Constants.COUPON_500:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount500Cnt(), "1");

                    break;
                case Constants.COUPON_500_MORE:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount500MoreCnt(), "1");


            }
        }


            return  cntValue;



    }






}