package utils;

import spark.extract.features.config.ConfigManager;
import spark.extract.features.constant.Constants;

/**
 * Created by UC227911 on 12/19/2017.
 */
public class SparkUtil {

    private static Boolean test = ConfigManager.getBoolean(Constants.DATA_LESS_TEST);


    public static String getOfflineInputPath() {


        if (test) {

            return Constants.LESS_OFFLINE_DATA_PATH;

        }

        return Constants.TRAIN_OFFLINE_DATA_PATH;


    }


    public static String getOnlineInputPath() {


        if (test) {

            return Constants.LESS_ONLINE_DATA_PATH;

        }

        return Constants.TRAIN_ONLINE_DATA_PATH;


    }


    public static String getOfflineOutputFeaturePath(int saveType){
       String path = "";
        switch (saveType){
            case Constants.SAVE_USER_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_OFFLINE_USER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_OFF_USER_FEATURE_PATH;
                }



                break;
            case Constants.SAVE_MERCHANT_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_OFFLINE_MER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_OFF_MERCHANT_FEATURE_PATH;
                }


                break;
            case Constants.SAVE_USER_MER_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_OFFLINE_USER_MER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_OFF_USER_MER_FEATURE_PATH;
                }


        }

        return  path;


    }


    public static String getOnlineOutputFeaturePath(int saveType){
        String path = "";
        switch (saveType){
            case Constants.SAVE_USER_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_ONLINE_USER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_ONLINE_USER_FEATURE_PATH;
                }



                break;
            case Constants.SAVE_MERCHANT_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_ONLINE_MER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_ONLINE_MERCHANT_FEATURE_PATH;
                }


                break;
            case Constants.SAVE_USER_MER_FEATURE_TYPE:
                if (test){
                    path = Constants.LESS_ONLINE_USER_MER_FEATURE_PATH;
                }else {

                    path = Constants.TRAIN_ONLINE_USER_MER_FEATURE_PATH;
                }


        }

        return  path;


    }


}
