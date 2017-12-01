package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by UC227911 on 12/1/2017.
 */
public class DateUtils {

    public  static  int getDayTimeInterval(String startTime,String endTime){
        SimpleDateFormat df  = new SimpleDateFormat("yyyyMMdd");
        Date sDate = null;
        Date eDate = null;

        try{
            sDate = df.parse(startTime);
            eDate = df.parse(endTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        int days = (int) ((eDate.getTime() - sDate.getTime()) / (1000*3600*24));
        return days;
    }


}
