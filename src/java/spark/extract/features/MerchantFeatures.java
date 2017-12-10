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
}
