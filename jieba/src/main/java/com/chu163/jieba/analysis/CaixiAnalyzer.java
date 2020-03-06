package com.chu163.jieba.analysis;

import com.huaban.analysis.jieba.word.JiebaSegmenter;
import com.huaban.analysis.jieba.word.Word;
import com.huaban.analysis.jieba.word.WordDictionary;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chu163.jieba.utils.FileUtils.getDictAbsolutePath;

public class CaixiAnalyzer {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};

    private String cFile = getDictAbsolutePath("cx.dict");//"D:/workspace/jiebafile/cx.dict";
    private String pcxFile = getDictAbsolutePath("pcx.dict");//"D:/workspace/jiebafile/pcx.dict";

    // 城市对菜系
    private Map<String ,String> cxMap = new HashMap<>();

    // 菜系 解析
    private JiebaSegmenter segmenterC;


    public CaixiAnalyzer() throws FileNotFoundException {

        // 加载菜系
        WordDictionary dictionaryC = new WordDictionary();
        dictionaryC.loadUserDict(new File(cFile));
        this.segmenterC = new JiebaSegmenter(dictionaryC);
        readPCX();
    }


    public String analyzer(String text, String position) {
        String line = text;
        // 去除干扰字符
        for (String f : fu) {
            line = line.replace(f, "");
        }
        // 获取菜系
        String caixi = getCaixi(line);
        // 查询省份对应菜系
        /*if(caixi == null && position != null){
            caixi = mapPCX(position);
        }*/
        if(caixi == null){
            caixi = "家常菜";
        }
        return  caixi;

    }


    // 获取菜系
    private String getCaixi(String line) {
        List<Word> wordp = segmenterC.sentenceProcess(line);
        for (Word w : wordp) {
            if (w.getTokenType().length()>1) {
                //sb.append(w.getToken()+",");
                //System.out.println("W ---- "+ w.getToken() + " - " + w.getTokenType());
                return w.getToken();
            }
        }
        return null;
    }

    // 查询省份对应菜系
    private String mapPCX(String position){
        for(Map.Entry<String, String > p : cxMap.entrySet()){
            if(position.contains(p.getKey())){
                return p.getValue();
            }
        }

        return null;
    }

    // 读取省份 - 菜系
    public void readPCX() {
        try {

            FileReader reader = new FileReader(pcxFile);
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {

                String[] s = str.split(" ");

                cxMap.put(s[0],s[1] );

            }

            br.close();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
