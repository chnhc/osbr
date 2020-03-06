package com.chu163.jieba.analysis;

import com.huaban.analysis.jieba.noWord.JiebaSegmenter;
import com.huaban.analysis.jieba.noWord.WordDictionary;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fenci {

    public static void main(String[] args) {
        test1();
    }


    public static void test3() {
        try {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("F:/bigData/xing.dict");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                for(String s: str.split("    ")){
                    if(!s.isEmpty()){
                        sb.append(s+" 1 n" + "\r\n");
                        sb.append(s+"主管 1 n" + "\r\n");
                        sb.append(s+"总 1 n" + "\r\n");
                        sb.append(s+"师傅 1 n" + "\r\n");
                        sb.append(s+"师父 1 n" + "\r\n");
                        sb.append(s+"哥 1 n" + "\r\n");
                    }
                }
            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("F:/bigData/xing1.dict");
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


    public static void test2() {
        //String path = "F:/bigData/jieba.dict";
        String path1 = "D:/workspace/jiebafile/jieba_xing.dict";
        //loadDict(path);
        loadDict(path1);
        JiebaSegmenter segmenter = new JiebaSegmenter();

        String line = "沙田镇月休四天8号入场";
        //String line = "拱北关口附近招请打荷水台各1名有意者请电13824140400林师父谢谢平台群里不回";

        System.out.println(segmenter.sentenceProcess(line.replace("，", "")));

    }


    public static void test1() {
        // 词典路径为Resource/dicts/jieba.dict
        Path path = Paths.get("D:/workspace/jiebafile/xian.dict");
        //WordDictionary.getInstance().loadUserDict(path);


        //WordDictionary.getInstance().init(path);

        JiebaSegmenter segmenter = new JiebaSegmenter();

        String line = "南安中骏世界城招一名新手炒锅3500-4500，新手粘4000到5000，要求不高。月休3天，";
      /*  String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }*/

        System.out.println(segmenter.sentenceProcess(line));
    }

    public static void loadDict(String path) {
        WordDictionary.getInstance().loadUserDict(Paths.get(path));
    }

}
