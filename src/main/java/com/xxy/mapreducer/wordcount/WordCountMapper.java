package com.xxy.mapreducer.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text,Text, LongWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(" ");
        // 全局计数器行数
        context.getCounter(GlobalCounter.CounterWordCountC.COUNT_LINES).increment(1);

        for (String s : split) {
            // 全局计算器 单词数
            context.getCounter(GlobalCounter.CounterWordCountC.COUNT_WORDS).increment(1);
            context.write(new Text(s),new LongWritable(1));
        }
    }
}
