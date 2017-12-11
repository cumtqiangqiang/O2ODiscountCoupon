package utils;

import spark.extract.features.constant.Constants;

/**
 * Created by UC227911 on 12/7/2017.
 */
public class CouponType {

  public  static  int couponDiscountType(String discount){
      if (StringUtils.isEmpty(discount)){
          return Constants.COUPON_NO;

      }

      if (!discount.contains(":")) {
          if (0.f< Float.valueOf(discount) && Float.valueOf(discount) < 1){

              return Constants.COUPON_DIRECT;

          }else if ("fixed".equals(discount)){
              return Constants.COUPON_FIXED;
          }else {

              System.out.println(String.format("s%  is wrong discount format",discount));
          }



      } else {

          int discountFull = Integer.valueOf(discount.split(":")[0]);
          if (discountFull <= 50) {
                return Constants.COUPON_50;
          } else if (discountFull <= 200) {
               return Constants.COUPON_200;
          } else if (discountFull <= 500) {
                return Constants.COUPON_500;
          } else {
                return Constants.COUPON_500_MORE;
          }


      }

      return -1;


  }

  public static float getDiscountRate(String discount){

    if (StringUtils.isEmpty(discount) || "fixed".equals(discount)){

        return 0.f;
    }else if (discount.contains(":")){
          String[] arr = discount.split(":");

          return Float.valueOf(arr[1])/Float.valueOf(arr[0]);

      } else  if ( 0 <Float.valueOf(discount) && Float.valueOf(discount) < 1){

        return Float.valueOf(discount);
    }

     return 0.f;

  }



}
