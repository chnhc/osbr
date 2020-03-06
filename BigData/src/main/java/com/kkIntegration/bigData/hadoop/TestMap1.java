package com.kkIntegration.bigData.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

/**
 * description: Map 文件操作
 * author: ckk
 * create: 2020-01-20 14:48
 */
public class TestMap1 {

    public static void main(String[] args) throws Exception {
        read1();
    }

    // 写操作
    public static void write1() throws Exception {
        // 初始化配置
        Configuration configuration = new Configuration();
        // 配置本地系统
        configuration.set("fs.defaultFS", "file:///");
        // 初始化文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        // Map写
        MapFile.Writer writer = new MapFile.Writer(configuration, fileSystem, "f:/map", IntWritable.class, Text.class);
        // 写数据
        for(int i = 0 ; i< 100; i++ ){
            writer.append(new IntWritable(i),new Text("text"+i));
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
        // 读序列化文件
        MapFile.Reader reader = new MapFile.Reader(fileSystem, "f:/map", configuration);

        // 读取数据
        IntWritable key = new IntWritable();
        Text value = new Text();
        while (reader.next(key, value)){
            System.out.println(key.get() + " ：" +value.toString() );
        }
        // 关闭
        reader.close();
    }

}
