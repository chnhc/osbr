package com.chu163.jieba.analysis;


import com.chu163.jieba.utils.PhoneModel;
import com.chu163.jieba.utils.PhoneUtil;
import com.huaban.analysis.jieba.word.JiebaSegmenter;
import com.huaban.analysis.jieba.word.Word;
import com.huaban.analysis.jieba.word.WordDictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.chu163.jieba.utils.FileUtils.getDictAbsolutePath;

public class PositionAnalyzer {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};

    private String pFile = getDictAbsolutePath("p.dict");//"D:/workspace/jiebafile/p.dict";
    private String cFile = getDictAbsolutePath("c.dict");//"D:/workspace/jiebafile/c.dict";
    private String rFile = getDictAbsolutePath("xian.dict");//"D:/workspace/jiebafile/xian.dict";

    // 省份 解析
    private JiebaSegmenter segmenterP;

    // 城市 解析
    private JiebaSegmenter segmenterPC;

    // 区域
    private JiebaSegmenter segmenterXian;

    public PositionAnalyzer() throws FileNotFoundException {

        // 加载省份
        WordDictionary dictionaryP = new WordDictionary();
        dictionaryP.loadUserDict(new File(pFile));
        this.segmenterP = new JiebaSegmenter(dictionaryP);

        // 先加载 城市 pc.dict
        WordDictionary dictionaryC = new WordDictionary();
        dictionaryC.loadUserDict(new File(cFile));
        this.segmenterPC = new JiebaSegmenter(dictionaryC);

        // 再加载 区域 xian.dict
        WordDictionary dictionaryXian = new WordDictionary();
        dictionaryXian.loadUserDict(new File(rFile));
        this.segmenterXian = new JiebaSegmenter(dictionaryXian);

    }


    public String analyzer(String text, String phone) {
        String line = text;
        // 去除干扰字符
        for (String f : fu) {
            line = line.replace(f, "");
        }
        // 先获取省份，如果有那就行这个省的
        String p = getProvince(line);
        if(p == null){
            PhoneModel phoneModel = PhoneUtil.getPhoneModel(phone);
            if(phoneModel != null && phoneModel.getProvinceName().length()>0){
                p = phoneModel.getProvinceName();
            }
        }
        // 再获取城市
        String c = getCity(line);
        // 注意， 区域可能存在多个
        String r = getRegion(line);

        // 如果区域是这个省份的，则ok
        if (r != null && p != null && r.contains(p)) {
            return r;
        }else if (c != null && p != null && c.contains(p)) {
            return c;
        }else if (r != null && p == null) {
            return r;
        } else if((c != null || r != null)&& p == null){
            return p;
        }  else if (r == null && c != null) {
            return c;
        } else if (p != null) {
            return p;
        } else {
            return "暴力查询";
        }

    }


    // 获取省份
    private String getProvince(String line) {
        List<Word> wordp = segmenterP.sentenceProcess(line);
        for (Word w : wordp) {
            if ("1".equals(w.getTokenType())) {
                return w.getToken();
                //System.out.println("P ---- "+ w.getToken() + " - " + w.getTokenType());
            }
        }
        return null;
    }

    // 获取城市
    private String getCity(String line) {
        List<Word> words = segmenterPC.sentenceProcess(line);
        for (Word w : words) {
            if (w.getTokenType().length() > 1) {
                return w.getTokenType().split(",")[1];
            }
        }
        return null;
    }

    // 获取区域
    private String getRegion(String line) {
        List<Word> wordss = segmenterXian.sentenceProcess(line);
        for (Word w : wordss) {
            if (w.getTokenType().length() > 1) {
                return w.getTokenType().split(",")[1];
            }
        }
        return null;
    }


}
