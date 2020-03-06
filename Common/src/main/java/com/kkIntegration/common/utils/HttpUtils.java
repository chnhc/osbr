package com.kkIntegration.common.utils;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.response.HttpResponse;

/**
 * description:
 * author: ckk
 * create: 2020-01-02 14:39
 */
public class HttpUtils {


    /**
     * 统一返回
     * @param Status
     */
    public static BaseResponse Response(BaseResponseStatus Status){
        String value = SecurityOauth2Utils.isRefreshAndClear();
        if(value != null){
            // 统一 成功返回 格式
            return HttpResponse.baseTokenResponse(Status.status ,value);
        }else{
            // 统一 成功返回 格式
            return HttpResponse.baseResponse(Status.status );
        }
    }

    /**
     * 统一返回
     * @param Status
     */
    public static BaseResponse Response(BaseResponseStatus Status, String msg){
        String value = SecurityOauth2Utils.isRefreshAndClear();
        if(value != null){
            // 统一 成功返回 格式
            return HttpResponse.baseTokenResponse(Status.status ,msg ,value);
        }else{
            // 统一 成功返回 格式
            return HttpResponse.baseResponse(Status.status, msg );
        }
    }

    /**
     * 统一返回
     * @param Status
     */
    public static BaseResponse Response(BaseResponseStatus Status,  Object data){
        String value = SecurityOauth2Utils.isRefreshAndClear();
        if(value != null){
            // 统一 成功返回 格式
            return HttpResponse.simpleTokenResponse(Status.status ,value, data);
        }else{
            // 统一 成功返回 格式
            return HttpResponse.simpleResponse(Status.status, data );
        }
    }



    /**
     * 统一返回
     * @param Status
     */
    public static BaseResponse Response(BaseResponseStatus Status, String msg, Object data){
        String value = SecurityOauth2Utils.isRefreshAndClear();
        if(value != null){
            // 统一 成功返回 格式
            return HttpResponse.simpleTokenResponse(Status.status, data , msg, value);
        }else{
            // 统一 成功返回 格式
            return HttpResponse.simpleResponse(Status.status, data, msg );
        }
    }
}
