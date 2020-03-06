package com.kkIntegration.bigData.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * description:
 * author: ckk
 * create: 2020-01-19 16:51
 */
public class TestReadWrite1 {


    public static void main (String[] arg )throws Exception{
        testRead();

        //testWrite1();
    }

    // 读取文件
    public static void testRead() throws Exception {
        // 获取配置
        Configuration configuration = new Configuration();
        // 文件系统
        FileSystem fs = FileSystem.get(configuration);
        // 获取文件系统中的文件路径
        Path path = new Path("hdfs://s201:9000/usr/home/wc1.txt");
        // 获取文件流
        FSDataInputStream fsDataInputStream = fs.open(path);
        // 初始化输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 拷贝文件到输出流
        IOUtils.copyBytes(fsDataInputStream, baos, 1024);
        // 关闭输入流
        fsDataInputStream.close();
        // 打印
        System.out.println("MyData ---- " + new String(baos.toByteArray()));
    }

    // 写入文件
    public static void testWrite() throws Exception {
        // 获取配置
        Configuration configuration = new Configuration();
        // 文件系统
        FileSystem fs = FileSystem.get(configuration);
        // 写入到文件系统的文件
        Path path = new Path("hdfs://s201:9000/usr/home/hello1.txt");
        // 获取输出流
        FSDataOutputStream fos = fs.create(path);
        // 数据写入输出流
        fos.write("window10 write data to HDFS!".getBytes());
        // 关闭输出流
        fos.close();
    }

    /*
     指定副本数和blockSize
        hdfs-size.xml 配置
        必须是 512 倍数 ，最小值 ： 512
        <property>
            <name>dfs.namenode.fs-limits.min-block-size</name>
            <value>1024</value>
        </property>
     */
    public static void testWrite1() throws Exception {
        // 获取配置
        Configuration configuration = new Configuration();
        // 文件系统
        FileSystem fs = FileSystem.get(configuration);
        // 写入到文件系统的文件
        Path path = new Path("hdfs://s201:9000/usr/home/wc1.txt");
        // 获取输出流
        FSDataOutputStream fos = fs.create(
                path,
                true,
                1024,
                (short) 2,
                1024);
        // 数据写入输出流
        fos.write("window10 write data to HDFS!\n thank you my student\nyes no three student value".getBytes());
        // 关闭输出流
        fos.close();
    }



}
