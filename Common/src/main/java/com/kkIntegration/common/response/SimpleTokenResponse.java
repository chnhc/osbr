package com.kkIntegration.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yuit
 * @create time  2018/3/30 20:37
 * @description
 * @modify
 * @modify time
 */
@Getter
@Setter
public class SimpleTokenResponse extends BaseTokenResponse {

    private Object data;

    protected SimpleTokenResponse() {
    }

    protected SimpleTokenResponse(int status, String msg, Object item, String token) {
        super(status, msg, token);
        this.data = item;
    }


}
