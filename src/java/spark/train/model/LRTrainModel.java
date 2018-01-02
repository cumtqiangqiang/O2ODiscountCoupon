package spark.train.model;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.BinaryLogisticRegressionSummary;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.classification.LogisticRegressionTrainingSummary;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
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
                .setMaster("local[2]");

        Logger.getLogger("org").setLevel(Level.ERROR);
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(jsc.sc());
        Map<String, String> options = new HashMap<String, String>();
        options.put("header", "true");
        options.put("path", "TestResource/train/train_features.csv");
        DataFrame featureDf = sqlContext.load("com.databricks.spark.csv", options);
//        featureDf.show(5);
        options.put("path", "TestResource/train/train_label.csv");
        DataFrame labelDf = sqlContext.load("com.databricks.spark.csv", options);
        DataFrame label = labelDf.select("label");
        DataFrame joinDf= featureDf.join(label);
        String[] joinColumns = joinDf.columns();
        String[] featureColumns = featureDf.columns();

        for (String col : joinColumns) {

            Column column = joinDf.col(col).cast(DataTypes.DoubleType);
            joinDf = joinDf.withColumn(col, column);

        }


        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(featureColumns)
                .setOutputCol("features");

        DataFrame transformFeatureDf = assembler.transform(joinDf);

//        Double[] regs = new Double[]{0.1,1d,10d,0.3,3d,5d,100d};
        Double[] regs = new Double[]{0d};
//        Double[] enps = new Double[]{0.8,8d,80d,800d,0.1};
        for (int i = 0;i < regs.length;i++){
            LogisticRegression lr = new LogisticRegression()
                    .setLabelCol("label")
                    .setMaxIter(10000)
                    .setRegParam(regs[i])
                    .setElasticNetParam(0.8);

            LogisticRegressionModel lrModel = lr.fit(transformFeatureDf);

            System.out.println("\n+++++++++ Binomial logistic regression's Coefficients: "
                    + lrModel.coefficients() + "\nBinomial Intercept: " + lrModel.intercept());


            LogisticRegressionTrainingSummary trainingSummary = lrModel.summary();

            double[] objectiveHistory = trainingSummary.objectiveHistory();
            for (double lossPerIteration : objectiveHistory) {
                System.out.println(lossPerIteration);
            }

            BinaryLogisticRegressionSummary binarySummary =
                    (BinaryLogisticRegressionSummary) trainingSummary;

            DataFrame roc = binarySummary.roc();
            roc.show();
//            roc.select("FPR").show();
            System.out.println(binarySummary.areaUnderROC());

//            DataFrame fMeasure = binarySummary.fMeasureByThreshold();
//            double maxFMeasure = fMeasure.select(functions.max("F-Measure")).head().getDouble(0);
//            double bestThreshold = fMeasure.where(fMeasure.col("F-Measure").equalTo(maxFMeasure))
//                    .select("threshold").head().getDouble(0);
//            lrModel.setThreshold(bestThreshold);
            System.out.println("------------------------------------------------");

        }


        jsc.stop();
    }


}
