package com.kkIntegration.bigData.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;

import java.io.IOException;

/**
 * description: 序列化文件操作
 * author: ckk
 * create: 2020-01-20 14:06
 */
public class TestSeq1 {


    public static void main(String[] args) throws Exception {
        read4();
    }

    // 写操作
    public static void write1() throws Exception {
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 写入文件路径
        Path p = new Path("f:/seq/1.seq");
        // 序列化写
        SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration, p, IntWritable.class, Text.class);
        // 写数据
        for(int i = 0 ; i< 100; i++ ){
            writer.append(new IntWritable(i),new Text("text"+i));
        }
        // 关闭
        writer.close();
    }

    // 写操作 -- 同步点
    public static void write2() throws Exception {
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 写入文件路径
        Path p = new Path("f:/seq/1.seq");
        // 序列化写
        SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration, p, IntWritable.class, Text.class);
        // 写数据
        for(int i = 0 ; i< 100; i++ ){
            writer.append(new IntWritable(i),new Text("text"+i));
            // 添加同步点
            writer.sync();
            // 可进行间隔性 同步点
        }
        // 关闭
        writer.close();
    }

    // 读数据
    public static void read1() throws Exception{
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 读取文件路径
        Path p = new Path("f:/seq/1.seq");
        // 读序列化文件
        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, p, configuration);

        // 读取数据
        IntWritable key = new IntWritable();
        Text value = new Text();
        while (reader.next(key, value)){
            System.out.println(key.get() + " ：" +value.toString() );
        }
        // 关闭
        reader.close();
    }

    // 读数据
    public static void read2() throws Exception{
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 读取文件路径
        Path p = new Path("f:/seq/1.seq");
        // 读序列化文件
        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, p, configuration);

        // 读取数据
        IntWritable key = new IntWritable();
        Text value = new Text();
        while (reader.next(key)){
            reader.getCurrentValue(value);
            System.out.println(key.get() + " ：" +value.toString() );
        }
        // 关闭
        reader.close();
    }

    // 读数据 -- 指针
    public static void read3() throws Exception{
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 读取文件路径
        Path p = new Path("f:/seq/1.seq");
        // 读序列化文件
        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, p, configuration);

        // 读取数据
        IntWritable key = new IntWritable();
        Text value = new Text();
        while (reader.next(key)){
            reader.getCurrentValue(value);
            // 文件值指针输出
            System.out.println(reader.getPosition() + " ：" +key.get() + " ：" +value.toString() );
        }
        // 关闭
        reader.close();
    }

    // 读数据 -- 同步点操作 -- 切割
    public static void read4() throws Exception{
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 读取文件路径
        Path p = new Path("f:/seq/1.seq");
        // 读序列化文件
        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, p, configuration);
        // 定位到特定的同步点
        //reader.seek(4563);
        // 模糊定位到同步点，最近的下个同步点
        reader.sync(4500);
        // 读取数据
        IntWritable key = new IntWritable();
        Text value = new Text();
        while (reader.next(key, value)){
            // 文件值指针输出
            System.out.println(reader.getPosition() + " ：" +key.get() + " ：" +value.toString() );
        }
        // 关闭
        reader.close();
    }

    // 压缩操作 record 压缩 -- 只压缩 value
    public static void write_RECORD() throws Exception {
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 写入文件路径
        Path p = new Path("f:/seq/1.seq");
        // 序列化写
        SequenceFile.Writer writer = SequenceFile.createWriter(
                fileSystem, configuration, p,
                IntWritable.class, Text.class,
                SequenceFile.CompressionType.RECORD,
                new GzipCodec());
        // 写数据
        for(int i = 0 ; i< 100; i++ ){
            writer.append(new IntWritable(i),new Text("text"+i));
        }
        // 关闭
        writer.close();
    }

    // 压缩操作 block 压缩 --  按照多个record形成一个block
    public static void write_BLOCK() throws Exception {
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // 写入文件路径
        Path p = new Path("f:/seq/1.seq");
        // 序列化写
        SequenceFile.Writer writer = SequenceFile.createWriter(
                fileSystem, configuration, p,
                IntWritable.class, Text.class,
                SequenceFile.CompressionType.BLOCK,
                new GzipCodec());
        // 写数据
        for(int i = 0 ; i< 100; i++ ){
            writer.append(new IntWritable(i),new Text("text"+i));
        }
        // 关闭
        writer.close();
    }


}
