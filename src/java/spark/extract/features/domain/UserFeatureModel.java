package spark.extract.features.domain;

import java.io.Serializable;

/**
 * Created by UC227911 on 12/13/2017.
 */
@SuppressWarnings("unused")
public class UserFeatureModel implements Serializable {

    private static final long serialVersionUID = -8606767131409884798L;

    private String userid;
    private String userUniqueCousumeMerchantCnt;
    private String userUniqueCouponCnt;
    private String uerConsumeCnt;
//    private String userAggreDistance;
//    private String discountRate;
    private String userNormalConsumeCnt;
    private String userHasCouponUsedCnt;
    private String userHasCouponNoUsedCnt;

    private String userDiscount50Cnt;
    private String userDiscount200Cnt;
    private String userDiscount500Cnt;

    private String userDiscountMore500Cnt;
    private String userDiscountFixedCnt;
    private String userDirectDiscountCnt;
    private String userCouponUseTimeIntervalLess15Cnt;
    private String userDiscountChargeOffRate;
    private String userAverageDistance;
    private String usernormalCounsumeRate;

    private String userHasCouponNoUseRate;
    private String userHasCouponUseRate;
    private String averageDiscountRate;
    private String userDiscount50Rate;
    private String userDiscount200Rate;
    private String userDiscount500Rate;
    private String userDiscountMore500Rate;
    private String userDiscountFixedRate;
    private String userDirectDiscountRate;
    private String userCouponUseTimeIntervalLess15Rate;
    public String getUserDiscount50Cnt() {
        return userDiscount50Cnt;
    }

    public void setUserDiscount50Cnt(String userDiscount50Cnt) {
        this.userDiscount50Cnt = userDiscount50Cnt;
    }

    public String getUserDiscount200Cnt() {
        return userDiscount200Cnt;
    }

    public void setUserDiscount200Cnt(String userDiscount200Cnt) {
        this.userDiscount200Cnt = userDiscount200Cnt;
    }

    public String getUserDiscount500Cnt() {
        return userDiscount500Cnt;
    }

    public void setUserDiscount500Cnt(String userDiscount500Cnt) {
        this.userDiscount500Cnt = userDiscount500Cnt;
    }

    public String getUserDiscountMore500Cnt() {
        return userDiscountMore500Cnt;
    }

    public void setUserDiscountMore500Cnt(String userDiscountMore500Cnt) {
        this.userDiscountMore500Cnt = userDiscountMore500Cnt;
    }

    public String getUserDiscountFixedCnt() {
        return userDiscountFixedCnt;
    }

    public void setUserDiscountFixedCnt(String userDiscountFixedCnt) {
        this.userDiscountFixedCnt = userDiscountFixedCnt;
    }

    public String getUserDirectDiscountCnt() {
        return userDirectDiscountCnt;
    }

    public void setUserDirectDiscountCnt(String userDirectDiscountCnt) {
        this.userDirectDiscountCnt = userDirectDiscountCnt;
    }



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserUniqueCousumeMerchantCnt() {
        return userUniqueCousumeMerchantCnt;
    }

    public void setUserUniqueCousumeMerchantCnt(String userUniqueCousumeMerchantCnt) {
        this.userUniqueCousumeMerchantCnt = userUniqueCousumeMerchantCnt;
    }

    public String getUserUniqueCouponCnt() {
        return userUniqueCouponCnt;
    }

    public void setUserUniqueCouponCnt(String userUniqueCouponCnt) {
        this.userUniqueCouponCnt = userUniqueCouponCnt;
    }

    public String getUerConsumeCnt() {
        return uerConsumeCnt;
    }

    public void setUerConsumeCnt(String uerConsumeCnt) {
        this.uerConsumeCnt = uerConsumeCnt;
    }

//    public String getUserAggreDistance() {
//        return userAggreDistance;
//    }
//
//    public void setUserAggreDistance(String userAggreDistance) {
//        this.userAggreDistance = userAggreDistance;
//    }

//    public String getDiscountRate() {
//        return discountRate;
//    }
//
//    public void setDiscountRate(String discountRate) {
//        this.discountRate = discountRate;
//    }

    public String getUserNormalConsumeCnt() {
        return userNormalConsumeCnt;
    }

