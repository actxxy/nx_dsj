package com.xxy.mapreducer.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowJobControlMR {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.addResource("hadoop/core-site.xml");
        conf.addResource("hadoop/hdfs-site.xml");
        System.setProperty("HADOOP_USER_NAME", "root");

        Job sumJob = Job.getInstance(conf);
        sumJob.setJobName("FlowSum");

        FileInputFormat.addInputPath(sumJob,new Path("hdfs://node01:9000/tmp/flow/datafile/flow.log"));

        Path outputPath = new Path("hdfs://node01:9000/tmp/flow/out_sum");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileOutputFormat.setOutputPath(sumJob,outputPath);


        sumJob.setMapperClass(FlowMapper.class);
        sumJob.setReducerClass(FlowReducer.class);

        sumJob.setMapOutputKeyClass(Text.class);
        sumJob.setMapOutputValueClass(FlowBean.class);

        sumJob.setOutputKeyClass(NullWritable.class);
        sumJob.setOutputValueClass(FlowBean.class);

        Job sortJob = Job.getInstance(conf);
        sortJob.setJobName("FlowSort");

        FileInputFormat.addInputPath(sortJob,new Path("hdfs://node01:9000/tmp/flow/out_sum"));

        Path outputPath2 = new Path("hdfs://node01:9000/tmp/flow/out_sum_sort");

        if (fs.exists(outputPath2)) {
            fs.delete(outputPath2, true);
        }

        FileOutputFormat.setOutputPath(sortJob,outputPath2);


        sortJob.setMapperClass(FlowSortApp.FlowSortMapper.class);
        sortJob.setReducerClass(FlowSortApp.FLowSortReducer.class);

        sortJob.setMapOutputKeyClass(FlowBean.class);
        sortJob.setMapOutputValueClass(NullWritable.class);

        sortJob.setNumReduceTasks(1);

        // 局部有序
//        job.setNumReduceTasks(2);

        sortJob.setOutputKeyClass(NullWritable.class);
        sortJob.setOutputValueClass(FlowBean.class);

        ControlledJob cjsum = new ControlledJob(sumJob.getConfiguration());
        cjsum.setJob(sumJob);
        ControlledJob cjsort = new ControlledJob(sortJob.getConfiguration());
        cjsort.setJob(sortJob);


        JobControl jc = new JobControl("sum and sort flow");
        jc.addJob(cjsum);
        jc.addJob(cjsort);
        cjsort.addDependingJob(cjsum);

        Thread t = new Thread(jc);
        t.start();

        while (!jc.allFinished()) {
            Thread.sleep(1000);
        }

        jc.stop();


        // 执行作业
//        System.exit(sortJob.waitForCompletion(true) ? 0 : 8);
    }
}
