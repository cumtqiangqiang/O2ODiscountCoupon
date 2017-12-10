package spark.extract.features;

/**
 * Created by fiona on 10/12/2017.
 */
public class UserFeatures  extends   CouponFeatures {


    @Override
    String getInitialCountValue() {
        return null;
    }

    @Override
    String getInitialRateValue() {
        return null;
    }

    @Override
    String getNormalConsumeCnt() {
        return null;
    }

    @Override
    String getHasCouponUsedCnt() {
        return null;
    }

    @Override
    String getHasCouponNoUsedCnt() {
        return null;
    }

    @Override
    String getLess15ConsumeCnt() {
        return null;
    }

    @Override
    String getFixedDiscountCnt() {
        return null;
    }

    @Override
    String getDirectDiscountCnt() {
        return null;
    }

    @Override
    String getDiscount50Cnt() {
        return null;
    }

    @Override
    String getDiscount200Cnt() {
        return null;
    }

    @Override
    String getDiscount500Cnt() {
        return null;
    }

    @Override
    String getDiscount500MoreCnt() {
        return null;
    }
}
