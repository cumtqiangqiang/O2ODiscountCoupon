package spark.extract.features;

import com.google.common.base.Optional;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
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


         // 过滤掉没有优惠券的
        JavaPairRDD<String, Row> filterCouponRDD = rawDataRDD.filter(new Function<Tuple2<String, Row>, Boolean>() {
            private static final long serialVersionUID = -6164994096654132755L;

            @Override
            public Boolean call(Tuple2<String, Row> v1) throws Exception {
                Row row = v1._2();
                String couponId = row.getString(2);

                 return StringUtils.notEmpty(couponId);
            }
        }).persist(StorageLevel.MEMORY_AND_DISK());


        String[] offlinePath = {Constants.LESS_OFFLINE_USER_DATA_PATH,Constants.LESS_OFFLINE_MER_DATA_PATH,
                            Constants.LESS_OFFLINE_USER_MER_DATA_PATH};

        String[] onlinePath = {Constants.LESS_ONLINE_USER_DATA_PATH,Constants.LESS_ONLINE_MER_DATA_PATH,
                Constants.LESS_ONLINE_USER_MER_DATA_PATH};

        for (int i = 0; i < offlinePath.length; i++) {

            getUserConsumeFeatures(rawDataRDD,filterCouponRDD,jsc,false,sqlContext,
                    offlinePath[i],i);

            getUserConsumeFeatures(rawDataRDD,filterCouponRDD,jsc,true,sqlContext,
                    onlinePath[i],i);

            getMerchantConsume(rawDataRDD,filterCouponRDD,jsc,false,sqlContext,
                    offlinePath[i],i);

            getMerchantConsume(rawDataRDD,filterCouponRDD,jsc,false,sqlContext,
                    onlinePath[i],i);
        }



        jsc.stop();


    }

    private  static void getUserConsumeFeatures(final JavaPairRDD<String, Row> rawDataRDD,
                                                JavaPairRDD<String, Row> filterCouponRDD,
                                                JavaSparkContext jsc,
                                                final Boolean online,SQLContext sqlContext,
                                                String path,int featureType){

        /**
         * userid-uniqueMerchant > cnt
         */
        JavaPairRDD<String, Long> userId2UniqueMerchantCnt = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
                String, String>() {


            private static final long serialVersionUID = 7095759952436551348L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();

                String userId = row.getString(0);
                String merchantId = row.getString(1);


                return new Tuple2<String, String>(userId, userId + "-" + merchantId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {


            private static final long serialVersionUID = -247302287102927054L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                return new Tuple2<String, Long>(tuple._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = -6772071712194173593L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });


        /**
         * userid-uniqueCouponId -> cnt RDD
         */
        JavaPairRDD<String, Long> userid2UniqueCouponIdRDD = filterCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
                String, String>() {


            private static final long serialVersionUID = 4534205119235271065L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                String userId = row.getString(0);
                String couponId = row.getString(2);

                return new Tuple2<String, String>(userId, userId + "-" + couponId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            private static final long serialVersionUID = 7959581893957713246L;

            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                return new Tuple2<String, Long>(tuple._1(), 1L);
            }
        }).reduceByKey(new Function2<Long, Long, Long>() {
            private static final long serialVersionUID = -8427188166950557879L;

            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        });


        JavaPairRDD<String, Tuple2<Long, Optional<Long>>> userIdUniqueMerCoupRDD =userId2UniqueMerchantCnt
                .leftOuterJoin(userid2UniqueCouponIdRDD);

        JavaPairRDD<String, String> userUniqueMerCoupCntRDD = userIdUniqueMerCoupRDD.mapValues(new Function<Tuple2<Long,
                Optional<Long>>, String>() {


            private static final long serialVersionUID = -5925011230648490081L;

            @Override
            public String call(Tuple2<Long, Optional<Long>> v1) throws Exception {
                long uniqueCoup = v1._2.or(0L);

                long uniqueMer = v1._1();

                return Constants.USER_UNIQUE_MERCHANT_COUNT + "=" + uniqueMer + "|"
                        + Constants.USER_UNIQUE_COUPON_COUNT + "=" + uniqueCoup;
            }
        });

        Map<String, String> userid2UniqueMerCoupMap = userUniqueMerCoupCntRDD.collectAsMap();

        /**
         * 将 userid -> unique Merchant & unique Coupon map 广播出去，小数据 在map端join.
         */
        final Broadcast<Map<String, String>> userid2UniqueMerCoupBroadcast = jsc.broadcast(userid2UniqueMerCoupMap);

//        userUniqueMerCoupCntRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println(tuple2._1() + " : " + tuple2._2());
//
//            }
//        });

        System.out.println("----------------------------------------------------------");
        /**
         * userid -> 各种cnt RDD.
         */
        JavaPairRDD<String, String> userId2CntValue = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
                String, String>() {


            private static final long serialVersionUID = -2392405283969984175L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                String userId = row.getString(0);
                String cntValue = getValuefeatureMapHelper(row, new UserFeatures(),online);
                return new Tuple2<String, String>(userId, cntValue);
            }
        });

        JavaPairRDD<String, String> userId2AggrateCntRDD = userId2CntValue.reduceByKey(new Function2<String, String,
                String>() {
            private static final long serialVersionUID = 2821690552741018401L;

            @Override
            public String call(String v1, String v2) throws Exception {
                return StringUtils.aggregate(v1, v2);
            }
        });


