package spark.extract.features;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;
import spark.extract.features.constant.Constants;
import spark.extract.features.domain.MerchantFeatureModel;
import spark.extract.features.domain.UserFeatureModel;
import spark.extract.features.domain.UserMerchantFeature;
import utils.StringUtils;

import java.util.*;

/**
 * Created by UC227911 on 12/14/2017.
 */
public class OutputManager {

  public  static void saveFeatures(SQLContext sqlContext, JavaPairRDD<String,String> featureRDD,
                                   String path,int saveType){

      switch (saveType){
          case Constants.SAVE_USER_FEATURE_TYPE:
              saveUserFeatures(sqlContext,featureRDD, path);
               break;
          case  Constants.SAVE_MERCHANT_FEATURE_TYPE:
              saveMerchantFeatures(sqlContext,featureRDD, path);
              break;
           case  Constants.SAVE_USER_MER_FEATURE_TYPE:
               saveUserMerchantFeatures(sqlContext,featureRDD, path);

      }


  }

    private static void saveUserMerchantFeatures(SQLContext sqlContext,JavaPairRDD<String,String> rdd,String path){

        JavaRDD<UserMerchantFeature> userMerchantRDD = rdd.map(new Function<Tuple2<String, String>, UserMerchantFeature>() {
            private static final long serialVersionUID = 622939869243808509L;

            @Override
            public UserMerchantFeature call(Tuple2<String, String> v1) throws Exception {

                UserMerchantFeature userMerchantFeature = new UserMerchantFeature();
                String[] userMer = v1._1().split("-");

                userMerchantFeature.setUserId(userMer[0]);
                userMerchantFeature.setMerchantId(userMer[1]);
                userMerchantFeature.setUserPerMerchantNormalConsumeCnt(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MERCHANT_NORMATL_CONSUME_CNT));


                userMerchantFeature.setUserPerMerchantCouponConsumeCnt(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MERCHANT_COUPON_CONSUME_CNT));

                userMerchantFeature.setUserPerMerchantHasCouponNoUsedConsumeCnt(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MERCHANT_HASCOUPON_NOUSED_CONSUME_CNT));
                userMerchantFeature.setUserPerMerNormConsumeRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MER_NORM_CONSUME_RATE));

                userMerchantFeature.setUserPerMerCoupConsumeRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MER_COUPON_CONSUME_RATE));

                userMerchantFeature.setUserPerMerCoupNoUsedConsumeRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_PER_MER_COUPON_NOUSED_CONSUME_RATE));

                userMerchantFeature.setUserMerchantNormalConsumeRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_MERCHANT_NORMATL_CONSUME_RATE));
                userMerchantFeature.setUserMerchantCouponConsumeRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_MERCHANT_COUPON_CONSUME_RATE));


                userMerchantFeature.setUserMerchantHasCoupNoUsedRate(StringUtils.getFieldFromConcatString(v1._2(),
                        "\\|", Constants.USER_MERCHANT_NOUSE_COUP_CONSUME_RATE));
                return userMerchantFeature;
            }
        });


        DataFrame dataFrame = sqlContext.createDataFrame(userMerchantRDD, UserMerchantFeature.class);

        Map<String, String> options = new HashMap<String, String>();
        options.put("header", "true");
        dataFrame.write().format("com.databricks.spark.csv").option("header","true").save(path);


    }


    private static void saveUserFeatures(SQLContext sqlContext ,JavaPairRDD<String,String> userFeaturesRDD,String path){

        JavaRDD<UserFeatureModel> userFeatureModelJavaRDD = userFeaturesRDD.mapPartitions(new FlatMapFunction<Iterator<
                Tuple2<String, String>>, UserFeatureModel>() {


            private static final long serialVersionUID = 7675853076302368898L;

            @Override
            public Iterable<UserFeatureModel> call(Iterator<Tuple2<String, String>> tuple2Iterator) throws Exception {
                List<UserFeatureModel> models = new ArrayList<UserFeatureModel>();
                while (tuple2Iterator.hasNext()) {

                    Tuple2<String, String> userFeatuer = tuple2Iterator.next();
                    String userId = userFeatuer._1();
                    String feature = userFeatuer._2();

                    UserFeatureModel model = new UserFeatureModel();
                    model.setUserid(userId);
                    model.setAverageDiscountRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.AVERAGE_DISCOUNT_RATE));

                    model.setUserUniqueCousumeMerchantCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_UNIQUE_MERCHANT_COUNT));
                    model.setUserUniqueCouponCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_UNIQUE_COUPON_COUNT));
                    model.setUerConsumeCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_CONSUME_COUNT));

                    model.setUserNormalConsumeCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_NORMATL_CONSUME_COUNT));
                    model.setUserHasCouponUsedCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_HASCOUPON_USED_COUNT));
                    model.setUserHasCouponNoUsedCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_HASCOUPON_NOUSED_COUNT));

                    model.setUserDiscount50Cnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_50_COUNT));
                    model.setUserDiscount200Cnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_200_COUNT));
                    model.setUserDiscount500Cnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_500_COUNT));

                    model.setUserDiscountMore500Cnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_MORE_COUNT));
                    model.setUserDiscountFixedCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_FIXED_COUNT));
                    model.setUserDirectDiscountCnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_DIRECT_COUNT));
                    model.setUserCouponUseTimeIntervalLess15Cnt(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_LESS15_COUNT));
                    model.setUserDiscountChargeOffRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_CHARGEOFF_RATE));
                    model.setUserAverageDistance(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_AVERAGE_DISTANCE));
                    model.setUsernormalCounsumeRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_NORMAL_CONSUME_RATE));

                    model.setUserHasCouponNoUseRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_HAS_COUPON_NOUSE_RATE));
                    model.setUserHasCouponUseRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_HAS_COUPON_USE_RATE));
                    model.setUserDiscount50Rate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_50_RATE));
                    model.setUserDiscount200Rate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_200_RATE));
                    model.setUserDiscount500Rate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_500_RATE));
                    model.setUserDiscountMore500Rate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_MORE_RATE));
                    model.setUserDiscountFixedRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_FIXED_RATE));
                    model.setUserDirectDiscountRate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_DIRECT_RATE));
                    model.setUserCouponUseTimeIntervalLess15Rate(StringUtils.getFieldFromConcatString(feature, "\\|",
                            Constants.USER_DISCOUNT_LESS15_RATE));

                    models.add(model);

                }

                return models;


            }
        });


        DataFrame dataFrame = sqlContext.createDataFrame(userFeatureModelJavaRDD, UserFeatureModel.class);

        dataFrame.write().format("com.databricks.spark.csv").option("header","true").save(path);

    }


    private static void saveMerchantFeatures(SQLContext sqlContext,JavaPairRDD<String,String> merchantInfosRDD,String path){


        JavaRDD<MerchantFeatureModel> merchantFeatureModelJavaRDD = merchantInfosRDD.mapPartitions(new FlatMapFunction<Iterator<Tuple2<String, String>>, MerchantFeatureModel>() {
            private static final long serialVersionUID = 7675853076302368898L;

            @Override
            public Iterable<MerchantFeatureModel> call(Iterator<Tuple2<String, String>> iterator) throws Exception {

                List<MerchantFeatureModel> models = new ArrayList<MerchantFeatureModel>();
                while (iterator.hasNext()) {
                    Tuple2<String, String> merInfos = iterator.next();
                    String merchantId = merInfos._1();
                    String info = merInfos._2();

                    MerchantFeatureModel model = new MerchantFeatureModel();

                    model.setMerchantId(merchantId);
                    model.setMerUniqueCousumeUserCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_UNIQUE_USER_COUNT));
                    model.setMerUniqueCouponCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_UNIQUE_COUPON_COUNT));
                    model.setMerchantCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_COUNT));
                    model.setMerchantAverageDistance(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_AVERAGE_DISTANCE));
                    model.setMerchantNormalConsumeCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_NORMAL_CONSUME_COUNT));
                    model.setMerchantHasCouponNoUseConsumeCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT));
                    model.setMerchantHasCouponUsedConsumeCnt(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT));
                    model.setMerchantDiscount50Count(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_50_COUNT));
                    model.setMerchantDiscount200Count(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_200_COUNT));
                    model.setMerchantDiscount500Count(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_500_COUNT));
                    model.setMerchantDiscountMore500Count(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_MORE_COUNT));
                    model.setMerchantDiscountFixedCount(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_FIXED_COUNT));
                    model.setMerchantDirectDiscountCount(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_DIRECT_COUNT));
                    model.setMerchantDiscountLess15Count(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_LESS15_COUNT));
                    model.setMerchantCouponChargeOffRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_COUPON_CHARGEOFF_RATE));
                    model.setMerchantNormalConsumeRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_NORMAL_CONSUME_RATE));
                    model.setMerchantHasCouponNoUseConsumeRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_RATE));
                    model.setMerchantHasCouponUsedConsumeRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_HASCOUPON_USED_CONSUME_RATE));
                    model.setAverageDiscountRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.AVERAGE_DISCOUNT_RATE));
                    model.setMerchantDiscount50Rate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_50_RATE));
                    model.setMerchantDiscount200Rate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_200_RATE));
                    model.setMerchantDiscount500Rate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_500_RATE));
                    model.setMerchantDiscountMore500Rate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_MORE_RATE));
                    model.setMerchantDiscountFixedRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_FIXED_RATE));
                    model.setMerchantDirectDiscountRate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_DIRECT_RATE));
                    model.setMerchantDiscountLess15Rate(StringUtils.getFieldFromConcatString(info, "\\|",
                            Constants.MERCHANT_DISCOUNT_LESS15_RATE));


                    models.add(model);
                }


                return models;


            }
        });

        DataFrame dataFrame = sqlContext.createDataFrame(merchantFeatureModelJavaRDD, MerchantFeatureModel.class);

        dataFrame.write().format("com.databricks.spark.csv").option("header","true").save(path);




    }




}
