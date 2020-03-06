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
public class SimpleResponse extends BaseResponse {

    private Object data;

    protected SimpleResponse() {
    }

    protected SimpleResponse(int status, String msg, Object item) {
        super(status, msg);
        this.data = item;
    }


}