//        userId2AggrateCntRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            private static final long serialVersionUID = -2084825518255726690L;
//
//            @Override
//            public void call(Tuple2<String, String> tuple) throws Exception {
//                System.out.println("userid:" + tuple._1() + " cntValue:" + tuple._2());
//            }
//        });
        System.out.println("-----------------------------------------------------------------------------");
        /**
         * userid -> 各种rate RDD   和cnt RDD 结合.
         */
        JavaPairRDD<String, String> userId2AggrateRateRDD  = userId2AggrateCntRDD.mapToPair(new PairFunction<Tuple2<String,String>, String, String>() {
            private static final long serialVersionUID = 6763945829437482826L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, String> tuple) throws Exception {

                String userId = tuple._1();
                String v1 = tuple._2();
                String rateV = calculateRate(v1, new UserFeatures(),online);


                Map<String, String> map = userid2UniqueMerCoupBroadcast.getValue();

                String mapV = map.get(userId);
                String userCntAndRateV =mapV + "|" + v1 + "|" + rateV;


                return new Tuple2<String, String>(userId,userCntAndRateV);

            }
        });

//        JavaPairRDD<String, String> userId2AggrateRateRDD  = userId2AggrateCntRDD.mapValues(new Function<String, String>() {
//
//
//            private static final long serialVersionUID = -6548323846341356764L;
//
//            @Override
//            public String call(String v1) throws Exception {
//
//                String rateV = calculateRate(v1, new UserFeatures(),online);
//
//                String userCntAndRateV = v1 + "|" + rateV;
//
//                return userCntAndRateV;
//            }
//        });
        userId2AggrateRateRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
            private static final long serialVersionUID = -2084825518255726690L;

            @Override
            public void call(Tuple2<String, String> tuple) throws Exception {
                System.out.println("userid:" + tuple._1() + " rateValue:" + tuple._2());
            }
        });



        OutputManager.saveFeatures(sqlContext,userId2AggrateRateRDD,path,featureType);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        Map<String, String> userIdCntMap = userId2AggrateCntRDD.collectAsMap();

        final Broadcast<Map<String, String>> mapBroadcast = jsc.broadcast(userIdCntMap);


        // 计算每个用户对每个商户正常消费，不消费，优惠券消费 所占总的各个消费类型的比重
        /**
         * userId-merchantId -> row    RDD
         */
        JavaPairRDD<String, Row> userMerchantId2RowRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, Row>() {


            private static final long serialVersionUID = -6075150712503133622L;

            @Override
            public Tuple2<String, Row> call(Tuple2<String, Row> tuple) throws Exception {
                Row row = tuple._2();
                String userId = row.getString(0);
                String merchantId = row.getString(1);

                return new Tuple2<String, Row>(userId + "-" + merchantId, row);
            }
        });

        /**
         * userid-merchantid -> diffConsumeCnt RDD
         *
         * diffConsumeCnt 正常消费  未使用优惠券   优惠券消费
         *
         */
        JavaPairRDD<String, String> userIdMerchantId2DiffConsumeRDD = userMerchantId2RowRDD.groupByKey().mapToPair(
                new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {


            private static final long serialVersionUID = 4996121220298113143L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {

                Iterator<Row> iterator = tuple._2().iterator();
                String userId = tuple._1().split("-")[0];
                Map<String,String> userFeatureMap = mapBroadcast.getValue();
                String userFeature = userFeatureMap.get(userId);

                float userNormalConsumeCnt =Float.valueOf(StringUtils.getFieldFromConcatString(userFeature, "\\|",
                        Constants.USER_NORMATL_CONSUME_COUNT));


                float userCouponConsumeCnt =Float.valueOf(StringUtils.getFieldFromConcatString(userFeature, "\\|",
                        Constants.USER_HASCOUPON_USED_COUNT));

                float userNoUsedCouponCnt =Float.valueOf(StringUtils.getFieldFromConcatString(userFeature, "\\|",
                        Constants.USER_HASCOUPON_NOUSED_COUNT));

                int normalConsumeCnt = 0;
                int couponConsumeCnt = 0;
                int noUsedCouponCnt = 0;
                while (iterator.hasNext()) {
                    Row row = iterator.next();

                    String couponId = row.getString(2);
                    String dataPay = row.getString(6);

                    if (StringUtils.isEmpty(couponId) && StringUtils.notEmpty(dataPay)) {
                        normalConsumeCnt++;
                    }
                    if (StringUtils.notEmpty(couponId) && StringUtils.notEmpty(dataPay)) {
                        couponConsumeCnt++;
                    }
                    if (StringUtils.notEmpty(couponId) && StringUtils.isEmpty(dataPay)){
                        noUsedCouponCnt++;
                    }


                }

                float userMerchantNormalRate = userNormalConsumeCnt > 0 ?
                               normalConsumeCnt/userNormalConsumeCnt:0.f;
                float userMerchantCoupUsedRate =userCouponConsumeCnt > 0 ?
                                couponConsumeCnt/userCouponConsumeCnt:0.f;
                float userMerchantHasCoupNoUsedRate =userNoUsedCouponCnt > 0 ?
                                noUsedCouponCnt/userNoUsedCouponCnt : 0.f;

                long perMerConsumCnt = normalConsumeCnt+couponConsumeCnt+noUsedCouponCnt;

                float userPerMerNormalConsumeRate = Float.valueOf(normalConsumeCnt)/perMerConsumCnt;
                float userPerMerCoupUsedConsumeRate = Float.valueOf(couponConsumeCnt)/perMerConsumCnt;
                float userPerMerHasCoupNoUsedConsumeRate = Float.valueOf(noUsedCouponCnt)/perMerConsumCnt;

                String normalCnt = Constants.USER_PER_MERCHANT_NORMATL_CONSUME_CNT + "=" + normalConsumeCnt;
                String couponCnt = Constants.USER_PER_MERCHANT_COUPON_CONSUME_CNT + "=" + couponConsumeCnt;
                String noUsedCnt = Constants.USER_PER_MERCHANT_HASCOUPON_NOUSED_CONSUME_CNT + "=" + noUsedCouponCnt;

                return new Tuple2<String, String>(tuple._1(), normalCnt + "|" + couponCnt + "|" + noUsedCnt + "|"
                        + Constants.USER_PER_MER_NORM_CONSUME_RATE + "=" + userPerMerNormalConsumeRate + "|"
                        + Constants.USER_PER_MER_COUPON_CONSUME_RATE + "=" + userPerMerCoupUsedConsumeRate + "|"
                        + Constants.USER_PER_MER_COUPON_NOUSED_CONSUME_RATE + "=" + userPerMerHasCoupNoUsedConsumeRate+ "|"
                        +Constants.USER_MERCHANT_NORMATL_CONSUME_RATE + "=" +userMerchantNormalRate + "|"
                        + Constants.USER_MERCHANT_COUPON_CONSUME_RATE + "=" + userMerchantCoupUsedRate + "|"
                        + Constants.USER_MERCHANT_NOUSE_COUP_CONSUME_RATE + "=" + userMerchantHasCoupNoUsedRate

                );
            }
        });

        
        OutputManager.saveFeatures(sqlContext,userIdMerchantId2DiffConsumeRDD,path,featureType);

