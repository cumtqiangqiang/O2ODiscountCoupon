package spark.extract.features;

import spark.extract.features.constant.Constants;

/**
 * Created by fiona on 10/12/2017.
 */
public class UserFeatures  extends   CouponFeatures {


    @Override
    String getInitialCountValue() {
        return Constants.User_DISCOUNT_INITIAL_COUNT;
    }

    @Override
    String getInitialRateValue() {
        return Constants.USER_DISCOUNT_INITIAL_RATE;
    }

    @Override
    String getConsumeCnt() {
        return Constants.USER_CONSUME_COUNT;
    }

    @Override
    String getNormalConsumeCnt() {
        return Constants.USER_NORMATL_CONSUME_COUNT;
    }

    @Override
    String getHasCouponUsedCnt() {
        return Constants.USER_HASCOUPON_USED_COUNT;
    }

    @Override
    String getHasCouponNoUsedCnt() {
        return Constants.USER_HASCOUPON_NOUSED_COUNT;
    }

    @Override
    String getLess15ConsumeCnt() {
        return Constants.USER_DISCOUNT_LESS15_COUNT;
    }

    @Override
    String getFixedDiscountCnt() {
        return Constants.USER_DISCOUNT_FIXED_COUNT;
    }

    @Override
    String getDirectDiscountCnt() {
        return Constants.USER_DISCOUNT_DIRECT_COUNT;
    }

    @Override
    String getCouponRate() {
        return Constants.DISCOUNT_RATE;
    }

    @Override
    String getDiscount50Cnt() {
        return Constants.USER_DISCOUNT_50_COUNT;
    }

    @Override
    String getDiscount200Cnt() {
        return Constants.USER_DISCOUNT_200_COUNT;
    }

    @Override
    String getDiscount500Cnt() {
        return Constants.USER_DISCOUNT_500_COUNT;
    }

    @Override
    String getDiscount500MoreCnt() {
        return Constants.USER_DISCOUNT_MORE_COUNT;
    }

    @Override
    String getCouponNormalConsumeRate() {
        return Constants.USER_NORMAL_CONSUME_RATE;
    }

    @Override
    String getCouponHasNoUsedConsumeRate() {
        return Constants.USER_HAS_COUPON_NOUSE_RATE;
    }

    @Override
    String getCouponHasUsedConsumeRate() {
        return Constants.USER_HAS_COUPON_USE_RATE;
    }

    @Override
    String getCouponLess15ConsumeRate() {
        return Constants.USER_DISCOUNT_LESS15_RATE;
    }

    @Override
    String getCouponChargeOffRate() {
        return Constants.USER_DISCOUNT_CHARGEOFF_RATE;
    }

    @Override
    String getCoupon50Rate() {
        return Constants.USER_DISCOUNT_50_RATE;
    }

    @Override
    String getCoupon200Rate() {
        return Constants.USER_DISCOUNT_200_RATE;
    }

    @Override
    String getCoupon500Rate() {
        return Constants.USER_DISCOUNT_500_RATE;
    }

    @Override
    String getCoupon500MoreRate() {
        return Constants.USER_DISCOUNT_MORE_RATE;
    }

    @Override
    String getCouponDirectRate() {
        return Constants.USER_DISCOUNT_DIRECT_RATE;
    }

    @Override
    String getCouponFixedRate() {
        return Constants.USER_DISCOUNT_FIXED_RATE;
    }
}
