package com.kkIntegration.bigData.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * description: 测试压缩
 * author: ckk
 * create: 2020-01-20 13:34
 */
public class TestZip1 {

    public static void main(String[] args) throws Exception {
        Class[] zipClass = {
                DefaultCodec.class,
                GzipCodec.class,
                BZip2Codec.class,
                Lz4Codec.class
        } ;

        for(Class c : zipClass){
            zip(c);
        }
    }

    // 压缩
    public static void zip(Class clazz) throws Exception {
        long start = System.currentTimeMillis();
        // 实例化对象
        CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(clazz, new Configuration());
        // 创建文件输出流，得到文件扩展名
        FileOutputStream fos = new FileOutputStream("F:/test"+compressionCodec.getDefaultExtension());
        // 得到压缩流
        CompressionOutputStream zipOut = compressionCodec.createOutputStream(fos);
        // 压缩文件
        IOUtils.copyBytes(new FileInputStream("F:/test.txt"), zipOut , 1024);

        zipOut.close();
        System.out.println(clazz.getSimpleName() + " : " + (System.currentTimeMillis() - start));
    }

    // 解压缩
    public static void unzip(Class clazz)throws Exception{
        long start = System.currentTimeMillis();
        // 实例化对象
        CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(clazz, new Configuration());
        // 创建文件输入流
        FileInputStream fis  = new FileInputStream("F:/test"+compressionCodec.getDefaultExtension());
        // 得到压缩流
        CompressionInputStream zipOut = compressionCodec.createInputStream(fis);

        IOUtils.copyBytes(zipOut, new FileOutputStream("F:/test"+compressionCodec.getDefaultExtension()+".txt"), 1024);

        zipOut.close();
        System.out.println(clazz.getSimpleName() + " : " + (System.currentTimeMillis() - start));
    }

}
