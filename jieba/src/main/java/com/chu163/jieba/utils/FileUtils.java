package com.chu163.jieba.utils;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 18:14
 */
public class FileUtils {

    // 获取 dict 绝对路径
    public static String getDictAbsolutePath(String dictFileName) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:chu/dict/"+dictFileName).getAbsolutePath();
    }

    // 获取 idf 绝对路径
    public static String getIDFAbsolutePath(String idfFileName) throws FileNotFoundException {
        return "/chu/idf/"+idfFileName;//ResourceUtils.getFile("classpath:chu/idf/"+idfFileName).getAbsolutePath();
    }


}
