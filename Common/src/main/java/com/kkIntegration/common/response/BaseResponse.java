package com.kkIntegration.common.response;

import lombok.Data;

/**
 * @author yuit
 * @create time  2018/3/30 20:37
 * @description 基础通用返回体
 * @modify
 * @modify time
 */
@Data
public class BaseResponse  {

    private int status;
    private String message;

    protected BaseResponse() {
    }

    protected BaseResponse(int status, String msg) {
        this.status = status;
        this.message = msg;

    }



}
