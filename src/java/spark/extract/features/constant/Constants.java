package spark.extract.features.constant;

/**
 * Created by UC227911 on 11/30/2017.
 */
public interface Constants {
    /**
     * 输入文件路径
     */
    String TRAIN_OFFLINE_DATA_PATH = "Resource/tb01ccf_offline_stage1_train.csv";
    String TRAIN_ONLINE_DATA_PATH = "Resource/tb02ccf_online_stage1_train.csv";
    String TEST_OFFLINE_DATA_PATH = "Resource/tb03ccf_offline_stage1_test_revised.csv";
    String LESS_OFFLINE_DATA_PATH = "TestResource/test_offline_stage1_train.csv";
    String LESS_ONLINE_DATA_PATH = "TestResource/onlineTest.csv";

    /**
     * 输出文件路径
     */
    // 训练线下数据
    String TRAIN_OFF_USER_FEATURE_PATH = "Resource/features/offline/trainUserFeature";
    String TRAIN_OFF_MERCHANT_FEATURE_PATH = "Resource/features/offline/trainMerFeature";
    String TRAIN_OFF_USER_MER_FEATURE_PATH = "Resource/features/offline/trainUserMerFeature";

    String TRAIN_ONLINE_USER_FEATURE_PATH = "Resource/features/online/trainUserFeature";
    String TRAIN_ONLINE_MERCHANT_FEATURE_PATH = "Resource/features/online/trainMerFeature";
    String TRAIN_ONLINE_USER_MER_FEATURE_PATH = "Resource/features/online/trainUserMerFeature";


    String LESS_OFFLINE_USER_DATA_PATH = "TestResource/offline/trainUserFeature";
    String LESS_OFFLINE_MER_DATA_PATH = "TestResource/offline/trainMerFeature";
    String LESS_OFFLINE_USER_MER_DATA_PATH = "TestResource/offline/trainUserMerFeature";

    String LESS_ONLINE_USER_DATA_PATH = "TestResource/online/trainUserFeature";
    String LESS_ONLINE_MER_DATA_PATH = "TestResource/online/trainMerFeature";
    String LESS_ONLINE_USER_MER_DATA_PATH = "TestResource/online/trainUserMerFeature";





    /**
     * 用户相关特征.
     */

    // 正常消费
    String USER_NORMATL_CONSUME_COUNT = "UserNormalConsumeCnt";
    // 获得优惠券后使用时间间隔
    String USER_DISCOUNT_LESS15_COUNT = "UserCouponUseTimeIntervalLess15Cnt";
    //用户消费优惠券的商家数量
    String USER_UNIQUE_MERCHANT_COUNT = "UserUniqueCousumeMerchantCnt";
    // 用户使用不同优惠券数量
    String USER_UNIQUE_COUPON_COUNT = "UserUniqueCouponCnt";
    // 用户消费总次数
    String USER_CONSUME_COUNT = "UserConsumeCnt";
    // 使用优惠券消费
    String USER_HASCOUPON_USED_COUNT = "UserHasCouponUsedCnt";
    // 有优惠券但是没有使用
    String USER_HASCOUPON_NOUSED_COUNT = "UserHasCouponNoUsedCnt";
    // 0~50 discount 满50减
    String  USER_DISCOUNT_50_COUNT = "UserDiscount50Cnt";
    // 50<discount<200
    String  USER_DISCOUNT_200_COUNT = "UserDiscount200Cnt";
    // 200<discount<500
    String  USER_DISCOUNT_500_COUNT = "UserDiscount500Cnt";
    // 500<discount
    String  USER_DISCOUNT_MORE_COUNT = "UserDiscountMore500Cnt";
    // 限时优惠活动.
    String  USER_DISCOUNT_FIXED_COUNT = "UserDiscountFixedCnt";
    // 直接以折扣的形式存在  不是满减
    String  USER_DISCOUNT_DIRECT_COUNT = "UserDirectDiscountCnt";
    // 用户点击数
    String USER_ACTION_0_COUNT = "UserActionClickCnt";
    // 用户购买数
    String USER_ACTION_1_COUNT = "UserActionBuyCnt";
    // 用户获得优惠券
    String USER_ACTION_2_COUNT = "UserActionGetCouponCnt";
    /**
     *
     * 用户不同的折扣的消费情况计算
     *
     */
    // 用户线上点击率
    String USER_ACTION_0_RATE = "UserActionClickRate";
    // 用户线上购买率
    String USER_ACTION_1_RATE = "UserActionBuyRate";
    // 用户线上获得优惠券率
    String USER_ACTION_2_RATE = "UserActionGetCouponRate";
    // 用户正常消费率
    String USER_NORMAL_CONSUME_RATE = "UsernormalCounsumeRate";
    // 用户有优惠券但是未使用率
    String USER_HAS_COUPON_NOUSE_RATE = "UserHasCouponNoUseRate";
    // 用户使用优惠券率
    String USER_HAS_COUPON_USE_RATE = "UserHasCouponUseRate";
    // 0~50 discount 满50减
    String USER_DISCOUNT_50_RATE = "UserDiscount50Rate";
    // 50<discount<200
    String  USER_DISCOUNT_200_RATE = "UserDiscount200Rate";
    // 200<discount<500
    String  USER_DISCOUNT_500_RATE = "UserDiscount500Rate";
    // 500<discount
    String  USER_DISCOUNT_MORE_RATE = "UserDiscountMore500Rate";
    // 限时优惠活动.
    String  USER_DISCOUNT_FIXED_RATE = "UserDiscountFixedRate";
    // 直接以折扣的形式存在  不是满减
    String  USER_DISCOUNT_DIRECT_RATE = "UserDirectDiscountRate";
    // 用户优惠券核销率
    String USER_DISCOUNT_CHARGEOFF_RATE = "UserDiscountChargeOffRate";
    // 用户少于15天就使用优惠券的频率
    String  USER_DISCOUNT_LESS15_RATE = "UserCouponUseTimeIntervalLess15Rate";

