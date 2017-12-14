package spark.extract.features.domain;

import java.io.Serializable;

/**
 * Created by UC227911 on 12/12/2017.
 */
@SuppressWarnings("unused")
public class UserMerchantFeature  implements Serializable{
    private static final long serialVersionUID = 7474184074612883165L;


    //|UserPerMerCoupNoUsedConsumeRate=1.0|UserMerchantNormalConsumeRate=0.0|UserMerchantCouponConsumeRate=0.0|UserMerchantHasCoupNoUsedRate=0.5

    private String userId;
    private String merchantId;
    private String userPerMerchantNormalConsumeCnt;
    private String userPerMerchantCouponConsumeCnt;
    private String userPerMerchantHasCouponNoUsedConsumeCnt;
    private String userPerMerNormConsumeRate;
    private String userPerMerCoupConsumeRate;
    private String userPerMerCoupNoUsedConsumeRate;
    private String userMerchantNormalConsumeRate;
    private String userMerchantCouponConsumeRate;
    private String userMerchantHasCoupNoUsedRate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserPerMerchantNormalConsumeCnt() {
        return userPerMerchantNormalConsumeCnt;
    }

    public void setUserPerMerchantNormalConsumeCnt(String userPerMerchantNormalConsumeCnt) {
        this.userPerMerchantNormalConsumeCnt = userPerMerchantNormalConsumeCnt;
    }

    public String getUserPerMerchantCouponConsumeCnt() {
        return userPerMerchantCouponConsumeCnt;
    }

    public void setUserPerMerchantCouponConsumeCnt(String userPerMerchantCouponConsumeCnt) {
        this.userPerMerchantCouponConsumeCnt = userPerMerchantCouponConsumeCnt;
    }

    public String getUserPerMerchantHasCouponNoUsedConsumeCnt() {
        return userPerMerchantHasCouponNoUsedConsumeCnt;
    }

    public void setUserPerMerchantHasCouponNoUsedConsumeCnt(String userPerMerchantHasCouponNoUsedConsumeCnt) {
        this.userPerMerchantHasCouponNoUsedConsumeCnt = userPerMerchantHasCouponNoUsedConsumeCnt;
    }

    public String getUserPerMerNormConsumeRate() {
        return userPerMerNormConsumeRate;
    }

    public void setUserPerMerNormConsumeRate(String userPerMerNormConsumeRate) {
        this.userPerMerNormConsumeRate = userPerMerNormConsumeRate;
    }

    public String getUserPerMerCoupConsumeRate() {
        return userPerMerCoupConsumeRate;
    }

    public void setUserPerMerCoupConsumeRate(String userPerMerCoupConsumeRate) {
        this.userPerMerCoupConsumeRate = userPerMerCoupConsumeRate;
    }

    public String getUserPerMerCoupNoUsedConsumeRate() {
        return userPerMerCoupNoUsedConsumeRate;
    }

    public void setUserPerMerCoupNoUsedConsumeRate(String userPerMerCoupNoUsedConsumeRate) {
        this.userPerMerCoupNoUsedConsumeRate = userPerMerCoupNoUsedConsumeRate;
    }

    public String getUserMerchantNormalConsumeRate() {
        return userMerchantNormalConsumeRate;
    }

    public void setUserMerchantNormalConsumeRate(String userMerchantNormalConsumeRate) {
        this.userMerchantNormalConsumeRate = userMerchantNormalConsumeRate;
    }

    public String getUserMerchantCouponConsumeRate() {
        return userMerchantCouponConsumeRate;
    }

    public void setUserMerchantCouponConsumeRate(String userMerchantCouponConsumeRate) {
        this.userMerchantCouponConsumeRate = userMerchantCouponConsumeRate;
    }

    public String getUserMerchantHasCoupNoUsedRate() {
        return userMerchantHasCoupNoUsedRate;
    }

    public void setUserMerchantHasCoupNoUsedRate(String userMerchantHasCoupNoUsedRate) {
        this.userMerchantHasCoupNoUsedRate = userMerchantHasCoupNoUsedRate;
    }






}
