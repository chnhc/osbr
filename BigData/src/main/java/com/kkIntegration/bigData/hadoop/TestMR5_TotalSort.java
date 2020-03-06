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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

import java.io.IOException;
import java.io.SequenceInputStream;

/**
 * description: 单词统计 5 全排序
 * author: ckk
 * create: 2020-01-20 11:03
 */
public class TestMR5_TotalSort {

    /*
    思路 ：
        1、 只使用一个Reducer

        2、 对分区函数进行重新编写， 人为设置分区界限

        3、使用采样器实现：





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
        job.setJarByClass(TestMR1.class);
        // 设置输入格式
        //job.setInputFormatClass(TextInputFormat.class);
        // 注意使用 序列化文件
        job.setInputFormatClass(SequenceFileInputFormat.class);

        int reducer_num = 3;

        // 设置Mapper类
        job.setMapperClass(WCMapper.class);
        // 设置Reducer类
        job.setReducerClass(WCReducer.class);
        // 设置Reducer任务个数
        job.setNumReduceTasks(reducer_num);
        // 设置Reducer输出Key
        job.setOutputKeyClass(Text.class);
        // 设置Reducer输出value
        job.setOutputValueClass(IntWritable.class);

        // 设置输入路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        // 设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));




        // 设置全排序分区类   -- 配合采样器
        job.setPartitionerClass(TotalOrderPartitioner.class);

        // 设置随机采样器     -- 配合全排序分区类
        InputSampler.Sampler<IntWritable, IntWritable> sampler =
                new InputSampler.RandomSampler<IntWritable, IntWritable>(
                        0.1,                // 随机选中的概率
                        10000 ,      // 样本数据量
                        reducer_num       // 划分分区数量
                );
        // 数据写入分区文件
        // 注意 ： job.getConfiguration() 获取作业中的配置
        TotalOrderPartitioner.setPartitionFile(job.getConfiguration(), new Path(""));
        // 写入分区文件
        InputSampler.writePartitionFile(job, sampler);






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
    public static class WCMapper extends Mapper<IntWritable, IntWritable, IntWritable, IntWritable>{

        @Override
        protected void map(IntWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
           context.write(key, value);
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
    public static class WCReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int max = Integer.MAX_VALUE;
            // 获取最大值
            for(IntWritable i : values){
                max = Math.max(max, i.get());
            }
            context.write(key, new IntWritable(max));
        }
    }






}