    // 计算用户的全部距离和，为了计算平均值
    String USER_AGGRE_DISTANCE = "UserAggreDistance";
    // 用户核销优惠券时距离商场的平均距离
    String USER_AVERAGE_DISTANCE = "UserAverageDistance";
    //最大距离
    String USER_MAX_DISTANCE = "UserMaxDistance";
    // 最小距离
    String USER_MIN_DISTANCE = "UserMinDistance";

    //全部优惠券的折扣（为了计算加的，不计入特征）
    String DISCOUNT_RATE = "DiscountRate";
    // 用户获得优惠券的平均折扣率
    String AVERAGE_DISCOUNT_RATE = "AverageDiscountRate";
    String User_DISCOUNT_INITIAL_COUNT = USER_CONSUME_COUNT + "=" +"0" +"|"
            + USER_AGGRE_DISTANCE + "=" +"-1"+"|"
            + DISCOUNT_RATE + "=" +"0"+"|"
            + USER_NORMATL_CONSUME_COUNT + "=" +"0"+"|"
            + USER_HASCOUPON_USED_COUNT + "=" +"0"+"|"
            + USER_HASCOUPON_NOUSED_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_50_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_200_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_500_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_MORE_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_FIXED_COUNT + "=" +"0"+"|"
            + USER_DISCOUNT_DIRECT_COUNT + "=" +"0" +"|"
            + USER_DISCOUNT_LESS15_COUNT + "=" + "0";


    String USER_DISCOUNT_INITIAL_RATE =  USER_DISCOUNT_CHARGEOFF_RATE +"=" +"0" +"|"
            + USER_AVERAGE_DISTANCE + "=" +"-1" + "|"
            + USER_NORMAL_CONSUME_RATE + "=" +"0" + "|"
            + USER_HAS_COUPON_NOUSE_RATE + "=" +"0" + "|"
            + USER_HAS_COUPON_USE_RATE + "=" +"0" + "|"
            + AVERAGE_DISCOUNT_RATE + "=" +"0" + "|"
            + USER_DISCOUNT_50_RATE +"=" +"0" +"|"
            + USER_DISCOUNT_200_RATE +"=" +"0" +"|"
            + USER_DISCOUNT_500_RATE +"=" +"0" +"|"
            + USER_DISCOUNT_MORE_RATE +"=" +"0" +"|"
            + USER_DISCOUNT_FIXED_RATE +"=" +"0" +"|"
            + USER_DISCOUNT_DIRECT_RATE +"=" +"0" + "|"
            + USER_DISCOUNT_LESS15_RATE +"=" +"0";



    /**
     * 以商户为中心进行计算不同折扣的消费情况
     *
     */
    // 商家数量
    String MERCHANT_COUNT = "MerchantCnt";

    //用户消费优惠券的商家数量
    String MERCHANT_UNIQUE_USER_COUNT = "MerUniqueCousumeUserCnt";
    // 用户使用不同优惠券数量
    String MERCHANT_UNIQUE_COUPON_COUNT = "MerUniqueCouponCnt";
    // 计算商家的全部距离和，为了计算平均值
    String MERCHANT_AGGRE_DISTANCE = "MerchantMerchantDistance";
    // 用户距离商场的平均距离
    String MERCHANT_AVERAGE_DISTANCE = "MerchantAverageDistance";
    //最大距离
    String MERCHANT_MAX_DISTANCE = "MerchantMaxDistance";
    // 最小距离
    String MERCHANT_MIN_DISTANCE = "MerchantMinDistance";


