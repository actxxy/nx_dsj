package com.xxy.mapreducer.flow;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements WritableComparable<FlowBean> {

    // 序列化框架在反序列化操作创建对象实例时会调用无参构造，所以一定要有无参构造方法
    public FlowBean() {

    }
    private String phone;    // 手机号
    private Long upFlow;     // 上行流量
    private Long downFlow;   // 下行流量
    private Long sumFlow;    // 总流量

    public void set(String phone, Long upFlow, Long downFlow) {
        this.phone = phone;
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(Long upFlow) {
        this.upFlow = upFlow;
    }

    public Long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(Long downFlow) {
        this.downFlow = downFlow;
    }

    public Long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(Long sumFlow) {
        this.sumFlow = sumFlow;
    }



    public FlowBean(String phone, Long upFlow, Long downFlow) {
        this.phone = phone;
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    // 对象比较逻辑，排序规则，默认情况下，也是reducer组件中的分组逻辑
    @Override
    public int compareTo(FlowBean fb) {
        return (int) (fb.getSumFlow() - this.sumFlow);
    }

    // 序列化方法
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(phone);
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    // 反序列化方法
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.phone = dataInput.readUTF();
        this.upFlow = dataInput.readLong();
        this.downFlow = dataInput.readLong();
        this.sumFlow = dataInput.readLong();
    }

    @Override
    public String toString() {
        return phone + '\t' + upFlow + '\t' + downFlow + '\t'+  sumFlow;
    }
}