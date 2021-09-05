package com.xxy.mapreducer.flow;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    Text k = new Text();
    FlowBean v = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = StringUtils.split(value.toString());
        // 抽取业务所需要的各字段
        String phone = split[1];
        Long upFlow = Long.parseLong(split[split.length - 3]);
        Long downFlow = Long.parseLong(split[split.length - 2]);
        k.set(phone);
        v.set(phone, upFlow, downFlow);
        context.write(k,v);
    }
}
