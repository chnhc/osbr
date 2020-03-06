package com.kkIntegration.bigData.hadoop;

import com.kkIntegration.bigData.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * description: 温度统计 6 二次排序
 * author: ckk
 * create: 2020-01-20 11:03
 */
public class TestMR6_SecondSort {

        /*

        思路 ：
            1、 自定义key -- 结合 自定义key排序对比器 实现 --  Reducer中 key的排序

            2、 自定义分区类，按照年份分区 -- 从Mapper 分到同一个 Reducer

            3、 自定义分组对比器 -- 在Reducer中分配到同一个组，迭代

            4、 自定义key排序对比器
     */

    // 启动作业
    public static void main(String[] args) throws Exception {
        // 配置
        Configuration configuration = new Configuration();
        // 初始化 作业 job
        Job job = Job.getInstance(configuration);
        // 设置job 作业名
        job.setJobName("WCAPP");
        // 搜索类
        job.setJarByClass(TestMR6_SecondSort.class);
        // 设置输入格式
        job.setInputFormatClass(TextInputFormat.class);

        // 设置Mapper类
        job.setMapperClass(WCMapper.class);
        // 设置Reducer类
        job.setReducerClass(WCReducer.class);
        // 设置Reducer任务个数
        job.setNumReduceTasks(3);
        // 设置Reducer输出Key
        job.setOutputKeyClass(IntWritable.class);
        // 设置Reducer输出value
        job.setOutputValueClass(IntWritable.class);


        // 设置分区类
        job.setPartitionerClass(YearPartitioner.class);
        // 设置分组对比器
        job.setGroupingComparatorClass(YearGroupComparator.class);
        // 设置key的排序对比器
        job.setSortComparatorClass(ComboKeyComparator.class);


        // 设置输入路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        // 设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 等待
        job.waitForCompletion(false);

    }


    /**
     * mapper
     * LongWritable 单行文本偏移量
     * Text         单行文本
     * <p>
     * Text         输出的 key
     * IntWritable  输出的 value
     */
    public static class WCMapper extends Mapper<LongWritable, Text, ComboKey, NullWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 空格切分 统计单词
            String[] arr = value.toString().split(" ");
            ComboKey comboKey = new ComboKey();
            comboKey.setYear(Integer.parseInt(arr[0]));
            comboKey.setYear(Integer.parseInt(arr[1]));
            context.write(comboKey, NullWritable.get());
        }

    }

    /**
     * reducer
     * Text         mapper端输出的key
     * IntWritable  mapper端输出的value
     * <p>
     * Text         输出的 key
     * IntWritable  输出的 value
     */
    public static class WCReducer extends Reducer<ComboKey, NullWritable, IntWritable, IntWritable> {

        @Override
        protected void reduce(ComboKey key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(new IntWritable(key.getYear()), new IntWritable(key.getTemp()));
        }
    }


    // 1、 自定义组合 key
    public static class ComboKey implements WritableComparable<ComboKey> {

        private int year;

        private int temp;

        @Override
        public int compareTo(ComboKey o) {
            int y0 = o.year;
            int t0 = o.temp;
            // 年份相同(升序)
            if (year == y0) {
                // 气温 降序
                return -(temp - t0);
            } else {
                return year - y0;
            }
        }

        // 串行
        @Override
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeInt(year);
            dataOutput.writeInt(temp);
        }

        // 反串行
        @Override
        public void readFields(DataInput dataInput) throws IOException {
            year = dataInput.readInt();
            temp = dataInput.readInt();
        }


        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
    }


    // 2、 自定义分区类，按照年份分区
    public static class YearPartitioner extends Partitioner<ComboKey, NullWritable> {

        @Override
        public int getPartition(ComboKey comboKey, NullWritable nullWritable, int i) {

            int year = comboKey.getYear();

            return year % i;
        }
    }


    // 3、 自定义分组对比器
    public static class YearGroupComparator extends WritableComparator{

        public YearGroupComparator() {
            super(ComboKey.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            ComboKey k1 = (ComboKey) a;
            ComboKey k2 = (ComboKey) b;
            return k1.getYear() - k2.getYear();
        }

    }


    // 4、 自定义key排序对比器
    public static class ComboKeyComparator extends WritableComparator{

        public ComboKeyComparator() {
            super(ComboKey.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            ComboKey k1 = (ComboKey) a;
            ComboKey k2 = (ComboKey) b;
            return k1.compareTo(k2);
        }

    }

}
