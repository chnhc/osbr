package com.kkIntegration.common.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuit
 * @create time  2018/3/30 20:37
 * @description 基础通用返回体
 * @modify
 * @modify time
 */
@Getter
@Setter
public class BaseTokenResponse extends BaseResponse {

    private String newToken;

    public BaseTokenResponse(){

    }

    public BaseTokenResponse(int status, String msg, String token) {
        super(status, msg);
        this.newToken = token;
    }
}
