package com.chu163.jieba.position;

import com.huaban.analysis.jieba.word.JiebaSegmenter;
import com.huaban.analysis.jieba.word.Word;
import com.huaban.analysis.jieba.word.WordDictionary;

import java.io.File;
import java.util.List;

public class Test {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};

    public static void main(String[] arg){

        // 广东省广州市  天河区
        // 广东省广州市  白云区

        // 广东省深圳市  罗湖区
        // 广东省深圳市  福田区

        /*

         type: 1-省份 ， 2-市区 ， 3-区域

            1、 省份   type,provinceCode  1,13

            2、 市区   type,provinceCode,cityCode  2,13,1302

            3、 区域   type,provinceCode,cityCode,regionCode   3,13,1302,130202


            -----------------------------
             处理数据

            1、省份 去除结尾 市 省 自治区 壮族自治区 回族自治区 维吾尔自治区

            2、市区 去除结尾 市 区 盟 地区 ，自治县、自治州结尾，删除自治县、自治州 分词，

            3、自治县、自治州结尾，删除自治县、自治州 分词， 完整市、区、地区，去除市、区、地区

            -----------------------------
             查询数据

            1、 直接匹配省份

            2、 匹配 城市 反查 省份

            3、 匹配 区域 反查 省份城市

         */

        // 词典路径为Resource/dicts/jieba.dict

        String line = "招聘 厦门岛内湖里，双十中学brt附近 打荷数名3200起 荷头一个 配菜一名 月休四天，医社保。正常班。工资准时发。 联系电话13950090742 石厨 厦门海沧招 客家菜炒锅5000到7500 2名 配菜 4000到5500 打荷数名3200起 月休4天，医社保，正常班，准时发工资， 联系电话15880626033江厨 说石师傅介绍的";
        // 去除干扰字符
        for (String f : fu) {
            line = line.replace(f, "");
        }
        // 加载省份
        WordDictionary dictionaryP = new WordDictionary();
        dictionaryP.loadUserDict(new File("D:/workspace/jiebafile/p.dict"));

        JiebaSegmenter segmenterP = new JiebaSegmenter(dictionaryP);



        List<Word> wordp = segmenterP.sentenceProcess(line);

        for (Word w : wordp) {
            if(w.getTokenType().length()>1){
                System.out.println("P ---- "+ w.getToken() + " - " + w.getTokenType());
            }
        }

        // 先加载 城市 pc.dict
        WordDictionary dictionaryC = new WordDictionary();
        dictionaryC.loadUserDict(new File("D:/workspace/jiebafile/pc.dict"));

        JiebaSegmenter segmenterPC = new JiebaSegmenter(dictionaryC);


        List<Word> words = segmenterPC.sentenceProcess(line);

        for (Word w : words) {
            if(w.getTokenType().length()>1){
                System.out.println("C ---- "+w.getToken() + " - " + w.getTokenType());
            }
        }


        // 再加载 区域 xian.dict
        WordDictionary dictionaryXian = new WordDictionary();
        dictionaryXian.loadUserDict(new File("D:/workspace/jiebafile/xian.dict"));
        JiebaSegmenter segmenterXian = new JiebaSegmenter(dictionaryXian);
        List<Word> wordss = segmenterXian.sentenceProcess(line);

        for (Word w : wordss) {
            if(w.getTokenType().length()>1){
                System.out.println("Q ---- "+w.getToken() + " - " + w.getTokenType());
            }
        }


    }


}
