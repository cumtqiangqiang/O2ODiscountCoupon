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
    String LESS_OFFLINE_DATA_PATH = "C:\\Users\\UC227911\\Desktop\\Mine\\O2O\\O2ODiscountCoupon\\TestResource\\test_offline_stage1_train.csv";


    // 获得优惠券后使用时间间隔
    String USER_COUPON_USE_TIPME_INTERVAL = "couponUseTimeInterval";
    //用户消费优惠券的商家数量
    String USER_COUPON_MERCHANT_COUNT = "userCousumeMerchantCount";


    String USER_CONSUME_COUNT = "userConsumeCount";
    // 用户使用不同优惠券数量
    String USER_COUPON_COUNT = "userCouponCount";
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
     *
     * 用户不同的折扣的消费情况计算
     *
     */
    String USER_NORMAL_CONSUME_RATE = "normalCounsumeRate";
    String USER_HAS_COUPON_NOUSE_RATE = "hasCouponNoUseRate";
    String USER_HAS_COUPON_USE_RATE = "hasCouponUseRate";
    // 在所有优惠消费中 直接使用折扣消费率
    String USER_DIRECT_DISCOUNT_RATE = "directDiscountRate";
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
    // 优惠券核销率
    String MERCHANT_COUPON_CHARGEOFF_RATE = "MerchantCouponChargeOffRate";
    // 少于15天消费 越大越好 1 - diff/15
    String MERCHANT_DISCOUNT_LESS15_RATE = "MerchantDiscountLess15Rate";

    // 0~50 discount 满50减
    String MERCHANT_DISCOUNT_50_COUNT = "MerchantDiscount50Count";

    // 50<discount<200
    String MERCHANT_DISCOUNT_200_COUNT = "MerchantDiscount200Count";
    // 200<discount<500
    String MERCHANT_DISCOUNT_500_COUNT = "MerchantDiscount500Count";
    // 500<discount
    String MERCHANT_DISCOUNT_MORE_COUNT = "MerchantDiscountMore500Count";
    // 限时优惠活动.
    String MERCHANT_DISCOUNT_FIXED_COUNT = "MerchantDiscountFixedCount";
    // 直接以折扣的形式存在  不是满减
    String MERCHANT_DISCOUNT_DIRECT_COUNT = "MerchantDirectDiscountCount";
    // 15天内消费优惠券
    String MERCHANT_DISCOUNT_LESS15_COUNT = "MerchantDiscountLess15Count";

    String MERCHANT_DISCOUNT_INITIAL_COUNT = MERCHANT_COUNT + "=" +"0" +"|"
                                           + MERCHANT_NORMAL_CONSUME_COUNT + "=" +"0"+"|"
                                           + MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT + "=" +"0"+"|"
                                           + MERCHANT_HASCOUPON_USED_CONSUME_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_50_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_200_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_500_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_MORE_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_FIXED_COUNT + "=" +"0"+"|"
                                            + MERCHANT_DISCOUNT_DIRECT_COUNT + "=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_LESS15_COUNT + "=" + "0";

    String MERCHANT_DISCOUNT_INITIAL_RATE =  MERCHANT_COUPON_CHARGEOFF_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_50_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_200_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_500_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_MORE_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_FIXED_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_DIRECT_RATE +"=" +"0" + "|"
                                            + MERCHANT_DISCOUNT_LESS15_RATE +"=" +"0";

    /**
     * 优惠券的优惠类型
     */
    // 无
    int COUPON_NO = -1;
    // 折扣形式
    int COUPON_DIRECT = 0;
    // 限时优惠
    int COUPON_FIXED = 1;
    // 满50 减或低于50
    int COUPON_50 = 2;
    // 满200 减或低于200
    int COUPON_200 = 3;
    // 满 500 减或低于500
    int COUPON_500 = 4;
    // 高于500 满减
    int COUPON_500_MORE = 5;


}