//        userIdMerchantId2DiffConsumeRDD.sortByKey().foreach(new VoidFunction<Tuple2<String, String>>() {
//            @Override
//            public void call(Tuple2<String, String> tuple) throws Exception {
//
//                System.out.println(tuple._1() + " : " + tuple._2());
//            }
//        });

    }

    /**
     * merchant
     * @param rawDataRDD
     * @param online
     */
    private  static  void getMerchantConsume(final JavaPairRDD<String, Row> rawDataRDD,
                                             JavaPairRDD<String, Row> filterCouponRDD,
                                             JavaSparkContext jsc,
                                             final Boolean online,SQLContext sqlContext,
                                             String path,int featureType){

        /**
         * 商户消费的不同用户量
         */
        JavaPairRDD<String, Long> merchantId2uniqueUserRDD = rawDataRDD.mapToPair(new PairFunction<Tuple2<String, Row>, String, String>() {
            private static final long serialVersionUID = 6813455970266645101L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple) throws Exception {

                Row row = tuple._2();
                // 用户id
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
        // 商户使用的不同的优惠券量

        JavaPairRDD<String, Long> merchantUniqueCoupRDD = filterCouponRDD.mapToPair(new PairFunction<Tuple2<String, Row>,
                String, String>() {
            private static final long serialVersionUID = -7353585028640054067L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, Row> tuple2) throws Exception {
                Row row = tuple2._2();
                String merchantId = row.getString(1);
                String couponId = row.getString(2);

                return new Tuple2<String, String>(merchantId, merchantId + "-" + couponId);
            }
        }).distinct().mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {


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
        });


        JavaPairRDD<String, Tuple2<Long, Optional<Long>>> merchantUniqueRDD = merchantId2uniqueUserRDD
                .leftOuterJoin(merchantUniqueCoupRDD);

        Map<String, String> merchantUniqueMap = merchantUniqueRDD.mapValues(new Function<Tuple2<Long, Optional<Long>>, String>() {
            private static final long serialVersionUID = 2668912550256638046L;

            @Override
            public String call(Tuple2<Long, Optional<Long>> v1) throws Exception {
                long uniqueUserCnt = v1._1();
                long uniqueCoupCnt = v1._2().or(0L);


                return Constants.MERCHANT_UNIQUE_USER_COUNT + "=" + uniqueUserCnt + "|"
                        + Constants.MERCHANT_UNIQUE_COUPON_COUNT + "=" + uniqueCoupCnt;
            }
        }).collectAsMap();


        final Broadcast<Map<String, String>> broadcastMerMap = jsc.broadcast(merchantUniqueMap);


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

                String cntValue = getValuefeatureMapHelper(row,new MerchantFeatures(),online);

                return new Tuple2<String, String>(merchantId, cntValue);
            }
        });


        JavaPairRDD<String, String> merchantId2AggraRDD = merchantIdCntRDD.reduceByKey(new Function2<String, String, String>() {
            private static final long serialVersionUID = 3675211601993422635L;

            @Override
            public String call(String v1, String v2) throws Exception {
                return StringUtils.aggregate(v1, v2);
            }
        });

