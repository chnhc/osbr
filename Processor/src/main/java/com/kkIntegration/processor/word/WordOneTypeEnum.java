package com.kkIntegration.processor.word;

public enum WordOneTypeEnum {


    GENERAL (1, "普通类型"),
    FILE_UPLOAD (2, "文件上传类型"),
    FILE_DOWNLOAD (3, "文件下载类型"),




    ;






    private int type;
    private String desc;

    WordOneTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
