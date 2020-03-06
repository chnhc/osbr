package com.kkIntegration.processor.word.entity;

/**
 * description: 参数
 * author: ckk
 * create: 2020-03-04 11:19
 */
public class WordParamEntity {

    // 参数名
    private String name;

    // 默认值
    private String defaultVal = "";

    // 参数类型
    private String type = "";

    // 说明
    private String desc = "";







    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
