package com.xxy.mapreducer.flow;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean, NullWritable, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        Long upFlowCount = 0L;
        Long downFlowCount = 0L;
        for (FlowBean value : values) {
            upFlowCount += value.getUpFlow();
            downFlowCount += value.getDownFlow();
        }
        context.write(NullWritable.get(), new FlowBean(key.toString(), upFlowCount, downFlowCount));
    }
}
