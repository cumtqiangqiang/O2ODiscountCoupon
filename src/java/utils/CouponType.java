package utils;

import spark.extract.features.constant.Constants;

/**
 * Created by UC227911 on 12/7/2017.
 */
public class CouponType {

  public  static  int couponDiscountType(String discount){
      if (StringUtils.isEmpty(discount)){
          return -1;

      }

      if (discount.indexOf(":") == -1) {
          if (0.f< Float.valueOf(discount) && Float.valueOf(discount) < 1){

              return 0;
          }else if ("fixed".equals(discount)){
              return 1;
          }else {

              System.out.println(String.format("s%  is wrong discount format",discount));
          }



      } else {

          int discountFull = Integer.valueOf(discount.split(":")[0]);
          if (discountFull <= 50) {
                return 2;
          } else if (discountFull <= 200) {
               return 3;
          } else if (discountFull <= 500) {
                return 4;
          } else {
                return 5;
          }


      }

      return -1;


  }



}
