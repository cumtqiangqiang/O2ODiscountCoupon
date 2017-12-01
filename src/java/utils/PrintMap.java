package utils;

import java.util.Map;

/**
 * Created by UC227911 on 12/1/2017.
 */
public class PrintMap {
      public  static  void printMap( String name,Map<String,Long> map){

          System.out.println(name);
          for (Map.Entry<String,Long> entry:map.entrySet()) {
              System.out.println("userid : "+entry.getKey() + " value : "+entry.getValue());
          }


      }

}
