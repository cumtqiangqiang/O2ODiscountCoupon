package spark.extract.features;

import spark.extract.features.constant.Constants;

/**
 * Created by fiona on 10/12/2017.
 */
public class MerchantFeatures extends CouponFeatures {


    @Override
    String getInitialCountValue() {
        return Constants.MERCHANT_DISCOUNT_INITIAL_COUNT;
    }

    @Override
    String getInitialRateValue() {
        return Constants.MERCHANT_DISCOUNT_INITIAL_RATE;
    }

    @Override
    String getConsumeCnt() {
        return Constants.MERCHANT_COUNT;
    }

    @Override
    String getNormalConsumeCnt() {
        return Constants.MERCHANT_NORMAL_CONSUME_COUNT;
    }

    @Override
    String getHasCouponUsedCnt() {
        return Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT;
    }

    @Override
    String getHasCouponNoUsedCnt() {
        return Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT;
    }

    @Override
    String getLess15ConsumeCnt() {

        return Constants.MERCHANT_DISCOUNT_LESS15_COUNT;
    }

    @Override
    String getFixedDiscountCnt() {
        return Constants.MERCHANT_DISCOUNT_FIXED_COUNT;
    }

    @Override
    String getDirectDiscountCnt() {
        return Constants.MERCHANT_DISCOUNT_DIRECT_COUNT;
    }

    @Override
    String getCouponRate() {
        return Constants.DISCOUNT_RATE;
    }

    @Override
    String getDiscount50Cnt() {
        return Constants.MERCHANT_DISCOUNT_50_COUNT;
    }

    @Override
    String getDiscount200Cnt() {
        return Constants.MERCHANT_DISCOUNT_200_COUNT;
    }

    @Override
    String getDiscount500Cnt() {
        return Constants.MERCHANT_DISCOUNT_500_COUNT;
    }

    @Override
    String getDiscount500MoreCnt() {
        return Constants.MERCHANT_DISCOUNT_MORE_COUNT;
    }

    @Override
    String getCouponNormalConsumeRate() {
        return Constants.MERCHANT_NORMAL_CONSUME_COUNT;
    }

    @Override
    String getCouponHasNoUsedConsumeRate() {
        return Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_RATE;
    }

    @Override
    String getCouponHasUsedConsumeRate() {
        return Constants.MERCHANT_HASCOUPON_USED_CONSUME_RATE;
    }

    @Override
    String getCouponLess15ConsumeRate() {
        return Constants.MERCHANT_DISCOUNT_LESS15_RATE;
    }

    @Override
    String getCouponChargeOffRate() {
        return Constants.MERCHANT_COUPON_CHARGEOFF_RATE;
    }

    @Override
    String getCoupon50Rate() {
        return Constants.MERCHANT_DISCOUNT_50_RATE;
    }

    @Override
    String getCoupon200Rate() {
        return Constants.MERCHANT_DISCOUNT_200_RATE;
    }

    @Override
    String getCoupon500Rate() {
        return Constants.MERCHANT_DISCOUNT_500_RATE;
    }

    @Override
    String getCoupon500MoreRate() {
        return Constants.MERCHANT_DISCOUNT_MORE_RATE;
    }

    @Override
    String getCouponDirectRate() {
        return Constants.MERCHANT_DISCOUNT_DIRECT_RATE;
    }

    @Override
    String getCouponFixedRate() {
        return Constants.MERCHANT_DISCOUNT_FIXED_RATE;
    }

    @Override
    String getDistanceAggre() {
        return Constants.MERCHANT_AGGRE_DISTANCE;
    }

    @Override
    String getDistanceAverage() {
        return Constants.MERCHANT_AVERAGE_DISTANCE;
    }
}
