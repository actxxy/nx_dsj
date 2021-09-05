package com.xxy.mapreducer.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text,Text, LongWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println(value);
        String[] split = value.toString().split(" ");
        for (String s : split) {
            System.out.println(s);
            context.write(new Text(s),new LongWritable(1));
        }
    }
}
