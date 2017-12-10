package utils;

import java.util.List;

/**
 * Created by UC227911 on 12/1/2017.
 */
public class Calculator {

  public  static float getAverage(List<Float> list){

      int cnt = list.size();
      float sum = 0.f;
      for (float f:list) {
          sum += f;
      }

      return sum / cnt;
  }




}
