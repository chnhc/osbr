package com.kkIntegration.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * description: 文件操作
 * author: ckk
 * create: 2020-03-05 09:28
 */
public class FileUtils {


    /**
    * 实现功能描述：写入数据
    */
    public static void writeFile(String path, String txt, boolean append){
        // 获取文件
        File file=new File(path);
        // 创建文件
        if(!file.exists()){
            // 创建父文件
           file.getParentFile().mkdir();
            try {
                // 创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 写文件
        BufferedWriter writer=null;
        try {
            // 获取写缓存
            writer=new BufferedWriter(new FileWriter(path,append));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            if(writer != null){
                // 写入文件
                writer.append(txt);
                //换行
                writer.newLine();
                //需要及时清掉流的缓冲区，万一文件过大就有可能无法写入了
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 最后关闭流
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] arg){
        writeFile("f:\\test1.txt", "aa111", false);
    }

}
