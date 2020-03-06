package com.kkIntegration.bigData.hadoop;

import com.kkIntegration.bigData.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * description: 单词统计 1
 * author: ckk
 * create: 2020-01-20 11:03
 */
public class TestMR1 {

    // 启动作业
    public static void main(String[] args) throws Exception {
        // 配置
        Configuration configuration = new Configuration();
        // 初始化 作业 job
        Job job = Job.getInstance(configuration);
        // 设置job 作业名
        job.setJobName("WCAPP");
        // 搜索类
        job.setJarByClass(TestMR1.class);
        // 设置输入格式
        job.setInputFormatClass(TextInputFormat.class);

        // 设置Mapper类
        job.setMapperClass(WCMapper.class);
        // 设置Reducer类
        job.setReducerClass(WCReducer.class);
        // 设置Reducer任务个数
        job.setNumReduceTasks(3);
        // 设置Reducer输出Key
        job.setOutputKeyClass(Text.class);
        // 设置Reducer输出value
        job.setOutputValueClass(IntWritable.class);


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
     *
     * Text         输出的 key
     * IntWritable  输出的 value
     */
    public static class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 输出 key
            Text keyOut = new Text();
            // 输出 value
            IntWritable valueOut = new IntWritable();
            // 空格切分 统计单词
            String[] arr = value.toString().split(" ");
            for(String w: arr){
                // 设置key
                keyOut.set(w);
                // 设置单词个数
                valueOut.set(1);
                // 输出
                context.write(keyOut, valueOut);
                // 计数器设置
                context.getCounter("m", Utils.getInfo(this)).increment(1);
            }
        }

    }

    /**
     * reducer
     * Text         mapper端输出的key
     * IntWritable  mapper端输出的value
     *
     * Text         输出的 key
     * IntWritable  输出的 value
     */
    public static class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            // 单个单词
            int count = 0;
            // 循环统计个数
            for (IntWritable writable : values) {
                count += writable.get();
            }
            context.write(key, new IntWritable(count));
            // 计数器设置
            context.getCounter("r", Utils.getInfo(this)).increment(1);
        }
    }



}