    public void setUserNormalConsumeCnt(String userNormalConsumeCnt) {
        this.userNormalConsumeCnt = userNormalConsumeCnt;
    }

    public String getUserHasCouponUsedCnt() {
        return userHasCouponUsedCnt;
    }

    public void setUserHasCouponUsedCnt(String userHasCouponUsedCnt) {
        this.userHasCouponUsedCnt = userHasCouponUsedCnt;
    }

    public String getUserHasCouponNoUsedCnt() {
        return userHasCouponNoUsedCnt;
    }

    public void setUserHasCouponNoUsedCnt(String userHasCouponNoUsedCnt) {
        this.userHasCouponNoUsedCnt = userHasCouponNoUsedCnt;
    }



    public String getUserCouponUseTimeIntervalLess15Cnt() {
        return userCouponUseTimeIntervalLess15Cnt;
    }

    public void setUserCouponUseTimeIntervalLess15Cnt(String userCouponUseTimeIntervalLess15Cnt) {
        this.userCouponUseTimeIntervalLess15Cnt = userCouponUseTimeIntervalLess15Cnt;
    }

    public String getUserDiscountChargeOffRate() {
        return userDiscountChargeOffRate;
    }

    public void setUserDiscountChargeOffRate(String userDiscountChargeOffRate) {
        this.userDiscountChargeOffRate = userDiscountChargeOffRate;
    }

    public String getUserAverageDistance() {
        return userAverageDistance;
    }

    public void setUserAverageDistance(String userAverageDistance) {
        this.userAverageDistance = userAverageDistance;
    }

    public String getUsernormalCounsumeRate() {
        return usernormalCounsumeRate;
    }

    public void setUsernormalCounsumeRate(String usernormalCounsumeRate) {
        this.usernormalCounsumeRate = usernormalCounsumeRate;
    }

    public String getUserHasCouponNoUseRate() {
        return userHasCouponNoUseRate;
    }

    public void setUserHasCouponNoUseRate(String userHasCouponNoUseRate) {
        this.userHasCouponNoUseRate = userHasCouponNoUseRate;
    }

    public String getUserHasCouponUseRate() {
        return userHasCouponUseRate;
    }

    public void setUserHasCouponUseRate(String userHasCouponUseRate) {
        this.userHasCouponUseRate = userHasCouponUseRate;
    }

    public String getAverageDiscountRate() {
        return averageDiscountRate;
    }

    public void setAverageDiscountRate(String averageDiscountRate) {
        this.averageDiscountRate = averageDiscountRate;
    }

    public String getUserDiscount50Rate() {
        return userDiscount50Rate;
    }

    public void setUserDiscount50Rate(String userDiscount50Rate) {
        this.userDiscount50Rate = userDiscount50Rate;
    }

    public String getUserDiscount200Rate() {
        return userDiscount200Rate;
    }

    public void setUserDiscount200Rate(String userDiscount200Rate) {
        this.userDiscount200Rate = userDiscount200Rate;
    }

    public String getUserDiscount500Rate() {
        return userDiscount500Rate;
    }

    public void setUserDiscount500Rate(String userDiscount500Rate) {
        this.userDiscount500Rate = userDiscount500Rate;
    }

    public String getUserDiscountMore500Rate() {
        return userDiscountMore500Rate;
    }

    public void setUserDiscountMore500Rate(String userDiscountMore500Rate) {
        this.userDiscountMore500Rate = userDiscountMore500Rate;
    }

    public String getUserDiscountFixedRate() {
        return userDiscountFixedRate;
    }

    public void setUserDiscountFixedRate(String userDiscountFixedRate) {
        this.userDiscountFixedRate = userDiscountFixedRate;
    }

    public String getUserDirectDiscountRate() {
        return userDirectDiscountRate;
    }

    public void setUserDirectDiscountRate(String userDirectDiscountRate) {
        this.userDirectDiscountRate = userDirectDiscountRate;
    }

    public String getUserCouponUseTimeIntervalLess15Rate() {
        return userCouponUseTimeIntervalLess15Rate;
    }

    public void setUserCouponUseTimeIntervalLess15Rate(String userCouponUseTimeIntervalLess15Rate) {
        this.userCouponUseTimeIntervalLess15Rate = userCouponUseTimeIntervalLess15Rate;
    }


}
