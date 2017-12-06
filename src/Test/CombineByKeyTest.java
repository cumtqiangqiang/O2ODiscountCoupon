import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Int;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UC227911 on 12/6/2017.
 */
public class CombineByKeyTest {



    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Test");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        List<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String, Integer>>();
        list.add(new Tuple2<String, Integer>("pandas",2));
        list.add(new Tuple2<String, Integer>("hello",2));
        list.add(new Tuple2<String, Integer>("pandas",6));
        list.add(new Tuple2<String, Integer>("hello",2));
        JavaPairRDD<String, Integer> parallelizeRDD = jsc.parallelize(list).mapToPair(new PairFunction<Tuple2<String, Integer>,
                String, Integer>() {


            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {
                return new Tuple2<String, Integer>(t._1(), t._2());
            }
        });

        Function<Integer, Tuple2<Integer, Integer>> function = new Function<Integer, Tuple2<Integer, Integer>>() {

            @Override
            public Tuple2<Integer, Integer> call(Integer v1) throws Exception {
                return new Tuple2<Integer, Integer>(v1, 1);
            }
        };


        Function2<Tuple2<Integer, Integer>, Integer, Tuple2<Integer, Integer>> function2 = new Function2<Tuple2<Integer, Integer>, Integer, Tuple2<Integer, Integer>>() {


            @Override
            public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> v1, Integer v2) throws Exception {
                return new Tuple2<Integer, Integer>(v1._1() + v2, v1._2() + 1);
            }
        };
        Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> function21 = new Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> v1, Tuple2<Integer, Integer> v2) throws Exception {
                return new Tuple2<Integer, Integer>(v1._1() + v2._1(), v2._1() + v2._2());

            }
        };



        parallelizeRDD.combineByKey(function, function2,function21 ).mapValues(new Function<Tuple2<Integer,Integer>, Object>() {
            @Override
            public Float call(Tuple2<Integer, Integer> v1) throws Exception {
                return Float.valueOf(v1._1())/Float.valueOf(v1._2());
            }
        }).foreach(new VoidFunction<Tuple2<String, Object>>() {
            @Override
            public void call(Tuple2<String, Object> t) throws Exception {
                System.out.println(t._1()+"="+t._2());
            }
        });





}

}
