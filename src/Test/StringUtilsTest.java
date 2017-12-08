import spark.extract.features.constant.Constants;
import utils.StringUtils;

/**
 * Created by fiona on 7/12/2017.
 */
public class StringUtilsTest {
    public static void main(String[] args) {

        String str1 = Constants.MERCHANT_COUNT + "=" +"1"+"|"
                + Constants.MERCHANT_NORMAL_CONSUME_COUNT + "=" +"1"+"|"
                + Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT + "=" +"2"+"|"
                + Constants.MERCHANT_DISCOUNT_50_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_200_COUNT + "=" +"1"+"|"
                + Constants.MERCHANT_DISCOUNT_500_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_MORE_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_FIXED_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_DIRECT_COUNT + "=" +"0";


        String str2 = Constants.MERCHANT_COUNT + "=" +"1"+"|"
                + Constants.MERCHANT_NORMAL_CONSUME_COUNT + "=" +"1"+"|"
                + Constants.MERCHANT_HASCOUPON_NOUSE_CONSUME_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_HASCOUPON_USED_CONSUME_COUNT + "=" +"2"+"|"
                + Constants.MERCHANT_DISCOUNT_50_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_200_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_500_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_MORE_COUNT + "=" +"3"+"|"
                + Constants.MERCHANT_DISCOUNT_FIXED_COUNT + "=" +"0"+"|"
                + Constants.MERCHANT_DISCOUNT_DIRECT_COUNT + "=" +"0";




        long a = 5L;
        long b = 7L;
        System.out.println(Float.valueOf(a)/b);


    }




}