//        merchantId2AggraRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
//            private static final long serialVersionUID = 1341762709922471040L;
//
//            @Override
//            public void call(Tuple2<String, String> tuple2) throws Exception {
//                System.out.println("merchantId :" +tuple2._1()+" value:"+tuple2._2());
//            }
//        });

        System.out.println("---------------------------------------------------------------");
        /**
         * 不同折扣率计算
         */
        JavaPairRDD<String, String> merId2AllInfosRDD = merchantId2AggraRDD.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {


            private static final long serialVersionUID = -6545925975754043398L;

            @Override
            public Tuple2<String, String> call(Tuple2<String, String> tuple2) throws Exception {
                String merId = tuple2._1();
                String v1 = tuple2._2();
                String uniqueV = broadcastMerMap.getValue().get(merId);
                String initialRate = calculateRate(v1, new MerchantFeatures(), false);

                return new Tuple2<String, String>(merId, uniqueV + "|" + v1 + "|" + initialRate);
            }
        });

        merId2AllInfosRDD.foreach(new VoidFunction<Tuple2<String, String>>() {
            @Override
            public void call(Tuple2<String, String> tuple2) throws Exception {
                System.out.println(tuple2._1() + " :" + tuple2._2());
            }
        });

        OutputManager.saveFeatures(sqlContext,merId2AllInfosRDD,path,featureType);

    }


    private  static  String calculateRate(String v1,CouponFeatures feature,boolean isOnline){



        // 未使用优惠券
        float hasCouponNoUsedCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getHasCouponNoUsedCnt()));
        // 使用优惠券
        float couponUsedCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getHasCouponUsedCnt()));

        float normalConsumeCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getNormalConsumeCnt()));
        // 总消费次数
        float cnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getConsumeCnt()));

        float dis50cnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getDiscount50Cnt()));
        float dis200cnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getDiscount200Cnt()));

        float dis500cnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getDiscount500Cnt()));
        float disMoreCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getDiscount500MoreCnt()));
        float disDirectcnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getDirectDiscountCnt()));

        float disFixedcnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getFixedDiscountCnt()));

        float disLess15cnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getLess15ConsumeCnt()));

        // 全部折扣的和  计算平均折扣率
        float allDiscountRate = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                "\\|",feature.getCouponRate())) ;
        long discountCnt = 0;
        // 优惠券发放量
        float couponCnt = hasCouponNoUsedCnt + couponUsedCnt;
        String initialRate = feature.getInitialRateValue();
        Map<String,Float> rateMap = new HashMap<String, Float>();


        if (isOnline ){
            if (feature instanceof  UserFeatures) {
                UserFeatures userFeature = (UserFeatures) feature;

                initialRate = initialRate + "|" + Constants.USER_ACTION_0_RATE + "=" + "0" + "|"
                        + Constants.USER_ACTION_1_RATE + "=" + "0" + "|"
                        + Constants.USER_ACTION_2_RATE + "=" + "0";
                // clickAction
                float cliclCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|", userFeature.getUserAction(Constants.USER_ACTION_CLICK)));

                float buyCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|", userFeature.getUserAction(Constants.USER_ACTION_BUY)));
                float getActionCnt = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                        "\\|", userFeature.getUserAction(Constants.USER_ACTION_GET_COUPON)));


                float userClickRate = cliclCnt / cnt;
                float userBuyRate = buyCnt/ cnt;
                float userGetCouponRate = getActionCnt/ cnt;

                rateMap.put(userFeature.getUserActionRate(Constants.USER_ACTION_CLICK), userClickRate);
                rateMap.put(userFeature.getUserActionRate(Constants.USER_ACTION_BUY), userBuyRate);
                rateMap.put(userFeature.getUserActionRate(Constants.USER_ACTION_GET_COUPON), userGetCouponRate);
            }

        }else {
            Float aggreDistance = Float.valueOf(StringUtils.getFieldFromConcatString(v1,
                    "\\|",feature.getDistanceAggre()));
            if (aggreDistance != -1){
                float averageDistance = aggreDistance/couponUsedCnt;

                rateMap.put(feature.getDistanceAverage(),averageDistance);
            }


        }




        if (normalConsumeCnt > 0){
            float normalConsumeRate = normalConsumeCnt/cnt;
            rateMap.put(feature.getCouponNormalConsumeRate(),normalConsumeRate);


        }
        if (hasCouponNoUsedCnt > 0){
            float hasCouponNoUsedConsumeRate = hasCouponNoUsedCnt/cnt;
            rateMap.put(feature.getCouponHasNoUsedConsumeRate(),hasCouponNoUsedConsumeRate);

        }
        if (couponUsedCnt > 0){
            // 使用优惠券消费
            float hasCouponUsedConsumeRate = hasCouponNoUsedCnt/cnt;
            rateMap.put(feature.getCouponHasUsedConsumeRate(),hasCouponUsedConsumeRate);

            // 核销率
            float couponChargeOffRate = couponUsedCnt/couponCnt;
            rateMap.put(feature.getCouponChargeOffRate(),couponChargeOffRate);




        }
        if (dis50cnt > 0 ){
            discountCnt += dis50cnt;
            float dis50Rate = dis50cnt/cnt;
            rateMap.put(feature.getCoupon50Rate(),dis50Rate);
        }
        if (dis200cnt > 0){
            discountCnt += dis200cnt;
            float dis200Rate = dis200cnt/cnt;
            rateMap.put(feature.getCoupon200Rate(),dis200Rate);

        }
        if (dis500cnt > 0){
            discountCnt += dis500cnt;

            float dis500Rate = dis500cnt/cnt;
            rateMap.put(feature.getCoupon500Rate(),dis500Rate);
        }
        if (disMoreCnt > 0){
            discountCnt += disMoreCnt;
            float disMoreRate = disMoreCnt/cnt;
            rateMap.put(feature.getCoupon500MoreRate(),disMoreRate);
        }

        if (disDirectcnt > 0){
            discountCnt += disDirectcnt;
            float disDirctRate = disDirectcnt/cnt;
            rateMap.put(feature.getCouponDirectRate(),disDirctRate);

        }
        if (disFixedcnt > 0){
            float disFixRate=disDirectcnt/cnt;
            rateMap.put(feature.getCouponFixedRate(),disFixRate);
        }

        if (disLess15cnt > 0){
            float disLess15Rate= 1 - disLess15cnt/15;
            rateMap.put(feature.getCouponLess15ConsumeRate(),disLess15Rate);

        }

        float averageDiscountRate = allDiscountRate/Float.valueOf(discountCnt);
        if (averageDiscountRate > 0){
            rateMap.put(Constants.AVERAGE_DISCOUNT_RATE,averageDiscountRate);
        }


        for (Map.Entry<String,Float> entry : rateMap.entrySet()) {

            initialRate = StringUtils.setFieldInConcatString(initialRate, "\\|",
                    entry.getKey(), String.valueOf(entry.getValue()));
        }


       return initialRate;

    }


    private  static String getValuefeatureMapHelper(Row row,CouponFeatures feature,boolean isOnline){
        int couponIndex = 2;
        String cntValue =feature.getInitialCountValue();
        int actionId = 0;

        if (isOnline && feature instanceof UserFeatures){

           UserFeatures userFeatures = (UserFeatures)feature;
            couponIndex = 3;
            actionId = Integer.valueOf(row.getString(2));

            String actionKey =userFeatures.getUserAction(actionId) ;

            cntValue = cntValue + "|" + Constants.USER_ACTION_0_COUNT +"=" + "0" +"|"
                      + Constants.USER_ACTION_1_COUNT +"=" + "0" +"|"
                    + Constants.USER_ACTION_2_COUNT +"=" + "0" ;
            cntValue = StringUtils.setFieldInConcatString(cntValue,
                    "\\|",actionKey, "1");


        }
        // 优惠券id
        String couponId = row.getString(couponIndex);
        // 折扣
        String discountRate = row.getString(couponIndex+1);

        // 消费券获得日
        String dateRecevied = row.getString(5);
        // 消费券使用日
        String datePay = row.getString(6);


        cntValue = StringUtils.setFieldInConcatString(cntValue,
                "\\|", feature.getConsumeCnt(), "1");


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

           // 线下数据 处理用户distance特征
            if (couponIndex == 2){
                String distance = row.getString(4);

                if (StringUtils.notEmpty(distance)) {

                   String  cntValue1 = StringUtils.setFieldInConcatString(cntValue,
                            "\\|", feature.getDistanceAggre(), distance);
                   cntValue = cntValue1;
                }

            }

            int interval = DateUtils.getDayTimeInterval(dateRecevied,datePay);
            if (interval < 15){
                cntValue = StringUtils.setFieldInConcatString(cntValue,
                        "\\|",feature.getLess15ConsumeCnt(), "1");
            }

            int coupponType = CouponType.couponDiscountType(discountRate);
            float rate = 0.f;
            switch (coupponType) {

                case Constants.COUPON_DIRECT:
                     rate = CouponType.getDiscountRate(discountRate);
                     cntValue = StringUtils.setFieldInConcatString(cntValue ,
                                 "\\|",feature.getCouponRate(), String.valueOf(rate));
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDirectDiscountCnt(), "1");
                    break;
                case Constants.COUPON_FIXED:
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getFixedDiscountCnt(), "1");

                    break;
                case Constants.COUPON_50:

                      rate = CouponType.getDiscountRate(discountRate);

                     cntValue = StringUtils.setFieldInConcatString(cntValue ,
                                 "\\|",feature.getCouponRate(), String.valueOf(rate));

                     cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount50Cnt(), "1");

                    break;
                case Constants.COUPON_200:
                    rate = CouponType.getDiscountRate(discountRate);
                     cntValue = StringUtils.setFieldInConcatString(cntValue ,
                                 "\\|",feature.getCouponRate(), String.valueOf(rate));

                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount200Cnt(), "1");

                    break;
                case Constants.COUPON_500:
                     rate = CouponType.getDiscountRate(discountRate);
                      cntValue = StringUtils.setFieldInConcatString(cntValue ,
                                  "\\|",feature.getCouponRate(), String.valueOf(rate));
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount500Cnt(), "1");

                    break;
                case Constants.COUPON_500_MORE:
                     rate = CouponType.getDiscountRate(discountRate);                    
                      cntValue = StringUtils.setFieldInConcatString(cntValue ,           
                                  "\\|",feature.getCouponRate(), String.valueOf(rate));  
                    cntValue = StringUtils.setFieldInConcatString(cntValue,
                            "\\|",feature.getDiscount500MoreCnt(), "1");

                     break;

            }
        }


            return  cntValue;



    }





}