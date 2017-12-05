package utils;

/**
 * Created by UC227911 on 11/30/2017.
 */
public class StringUtils {
    public static boolean isEmpty(String str){
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static  boolean notEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean hasDelimiter(String tartStr,String delimiter){

        if (tartStr.indexOf(delimiter) == -1){
            return  false;
        }
        return  true;

    }

}
