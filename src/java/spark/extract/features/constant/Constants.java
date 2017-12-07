package spark.extract.features.constant;

/**
 * Created by UC227911 on 11/30/2017.
 */
public interface Constants {
    /**
     * 文件路径
     */
    String TRAIN_OFFLINE_DATA_PATH = "/Users/fiona/Desktop/ML/O2ODiscountCoupon/Resource/tb01ccf_offline_stage1_train.csv";
    String TRAIN_ONLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\Resource\\tb02ccf_online_stage1_train.csv";
    String TEST_OFFLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\Resource\\tb03ccf_offline_stage1_test_revised.csv";
    String LESS_OFFLINE_DATA_PATH = "/Users/fiona/Desktop/ML/O2ODiscountCoupon/TestResource/test_offline_stage1_train.csv";

    String USER_NORMAL_CONSUME_RATE = "normalCounsumeRate";
    String USER_HAS_COUPON_NOUSE_RATE = "hasCouponNoUseRate";
    String USER_HAS_COUPON_USE_RATE = "hasCouponUseRate";
    // 在所有优惠消费中 直接使用折扣消费率
    String USER_DIRECT_DISCOUNT_RATE = "directDiscountRate";
    // 获得优惠券后使用时间间隔
    String USER_COUPON_USE_TIPME_INTERVAL = "couponUseTimeInterval";
    //用户消费优惠券的商家数量
    String USER_COUPON_MERCHANT_COUNT = "userCousumeMerchantCount";

    // 使用优惠券消费的数目
    String USER_CONSUME_USE_COUPON_COUNT = "userConsumeUseCoupon";

    String USER_CONSUME_COUNT = "userConsumeCount";
    // 用户使用不同优惠券数量
    String USER_COUPON_COUNT = "userCouponCount";

    /**
     *
     * 用户不同的折扣的消费情况计算
     *
     */
    // 0~50 discount 满50减
    String DISCOUNT_50_RATE = "discount50Rate";
    // 50<discount<200
    String DISCOUNT_200_RATE = "discount200Rate";
    // 200<discount<500
    String DISCOUNT_500_RATE = "discount500Rate";
    // 500<discount
    String DISCOUNT_MORE_RATE = "discountMore500Rate";
    // 限时优惠活动.
    String DISCOUNT_FIXED_RATE = "discountFixedRate";
    // 直接以折扣的形式存在  不是满减
    String DISCOUNT_DIRECT_RATE = "directDiscountRate";


    // 0~50 discount 满50减
    String DISCOUNT_50_COUNT = "discount50RateCount";

    // 50<discount<200
    String DISCOUNT_200_COUNT = "discount200RateCount";
    // 200<discount<500
    String DISCOUNT_500_COUNT = "discount500RateCount";
    // 500<discount
    String DISCOUNT_MORE_COUNT = "discountMore500RateCount";
    // 限时优惠活动.
    String DISCOUNT_FIXED_COUNT = "discountFixedRateCount";
    // 直接以折扣的形式存在  不是满减
    String DISCOUNT_DIRECT_COUNT = "directDiscountRateCount";

    /**
     * 以商户为中心进行计算不同折扣的消费情况
     *
     */
    // 商家数量
    String MERCHANT_COUNT = "MerchantCnt";

    // 正常消费
    String MERCHANT_NORMAL_CONSUME_COUNT = "MerchantNormalConsumeCnt";
    // 未使用优惠券进行消费
    String MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT = "MerchantHasCouponNoUseConsumeCnt";
   // 有优惠券并且使用
    String MERCHANT_HASCOUPON_USED_CONSUME_COUNT = "MerchantHasCouponUsedConsumeCnt";
    // 优惠券核销率
    String MERCHANT_COUPON_CHARGEOFF = "MerchantCouponChargeOff";

    /**
     *
     * 商户不同的折扣的消费情况计算
     *
     */
    // 0~50 discount 满50减
    String MERCHANT_DISCOUNT_50_RATE = "MerchantDiscount50Rate";
    // 50<discount<200
    String MERCHANT_DISCOUNT_200_RATE = "MerchantDiscount200Rate";
    // 200<discount<500
    String MERCHANT_DISCOUNT_500_RATE = "MerchantDiscount500Rate";
    // 500<discount
    String MERCHANT_DISCOUNT_MORE_RATE = "MerchantDiscountMore500Rate";
    // 限时优惠活动.
    String MERCHANT_DISCOUNT_FIXED_RATE = "MerchantDiscountFixedRate";
    // 直接以折扣的形式存在  不是满减
    String MERCHANT_DISCOUNT_DIRECT_RATE = "MerchantDirectDiscountRate";


    // 0~50 discount 满50减
    String MERCHANT_DISCOUNT_50_COUNT = "MerchantDiscount50RateCount";

    // 50<discount<200
    String MERCHANT_DISCOUNT_200_COUNT = "MerchantDiscount200RateCount";
    // 200<discount<500
    String MERCHANT_DISCOUNT_500_COUNT = "MerchantDiscount500RateCount";
    // 500<discount
    String MERCHANT_DISCOUNT_MORE_COUNT = "MerchantDiscountMore500RateCount";
    // 限时优惠活动.
    String MERCHANT_DISCOUNT_FIXED_COUNT = "MerchantDiscountFixedRateCount";
    // 直接以折扣的形式存在  不是满减
    String MERCHANT_DISCOUNT_DIRECT_COUNT = "MerchantDirectDiscountRateCount";

    String MERCHANT_DISCOUNT_INITIAL_COUNT = Constants.MERCHANT_COUNT + "=" +"0" +"|"
            + Constants.MERCHANT_NORMAL_CONSUME_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_50_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_200_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_500_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_MORE_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_FIXED_COUNT + "=" +"0"+"|"
            + Constants.MERCHANT_DISCOUNT_DIRECT_COUNT + "=" +"0";




}
