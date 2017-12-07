package spark.extract.features;


import org.apache.spark.AccumulatorParam;
import spark.extract.features.constant.Constants;
import utils.StringUtils;

/**
 * Created by UC227911 on 12/7/2017.
 */
public class MerchantAggrAccumulator  implements AccumulatorParam<String>{


    private static final long serialVersionUID = 965923901306345495L;

    @Override
    public String zero(String initialValue) {
        return Constants.MERCHANT_COUNT + "=" +"0"
                + Constants.MERCHANT_NORMAL_CONSUME_COUNT + "=" +"0"
                + Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT + "=" +"0"
                + Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_50_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_200_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_500_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_MORE_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_FIXED_COUNT + "=" +"0"
                + Constants.MERCHANT_DISCOUNT_DIRECT_COUNT + "=" +"0";
    }

    @Override
    public String addAccumulator(String t1, String t2) {
        return add(t1,t2);



    }

    @Override
    public String addInPlace(String r1, String r2) {
        return add(r1,r2);
    }


    private String add(String v1,String v2){
        if(StringUtils.isEmpty(v1)) {
            return v2;
        }

        String oldValue = StringUtils.getFieldFromConcatString(v1,"\\|",v2);
        if (oldValue != null){
            int newValue = Integer.valueOf(oldValue) + 1;

            return StringUtils.setFieldInConcatString(v1,"\\|",v2,String.valueOf(newValue));
        }


         return v1;
    }


}
