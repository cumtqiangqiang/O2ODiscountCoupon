package spark.extract.features;

import spark.extract.features.constant.Constants;

/**
 * Created by fiona on 10/12/2017.
 */
abstract class CouponFeatures {

    abstract String getInitialCountValue();

    abstract String getInitialRateValue();

    abstract String getConsumeCnt();

    abstract String getNormalConsumeCnt();
    abstract String getHasCouponUsedCnt();
    abstract String getHasCouponNoUsedCnt();
    abstract String getLess15ConsumeCnt();
    abstract String getFixedDiscountCnt();
    abstract String getDirectDiscountCnt();
    abstract String getCouponRate();
    abstract String getDiscount50Cnt();
    abstract String getDiscount200Cnt();
    abstract String getDiscount500Cnt();
    abstract String getDiscount500MoreCnt();

    abstract String getCouponNormalConsumeRate();
    abstract String getCouponHasNoUsedConsumeRate();
    abstract String getCouponHasUsedConsumeRate();
    abstract String getCouponLess15ConsumeRate();

    abstract String getCouponChargeOffRate();
    abstract String getCoupon50Rate();
    abstract String getCoupon200Rate();
    abstract String getCoupon500Rate();
    abstract String getCoupon500MoreRate();
    abstract String getCouponDirectRate();
    abstract String getCouponFixedRate();
    abstract String getDistanceAggre();
    abstract String getDistanceAverage();

}
