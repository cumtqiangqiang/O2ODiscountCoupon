package spark.train.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import utils.SparkUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by UC227911 on 12/28/2017.
 */
public class LRTrainModel {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("O2OCoupon")
                .setMaster("local");

        Logger.getLogger("org").setLevel(Level.ERROR);
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(jsc.sc());
        Map<String, String> options = new HashMap<String, String>();
        options.put("header", "true");
        options.put("path", "TestResource/features/feature.csv");
        DataFrame featureDf = sqlContext.load("com.databricks.spark.csv", options);

        options.put("path","TestResource/features/label_data.csv");
        DataFrame labelDf = sqlContext.load("com.databricks.spark.csv", options);

        LogisticRegression lr = new LogisticRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0.8);

//        LogisticRegressionModel lrModel = lr.fit(featureDf);
//        System.out.println("Coefficients: "
//                + lrModel.coefficients() + " Intercept: " + lrModel.intercept());

        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");

        HashingTF hashingTF = new HashingTF()
                .setNumFeatures(90)
                .setInputCol(tokenizer.getOutputCol())
                .setOutputCol("features");


        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{tokenizer,hashingTF,lr});


        PipelineModel pipelineModel = pipeline.fit(featureDf);

    }


}
