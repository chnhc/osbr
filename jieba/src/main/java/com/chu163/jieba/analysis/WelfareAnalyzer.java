package com.chu163.jieba.analysis;

import com.huaban.analysis.jieba.word.JiebaSegmenter;
import com.huaban.analysis.jieba.word.Word;
import com.huaban.analysis.jieba.word.WordDictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.chu163.jieba.utils.FileUtils.getDictAbsolutePath;

public class WelfareAnalyzer {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};

    private String wFile = getDictAbsolutePath("w.dict");//"D:/workspace/jiebafile/w.dict";


    // 福利 解析
    private JiebaSegmenter segmenterW;


    public WelfareAnalyzer() throws FileNotFoundException {

        // 加载福利
        WordDictionary dictionaryW = new WordDictionary();
        dictionaryW.loadUserDict(new File(wFile));
        this.segmenterW = new JiebaSegmenter(dictionaryW);

    }


    public String analyzer(String text) {
        String line = text;
        // 去除干扰字符
        for (String f : fu) {
            line = line.replace(f, "");
        }
        // 获取福利
        String welfare = getWelfare(line);
        if(welfare.isEmpty()){
            welfare = "福利面谈";
        }
        return  welfare;
    }


    // 获取福利
    private String getWelfare(String line) {
        List<Word> wordp = segmenterW.sentenceProcess(line);
        StringBuffer sb = new StringBuffer();
        for (Word w : wordp) {
            if (w.getTokenType().length()>1) {
                sb.append(w.getToken()+",");
                //return w.getToken();
                //System.out.println("W ---- "+ w.getToken() + " - " + w.getTokenType());
            }
        }
        return sb.toString();
    }



}
