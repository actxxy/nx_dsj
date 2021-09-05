package com.xxy.mapreducer.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;
import java.util.Map;

/**
 * 分区器
 */
public class WordCountPartitioner extends Partitioner<Text, LongWritable> {

    private static Map<String, Integer> wordMap = new HashMap<>();

    static {
        wordMap.put("h", 0);
        wordMap.put("k", 1);
        wordMap.put("f", 2);
    }

    @Override
    public int getPartition(Text text, LongWritable longWritable, int numPartitions) {
        String preStr = text.toString().substring(0, 1);
        Integer integer = wordMap.get(preStr);
        if (integer != null) {
            return integer;
        } else {
            return 3;
        }
    }
}
