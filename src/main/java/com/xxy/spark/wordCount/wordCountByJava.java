package com.xxy.spark.wordCount;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class wordCountByJava {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[*]");
        sparkConf.setAppName("WordCountByJava");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        // 读取文件
        JavaRDD<String> stringJavaRDD = javaSparkContext.textFile(args[0]);
        JavaRDD<String> stringJavaRDD1 = stringJavaRDD.flatMap(t -> Arrays.asList(t.split(" ")).iterator());
        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringJavaRDD1.mapToPair(t -> new Tuple2<>(t, 1));
        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD1 = stringIntegerJavaPairRDD.reduceByKey((a, b) -> a + b);
        List<Tuple2<String, Integer>> collect = stringIntegerJavaPairRDD1.collect();
        for (Tuple2<String, Integer> stringIntegerTuple2 : collect) {
            System.out.println(stringIntegerTuple2);
        }
        javaSparkContext.close();


//        // 压平
//        JavaRDD<String> stringJavaRDD1 = stringJavaRDD.flatMap(new FlatMapFunction<String, String>() {
//
//            @Override
//            public Iterator<String> call(String s) throws Exception {
//                String[] split = s.split(" ");
//                return Arrays.stream(split).iterator();
//            }
//        });
//        // 合并成元组
//        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringJavaRDD1.mapToPair(new PairFunction<String, String, Integer>() {
//
//            @Override
//            public Tuple2<String, Integer> call(String s) throws Exception {
//                return new Tuple2<>(s, 1);
//            }
//        });
//
//        // 计算reduce
//        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD1 = stringIntegerJavaPairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) throws Exception {
//                return integer + integer2;
//            }
//        });
//
//        // 输出结果
//        List<Tuple2<String, Integer>> collect = stringIntegerJavaPairRDD1.collect();
//
//        for (Tuple2<String, Integer> stringIntegerTuple2 : collect) {
//            System.out.print(stringIntegerTuple2._1);
//            System.out.print(" ");
//            System.out.print(stringIntegerTuple2._2);
//            System.out.println();
//        }

        javaSparkContext.close();


    }
}
