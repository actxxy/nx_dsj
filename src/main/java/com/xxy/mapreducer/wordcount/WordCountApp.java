package com.xxy.mapreducer.wordcount;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import java.io.IOException;

public class WordCountApp {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        Configuration conf = new Configuration();
        conf.addResource("hadoop/core-site.xml");
        conf.addResource("hadoop/hdfs-site.xml");
        System.setProperty("HADOOP_USER_NAME", "root");



        Job job = Job.getInstance(conf);
        job.setJobName("wordCountMapReducer");

        FileInputFormat.addInputPath(job,new Path("hdfs://node01:9000/tmp/wordcount/datafile/words"));

        Path outputPath = new Path("hdfs://node01:9000/tmp/wordcount/result");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileOutputFormat.setOutputPath(job, outputPath);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);


        // 为该mapreduce程序制定默认的数据分区组件。默认时HashPartitioner.class
//        job.setPartitionerClass(HashPartitioner.class);

        // 分区统计
        job.setNumReduceTasks(4);
        job.setPartitionerClass(WordCountPartitioner.class);


//        job.setNumReduceTasks(1);
        job.setCombinerClass(WordCountCombiner.class);



        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 执行作业
        System.exit(job.waitForCompletion(true) ? 0 : 8);
//        job.waitForCompletion(true);
    }
}
