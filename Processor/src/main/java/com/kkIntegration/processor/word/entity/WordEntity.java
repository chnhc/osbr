package com.kkIntegration.processor.word.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 一个controller文档实体
 * author: ckk
 * create: 2020-03-04 11:09
 */
public class WordEntity {


    // 分类标签名
    private String name;

    // controller 里的 一个接口
    private List<WordOneEntiry> sonWords = new ArrayList<>();






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WordOneEntiry> getSonWords() {
        return sonWords;
    }

    public void setSonWords(List<WordOneEntiry> sonWords) {
        this.sonWords = sonWords;
    }
}
