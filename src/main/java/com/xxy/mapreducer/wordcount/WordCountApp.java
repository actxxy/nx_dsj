package com.xxy.mapreducer.wordcount;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

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
        FileOutputFormat.setOutputPath(job,new Path("hdfs://node01:9000/tmp/wordcount/result"));

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setCombinerClass(WordCountCombiner.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 执行作业
        System.exit(job.waitForCompletion(true) ? 0 : 8);
//        job.waitForCompletion(true);
    }
}
