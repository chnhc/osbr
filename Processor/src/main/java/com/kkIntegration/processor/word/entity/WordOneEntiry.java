package com.kkIntegration.processor.word.entity;

import com.kkIntegration.processor.word.WordOneTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * description: controller 里的 子接口
 * author: ckk
 * create: 2020-03-04 11:12
 */
public class WordOneEntiry {

    // method
    private String method;

    // 类型 file     -- 文件：上传下载，
    //     general   --  get/post
    private WordOneTypeEnum type = WordOneTypeEnum.GENERAL;

    // url
    private String url;

    // 子接口作用 -- 简短
    private String title = "";

    // 子接口说明 -- 详细说明
    private String desc = "";

    // 参数列表
    private List<WordParamEntity> params;

    // 返回的实体
    private String response ;





    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public WordOneTypeEnum getType() {
        return type;
    }

    public void setType(WordOneTypeEnum type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<WordParamEntity> getParams() {
        return params;
    }

    public void setParams(List<WordParamEntity> params) {
        this.params = params;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
