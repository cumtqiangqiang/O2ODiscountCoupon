package spark.extract.features.domain;

import java.io.Serializable;

/**
 * Created by UC227911 on 12/13/2017.
 */
@SuppressWarnings("unused")
public class MerchantFeatureModel implements Serializable {
    private static final long serialVersionUID = -8841879778776921904L;


    private String merchantId;
    private String merUniqueCousumeUserCnt;
    private String merUniqueCouponCnt;
    private String merchantCnt;
    private String merchantAverageDistance;
    private String merchantNormalConsumeCnt;
    private String merchantHasCouponNoUseConsumeCnt;
    private String merchantHasCouponUsedConsumeCnt;
    private String merchantDiscount50Count;
    private String merchantDiscount200Count;
    private String merchantDiscount500Count;
    private String merchantDiscountMore500Count;
    private String merchantDiscountFixedCount;
    private String merchantDirectDiscountCount;
    private String merchantDiscountLess15Count;
    private String merchantCouponChargeOffRate;
    private String merchantNormalConsumeRate;
    private String merchantHasCouponNoUseConsumeRate;
    private String merchantHasCouponUsedConsumeRate;
    private String averageDiscountRate;
    private String merchantDiscount50Rate;
    private String merchantDiscount200Rate;
    private String merchantDiscount500Rate;
    private String merchantDiscountMore500Rate;
    private String merchantDiscountFixedRate;
    private String merchantDirectDiscountRate;
    private String merchantDiscountLess15Rate;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }



    public String getMerUniqueCousumeUserCnt() {
        return merUniqueCousumeUserCnt;
    }

    public void setMerUniqueCousumeUserCnt(String merUniqueCousumeUserCnt) {
        this.merUniqueCousumeUserCnt = merUniqueCousumeUserCnt;
    }

    public String getMerUniqueCouponCnt() {
        return merUniqueCouponCnt;
    }

    public void setMerUniqueCouponCnt(String merUniqueCouponCnt) {
        this.merUniqueCouponCnt = merUniqueCouponCnt;
    }

    public String getMerchantCnt() {
        return merchantCnt;
    }

    public void setMerchantCnt(String merchantCnt) {
        this.merchantCnt = merchantCnt;
    }

    public String getMerchantAverageDistance() {
        return merchantAverageDistance;
    }

    public void setMerchantAverageDistance(String merchantAverageDistance) {
        this.merchantAverageDistance = merchantAverageDistance;
    }

    public String getMerchantNormalConsumeCnt() {
        return merchantNormalConsumeCnt;
    }

    public void setMerchantNormalConsumeCnt(String merchantNormalConsumeCnt) {
        this.merchantNormalConsumeCnt = merchantNormalConsumeCnt;
    }

    public String getMerchantHasCouponNoUseConsumeCnt() {
        return merchantHasCouponNoUseConsumeCnt;
    }

    public void setMerchantHasCouponNoUseConsumeCnt(String merchantHasCouponNoUseConsumeCnt) {
        this.merchantHasCouponNoUseConsumeCnt = merchantHasCouponNoUseConsumeCnt;
    }

    public String getMerchantHasCouponUsedConsumeCnt() {
        return merchantHasCouponUsedConsumeCnt;
    }

    public void setMerchantHasCouponUsedConsumeCnt(String merchantHasCouponUsedConsumeCnt) {
        this.merchantHasCouponUsedConsumeCnt = merchantHasCouponUsedConsumeCnt;
    }

    public String getMerchantDiscount50Count() {
        return merchantDiscount50Count;
    }

    public void setMerchantDiscount50Count(String merchantDiscount50Count) {
        this.merchantDiscount50Count = merchantDiscount50Count;
    }

    public String getMerchantDiscount200Count() {
        return merchantDiscount200Count;
    }

    public void setMerchantDiscount200Count(String merchantDiscount200Count) {
        this.merchantDiscount200Count = merchantDiscount200Count;
    }

    public String getMerchantDiscount500Count() {
        return merchantDiscount500Count;
    }

    public void setMerchantDiscount500Count(String merchantDiscount500Count) {
        this.merchantDiscount500Count = merchantDiscount500Count;
    }

    public String getMerchantDiscountMore500Count() {
        return merchantDiscountMore500Count;
    }

    public void setMerchantDiscountMore500Count(String merchantDiscountMore500Count) {
        this.merchantDiscountMore500Count = merchantDiscountMore500Count;
    }

    public String getMerchantDiscountFixedCount() {
        return merchantDiscountFixedCount;
    }

    public void setMerchantDiscountFixedCount(String merchantDiscountFixedCount) {
        this.merchantDiscountFixedCount = merchantDiscountFixedCount;
    }

    public String getMerchantDirectDiscountCount() {
        return merchantDirectDiscountCount;
    }

    public void setMerchantDirectDiscountCount(String merchantDirectDiscountCount) {
        this.merchantDirectDiscountCount = merchantDirectDiscountCount;
    }

    public String getMerchantDiscountLess15Count() {
        return merchantDiscountLess15Count;
    }

    public void setMerchantDiscountLess15Count(String merchantDiscountLess15Count) {
        this.merchantDiscountLess15Count = merchantDiscountLess15Count;
    }

    public String getMerchantCouponChargeOffRate() {
        return merchantCouponChargeOffRate;
    }

    public void setMerchantCouponChargeOffRate(String merchantCouponChargeOffRate) {
        this.merchantCouponChargeOffRate = merchantCouponChargeOffRate;
    }

    public String getMerchantNormalConsumeRate() {
        return merchantNormalConsumeRate;
    }

    public void setMerchantNormalConsumeRate(String merchantNormalConsumeRate) {
        this.merchantNormalConsumeRate = merchantNormalConsumeRate;
    }

    public String getMerchantHasCouponNoUseConsumeRate() {
        return merchantHasCouponNoUseConsumeRate;
    }

    public void setMerchantHasCouponNoUseConsumeRate(String merchantHasCouponNoUseConsumeRate) {
        this.merchantHasCouponNoUseConsumeRate = merchantHasCouponNoUseConsumeRate;
    }

    public String getMerchantHasCouponUsedConsumeRate() {
        return merchantHasCouponUsedConsumeRate;
    }

    public void setMerchantHasCouponUsedConsumeRate(String merchantHasCouponUsedConsumeRate) {
        this.merchantHasCouponUsedConsumeRate = merchantHasCouponUsedConsumeRate;
    }

    public String getAverageDiscountRate() {
        return averageDiscountRate;
    }

    public void setAverageDiscountRate(String averageDiscountRate) {
        this.averageDiscountRate = averageDiscountRate;
    }

    public String getMerchantDiscount50Rate() {
        return merchantDiscount50Rate;
    }

    public void setMerchantDiscount50Rate(String merchantDiscount50Rate) {
        this.merchantDiscount50Rate = merchantDiscount50Rate;
    }

    public String getMerchantDiscount200Rate() {
        return merchantDiscount200Rate;
    }

    public void setMerchantDiscount200Rate(String merchantDiscount200Rate) {
        this.merchantDiscount200Rate = merchantDiscount200Rate;
    }

    public String getMerchantDiscount500Rate() {
        return merchantDiscount500Rate;
    }

    public void setMerchantDiscount500Rate(String merchantDiscount500Rate) {
        this.merchantDiscount500Rate = merchantDiscount500Rate;
    }

    public String getMerchantDiscountMore500Rate() {
        return merchantDiscountMore500Rate;
    }

    public void setMerchantDiscountMore500Rate(String merchantDiscountMore500Rate) {
        this.merchantDiscountMore500Rate = merchantDiscountMore500Rate;
    }

    public String getMerchantDiscountFixedRate() {
        return merchantDiscountFixedRate;
    }

    public void setMerchantDiscountFixedRate(String merchantDiscountFixedRate) {
        this.merchantDiscountFixedRate = merchantDiscountFixedRate;
    }

    public String getMerchantDirectDiscountRate() {
        return merchantDirectDiscountRate;
    }

    public void setMerchantDirectDiscountRate(String merchantDirectDiscountRate) {
        this.merchantDirectDiscountRate = merchantDirectDiscountRate;
    }

    public String getMerchantDiscountLess15Rate() {
        return merchantDiscountLess15Rate;
    }

    public void setMerchantDiscountLess15Rate(String merchantDiscountLess15Rate) {
        this.merchantDiscountLess15Rate = merchantDiscountLess15Rate;
    }
}
