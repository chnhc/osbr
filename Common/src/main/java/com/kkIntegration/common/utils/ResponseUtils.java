package com.kkIntegration.common.utils;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * description: 系统 ResponseEntity 的封装
 * author: ckk
 * create: 2020-01-14 14:43
 */
public class ResponseUtils {


    /**
     * 统一返回
     * @param Status
     */
    public static ResponseEntity Response(BaseResponseStatus Status){
        return Response(new HttpHeaders(), Status);
    }
    public static ResponseEntity Response(HttpHeaders headers, BaseResponseStatus Status){
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<BaseResponse>(HttpUtils.Response(Status), headers, HttpStatus.OK);
    }

    /**
     * 统一返回
     * @param Status
     */
    public static ResponseEntity Response(BaseResponseStatus Status, String msg){
        return Response(new HttpHeaders(), Status, msg);
    }

    public static ResponseEntity Response(HttpHeaders headers, BaseResponseStatus Status, String msg){
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<BaseResponse>(HttpUtils.Response(Status,msg), headers, HttpStatus.OK);
    }

    /**
     * 统一返回
     * @param Status
     */
    public static ResponseEntity Response(BaseResponseStatus Status, Object data){
        return Response(new HttpHeaders(), Status, data);
    }

    public static ResponseEntity Response(HttpHeaders headers, BaseResponseStatus Status,  Object data){
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<BaseResponse>(HttpUtils.Response(Status, data), headers, HttpStatus.OK);
    }



    /**
     * 统一返回
     * @param Status
     */
    public static ResponseEntity Response(BaseResponseStatus Status, String msg, Object data){
       return Response(new HttpHeaders(), Status, msg, data);
    }

    public static ResponseEntity Response(HttpHeaders headers, BaseResponseStatus Status, String msg, Object data){
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<BaseResponse>(HttpUtils.Response(Status, msg, data), headers, HttpStatus.OK);
    }




}
