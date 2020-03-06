package com.chu163.jieba.test;

import com.qianxinyao.analysis.jieba.keyword.Keyword;
import com.qianxinyao.analysis.jieba.keyword.TFIDFAnalyzer;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    // https://github.com/huaban/jieba-analysis
    public static void main(String[] args) throws FileNotFoundException {
        test10();
    }

    public static void test10() throws FileNotFoundException {
        ResourceUtils.getFile("classpath:chu/dict/jieba_salary.dict").getAbsolutePath();
    }


    public static void test2() {
        String content = "孩子上了幼儿园，安全防拐教育要做好";
        int topN = 10;
        TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
        List<Keyword> list = tfidfAnalyzer.analyze(content, topN);
        for (Keyword word : list)
            System.out.println(word.getName() + ":" + word.getTfidfvalue() + ",");
        // 防拐:0.1992,幼儿园:0.1434,做好:0.1065,教育:0.0946,安全:0.0924
    }

    public static void test3() {
        try {
            // read file content from file 1.9646366
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("D:/workspace/jiebafile/xing1.dict");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                for (String s : str.split("    ")) {
                    if (!s.isEmpty()) {
                        sb.append(s + " 1000" + "\r\n");
                      /*  sb.append(s + "主管 1 n" + "\r\n");
                        sb.append(s + "总 1 n" + "\r\n");
                        sb.append(s + "师傅 1 n" + "\r\n");
                        sb.append(s + "师父 1 n" + "\r\n");
                        sb.append(s + "哥 1 n" + "\r\n");*/
                    }
                }
            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("D:/workspace/jiebafile/idf_xing.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void salary() {
        try {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("D:/workspace/jiebafile/salary1.dict");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                sb.append(str.split(" ")[0] + "\r\n");
               /* for (String s : str.split(" ")) {
                    if (!s.isEmpty()) {
                        sb.append(s + " 1 n" + "\r\n");
                      *//*  sb.append(s + "主管 1 n" + "\r\n");
                        sb.append(s + "总 1 n" + "\r\n");
                        sb.append(s + "师傅 1 n" + "\r\n");
                        sb.append(s + "师父 1 n" + "\r\n");
                        sb.append(s + "哥 1 n" + "\r\n");*//*
                    }
                }*/
            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("D:/workspace/jiebafile/salary11.dict");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void salary1() {
        try {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("D:/workspace/jiebafile/xing.dict");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                for (String s : str.split("    ")) {
                    if (!s.isEmpty()) {
                        sb.append(s  + "\r\n");
                      //  sb.append(s + " 1000" + "\r\n");
                        sb.append(s + "主管" + "\r\n");
                        sb.append(s + "总" + "\r\n");
                        sb.append(s + "师傅" + "\r\n");
                        sb.append(s + "师父" + "\r\n");
                        sb.append(s + "哥" + "\r\n");
                        sb.append(s + "店" + "\r\n");
                    }
                }
            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("D:/workspace/jiebafile/xing1.dict");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<String> getAllJob(){
        try {
            List<String> list = new ArrayList<>();


            FileReader reader = new FileReader("D:/workspace/jiebafile/num1.dict");
            BufferedReader br = new BufferedReader(reader);
            String str = null;

            while ((str = br.readLine()) != null) {
               /* for (String s : str.split("    ")) {
                    if (!s.isEmpty()) {

                    }
                }*/
                list.add(str.split(" ")[0]);

            }

            br.close();
            reader.close();

            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAllSalary(){
        try {
            List<String> list = new ArrayList<>();


            FileReader reader = new FileReader("D:/workspace/jiebafile/salary1.dict");
            BufferedReader br = new BufferedReader(reader);
            String str = null;

            while ((str = br.readLine()) != null) {
                for (String s : str.split("    ")) {
                    if (!s.isEmpty()) {
                        list.add(s);
                    }
                }
            }

            br.close();
            reader.close();

            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void test4() {
        try {
            //List allJob = getAllJob();
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("D:/workspace/jiebafile/cx.txt");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                /*if(!allJob.contains(str.split(" ")[0])){
                    sb.append(str+ "\r\n");
                }*/
                String[] s = str.split(" ");
                //if(s[0].length()>1){
                    sb.append(s[0]+" 1 "+s[1]+"\r\n");
                //}

            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("D:/workspace/jiebafile/cx.dict");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 获取字符在字符串的所有位置
    public static void test5(){
        String s = "ashhx123asjkdakjsdkpo123oxocakm1m23kzxckzxm123klzxkcv";
        List list = new ArrayList();
        String check = "123";
        int index = 0;
        int ax = 0;
        while ((ax = s.indexOf(check , index)) != -1){
            //System.out.println(ax);
            index = ax + check.length();
            list.add(ax);
        }

        Integer[] a = (Integer[]) list.toArray(new Integer[list.size()]);

        for(int a1 : a){
            System.out.println(a1);
        }
    }

    // 获取字符在字符串的所有位置
    public static Integer[] getAllDistances(String text , String check){
        // 存储 位置
        List list = new ArrayList();
        // 如果有多个位置，下次查找开始位置
        int index = 0;
        // 位置
        int position;
        while ((position = text.indexOf(check , index)) != -1){
            // 下次查找开始位置 ， 需要加上字符的长度大小
            index = position + check.length();
            // 位置保存
            list.add(position);
        }
        // 将list转换数组
        return  (Integer[]) list.toArray(new Integer[list.size()]);
    }


    public static void test6(){

        Integer[] i1 = new Integer[]{8,15,20};
        Integer[] i2 = new Integer[]{6,12,18};
        Map<String ,Integer[]> mapDistance = new HashMap<>();
        mapDistance.put("i1", i1);
        mapDistance.put("i2", i2);

        System.out.println(CalculatedDistance(10, mapDistance));
    }


    // 计算距离 ---  判断当前位置于map数组中的哪个近
    //  currentPosition  10
    // mapDistance [ 8,15,20]  , [ 6,12,18]
    //
    public static String CalculatedDistance(int currentPosition, Map<String ,Integer[]> mapDistance){
        // 存储  对应字符串 -- 最小距离
        Map<String ,Integer> distance = new HashMap<>();
        for(Map.Entry<String ,Integer[]> entry : mapDistance.entrySet()){
            // 查找一个的距离，并存到distance中
            for(int i = 0 , len = entry.getValue().length; i < len ; i++ ){
                // 如果只有一个元素，则不需要拦截
                // 如果 i 到了最后一个元素，则没必要继续判断
            /*if(i != 0 && i == len-1){
                return;
            }*/
                // 如果第一个 i=0 的时候，只需要判断 i+1 即可
                if(i == 0 ){
                    if(entry.getValue()[i] > currentPosition){
                        distance.put(entry.getKey(),entry.getValue()[i]);
                        break;
                    }
                }else{
                    // 如果当前位置为10 ， 数组为[ 8,15,20]
                    // 只要 [i-1] < 10  && [i] > 10  成立
                    // 那么 [i] 就是需要的值
                    if(entry.getValue()[i-1] < currentPosition && entry.getValue()[i] > currentPosition){
                        distance.put(entry.getKey(),entry.getValue()[i]);
                        break;
                    }
                }
            }
        }
        // 最小值所在的字符串，用于返回
        String minString  = null;
        // 记录最小距离, 默认为整数最大值
        int minDistance = Integer.MAX_VALUE;
        // 判断对应字符串，与记录最小距离比较
        for(Map.Entry<String ,Integer> entry : distance.entrySet()){
            if(entry.getValue() < minDistance){
                minString = entry.getKey();
                minDistance = entry.getValue();
            }
        }
        return minString;
    }


}