    // 正常消费
    String MERCHANT_NORMAL_CONSUME_COUNT = "MerchantNormalConsumeCnt";
    // 未使用优惠券进行消费
    String MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT = "MerchantHasCouponNoUseConsumeCnt";
   // 有优惠券并且使用
    String MERCHANT_HASCOUPON_USED_CONSUME_COUNT = "MerchantHasCouponUsedConsumeCnt";
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
    /**
     *
     * 商户不同的折扣的消费情况计算
     *
     */
    // 正常消费
    String MERCHANT_NORMAL_CONSUME_RATE = "MerchantNormalConsumeRate";
    // 未使用优惠券进行消费
    String MERCHANT_HASCOUPON_NOUSE_CONSUME_RATE = "MerchantHasCouponNoUseConsumeRate";
    // 有优惠券并且使用
    String MERCHANT_HASCOUPON_USED_CONSUME_RATE = "MerchantHasCouponUsedConsumeRate";

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

    String MERCHANT_DISCOUNT_INITIAL_COUNT = MERCHANT_COUNT + "=" +"0" +"|"
                                           + DISCOUNT_RATE + "=" +"0"+"|"
                                           + MERCHANT_AGGRE_DISTANCE + "=" +"-1"+"|"
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
                                            + MERCHANT_AVERAGE_DISTANCE +"=" +"0" +"|"
                                            + MERCHANT_NORMAL_CONSUME_RATE +"=" +"0" +"|"
                                            + MERCHANT_HASCOUPON_NOUSE_CONSUME_RATE +"=" +"0" +"|"
                                            + MERCHANT_HASCOUPON_USED_CONSUME_RATE +"=" +"0" +"|"
                                            + AVERAGE_DISCOUNT_RATE + "=" +"0" + "|"
                                            + MERCHANT_DISCOUNT_50_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_200_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_500_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_MORE_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_FIXED_RATE +"=" +"0" +"|"
                                            + MERCHANT_DISCOUNT_DIRECT_RATE +"=" +"0" + "|"
                                            + MERCHANT_DISCOUNT_LESS15_RATE +"=" +"0";

    /**
     * 用户对每个商家的特征
     */
    // 在每个店正常消费的次数
    String USER_PER_MERCHANT_NORMATL_CONSUME_CNT = "UserPerMerchantNormalConsumeCnt";
    // 在每个店优惠券消费的次数
    String USER_PER_MERCHANT_COUPON_CONSUME_CNT = "UserPerMerchantCouponConsumeCnt";
    // 在每个店获取优惠券但未消费的次数
    String USER_PER_MERCHANT_HASCOUPON_NOUSED_CONSUME_CNT = "UserPerMerchantHasCouponNoUsedConsumeCnt";
    // 用户在某个店正常消费占自己所有正常消费的比率
    String USER_MERCHANT_NORMATL_CONSUME_RATE = "UserMerchantNormalConsumeRate";
    // 用户在某个店使用优惠券消费占自己所有使用优惠券消费的比率
    String USER_MERCHANT_COUPON_CONSUME_RATE = "UserMerchantCouponConsumeRate";
    // 用户在某个店领取优惠券但未使用占自己所有领取优惠券未使用的比率
    String USER_MERCHANT_NOUSE_COUP_CONSUME_RATE = "UserMerchantHasCoupNoUsedRate";

    // 每个用户对每个店正常消费率
    String USER_PER_MER_NORM_CONSUME_RATE = "UserPerMerNormConsumeRate";
    // 每个用户对每个店使用优惠券消费率
    String USER_PER_MER_COUPON_CONSUME_RATE = "UserPerMerCoupConsumeRate";
    // 每个用户对每个店有优惠券但未使用消费率
    String USER_PER_MER_COUPON_NOUSED_CONSUME_RATE = "UserPerMerCoupNoUsedConsumeRate";

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

    /**
     * 用户线上操作类型.
     */
    int USER_ACTION_CLICK = 0;
    int USER_ACTION_BUY = 1;
    int USER_ACTION_GET_COUPON = 2;

    /**
     * 输出的特征类型.
     */
    int SAVE_USER_FEATURE_TYPE = 0;
    int SAVE_MERCHANT_FEATURE_TYPE = 1;
    int SAVE_USER_MER_FEATURE_TYPE = 2;

}
