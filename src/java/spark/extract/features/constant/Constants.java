package spark.extract.features.constant;

/**
 * Created by UC227911 on 11/30/2017.
 */
public interface Constants {

   String TRAIN_OFFLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\Resource\\tb01ccf_offline_stage1_train.csv";
   String TRAIN_ONLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\Resource\\tb02ccf_online_stage1_train.csv";
   String TEST_OFFLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\Resource\\tb03ccf_offline_stage1_test_revised.csv";
   String LESS_OFFLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\TestResource\\test_offline_stage1_train.csv";

   String  USER_NORMAL_CONSUME_RATE = "normalCounsumeRate";
   String USER_HAS_COUPON_NOUSE_RATE="hasCouponNoUseRate";
   String USER_HAS_COUPON_USE_RATE  ="hasCouponUseRate";
   // 在所有优惠消费中 直接使用折扣消费率
   String USER_DIRECT_DISCOUNT_RATE ="directDiscountRate";
   // 获得优惠券后使用时间间隔
   String USER_COUPON_USE_TIPME_INTERVAL = "couponUseTimeInterval";
   //用户消费优惠券的商家数量
   String USER_COUPON_MERCHANT_COUNT = "userCousumeMerchantCount";

    // 用户使用不同优惠券数量
   String USER_COUPON_COUNT = "userCouponCount";


}
