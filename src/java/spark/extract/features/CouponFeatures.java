package spark.extract.features;

import spark.extract.features.constant.Constants;

/**
 * Created by fiona on 10/12/2017.
 */
abstract class CouponFeatures {

    abstract String getInitialCountValue();

    abstract String getInitialRateValue();

    String getMerchangCnt(){
        return Constants.MERCHANT_COUNT;
    };

    abstract String getNormalConsumeCnt();
    abstract String getHasCouponUsedCnt();
    abstract String getHasCouponNoUsedCnt();
    abstract String getLess15ConsumeCnt();
    abstract String getFixedDiscountCnt();
    abstract String getDirectDiscountCnt();
    abstract String getDiscount50Cnt();
    abstract String getDiscount200Cnt();
    abstract String getDiscount500Cnt();
    abstract String getDiscount500MoreCnt();





}
