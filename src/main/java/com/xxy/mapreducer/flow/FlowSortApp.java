package com.xxy.mapreducer.flow;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


public class FlowSortApp {

    public static class FlowSortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = StringUtils.split(value.toString(), "\t");
            String phone = split[0];
            Long upFlow = Long.parseLong(split[1]);
            Long downFlow = Long.parseLong(split[2]);
            context.write(new FlowBean(phone, upFlow, downFlow), NullWritable.get());
        }
    }

    public static class FLowSortReducer extends Reducer<FlowBean, NullWritable, NullWritable, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

            for (NullWritable value : values) {
                context.write(NullWritable.get(), key);
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.addResource("hadoop/core-site.xml");
        conf.addResource("hadoop/hdfs-site.xml");
        System.setProperty("HADOOP_USER_NAME", "root");



        Job job = Job.getInstance(conf);
        job.setJobName("FlowSort");

        FileInputFormat.addInputPath(job,new Path("hdfs://node01:9000/tmp/flow/out_sum"));

        Path outputPath = new Path("hdfs://node01:9000/tmp/flow/out_sum_sort");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileOutputFormat.setOutputPath(job,outputPath);


        job.setMapperClass(FlowSortMapper.class);
        job.setReducerClass(FLowSortReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(1);

        // 局部有序
//        job.setNumReduceTasks(2);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(FlowBean.class);

        // 执行作业
        System.exit(job.waitForCompletion(true) ? 0 : 8);
    }
}
