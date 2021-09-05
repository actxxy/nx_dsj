package com.xxy.mapreducer.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 预合并，即在map端执行一次reduce操作
 */

public class WordCountCombiner extends Reducer<Text, LongWritable, Text, LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        Long sum = 0L;
        for (LongWritable value : values) {
            sum += Long.parseLong(value.toString());
        }
        context.write(key, new LongWritable(sum));
    }
}
