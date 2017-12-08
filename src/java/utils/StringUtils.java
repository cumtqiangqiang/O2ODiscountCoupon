package utils;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

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

    /**
     * 去除字符串头或者尾部的字符串
     * @param str 字符串
     * @param delimiter  分隔符
     * @return
     */
    public static String trimSymbolInStr(String str,String delimiter) {
        if(str.startsWith(delimiter)) {
            str = str.substring(1);
        }
        if(str.endsWith(delimiter)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    /**
     * 从拼接的字符串中提取字段
     * @param str 字符串
     * @param delimiter 分隔符
     * @param field 字段
     * @return 字段值
     */

    public static String getFieldFromConcatString( String str,
                                                  String delimiter, String field) {
        try {
            String[] fields = str.split(delimiter);
            for(String concatField : fields) {
                // searchKeywords=|clickCategoryIds=1,2,3
                if(concatField.split("=").length == 2) {
                    String fieldName = concatField.split("=")[0];
                    String fieldValue = concatField.split("=")[1];
                    if(fieldName.equals(field)) {
                        return fieldValue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 给字段设置值
     * @param str 字符串
     * @param delimiter 分隔符
     * @param field 字段名
     * @param newFieldValue 新的field值
     * @return 字段值
     */
    public static String setFieldInConcatString(String str,
                                                String delimiter, String field, String newFieldValue) {
        String[] fields = str.split(delimiter);

        for(int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].split("=")[0];
            if(fieldName.equals(field)) {
                String concatField = fieldName + "=" + newFieldValue;
                fields[i] = concatField;
                break;
            }
        }

        StringBuffer buffer = new StringBuffer("");
        for(int i = 0; i < fields.length; i++) {
            buffer.append(fields[i]);
            if(i < fields.length - 1) {
                buffer.append("|");
            }
        }

        return buffer.toString();
    }

    public static String aggregate(String v1,String v2){

        String[] strs = v2.split("\\|");
        String results = v1;
        for (String str: strs){

            String[] kv = str.split("=");
            String oldV1 = getFieldFromConcatString(v1,"\\|",kv[0]);
            String oldV2 = getFieldFromConcatString(v2,"\\|",kv[0]);

            long newV = Long.valueOf(oldV1)+Long.valueOf(oldV2);
            String newVStr = String.valueOf(newV);
           results = setFieldInConcatString(results,"\\|",kv[0],newVStr);

        }


        return results;


    }




}
