package com.xxy.mapreducer.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        conf.addResource("hadoop/core-site.xml");
        conf.addResource("hadoop/hdfs-site.xml");
        System.setProperty("HADOOP_USER_NAME", "root");



        Job job = Job.getInstance(conf);
        job.setJobName("FlowSum");

        FileInputFormat.addInputPath(job,new Path("hdfs://node01:9000/tmp/flow/datafile/flow.log"));
        FileOutputFormat.setOutputPath(job,new Path("hdfs://node01:9000/tmp/flow/out_sum"));


        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(FlowBean.class);

        // 执行作业
        System.exit(job.waitForCompletion(true) ? 0 : 8);
    }
}
