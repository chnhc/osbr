package com.kkIntegration.common.response;


/**
 * @author yuit
 * @create time  2018/3/30 20:37
 * @description
 * @modify
 * @modify time
 */
public final class HttpResponse {

    /**--------------------------BaseResponse-------------------------------------------------*/
    public  static BaseResponse baseResponse(int status) {
        return baseResponse(status, null);
    }

    public  static BaseResponse baseResponse(int status, String msg) {

        if (msg != null) {
            return new BaseResponse(status, msg);
        } else {
            return new BaseResponse(status, BaseResponseStatus.getMessageByStatus(status));
        }
    }


    public static BaseResponse successResponse(){
        return baseResponse(200);
    }

    public static BaseResponse successResponse(String msg){
        return baseResponse(200,msg);
    }

    /**--------------------------BaseTokenResponse-------------------------------------------------*/
    public  static BaseTokenResponse baseTokenResponse(int status, String Token) {
        return  baseTokenResponse(status, null, Token);
    }

    public  static BaseTokenResponse baseTokenResponse(int status , String msg , String Token) {

        BaseTokenResponse response = new BaseTokenResponse();
        response.setStatus(status);
        response.setNewToken(Token);
        if (msg != null) {
            response.setMessage(msg);
        } else {
            response.setMessage(BaseResponseStatus.getMessageByStatus(status));
        }
        return response;
    }



    /**--------------------------SimpleResponse-------------------------------------------------*/
    public  static SimpleResponse simpleResponse(int status) {
        return simpleResponse(status, null, null);
    }


    public  static SimpleResponse simpleResponse(int status, Object data) {
        return simpleResponse(status,  data , null);
    }

    public  static SimpleResponse simpleResponse(int status, Object data , String msg) {

        SimpleResponse response = new SimpleResponse();
        response.setStatus(status);
        if (msg != null) {
            response.setMessage(msg);
        } else {
            response.setMessage(BaseResponseStatus.getMessageByStatus(status));
        }
        response.setData(data);
        return response;
    }

    public static SimpleResponse successSimpleResponse(Object data){
        return simpleResponse(200,data);
    }

    public static SimpleResponse successSimpleResponse(String msg,Object data){
        return simpleResponse(200,data,msg);
    }



    /**--------------------------SimpleTokenResponse-------------------------------------------------*/
    public  static SimpleTokenResponse simpleTokenResponse(int status, String Token) {
        return  simpleTokenResponse(status, null, null, Token);
    }

    public  static SimpleTokenResponse simpleTokenResponse(int status, String Token, Object data ) {
        return  simpleTokenResponse(status, data, null, Token);
    }

    public  static SimpleTokenResponse simpleTokenResponse(int status, Object data , String msg , String Token) {

        SimpleTokenResponse response = new SimpleTokenResponse();
        response.setStatus(status);
        response.setNewToken(Token);
        if (msg != null) {
            response.setMessage(msg);
        } else {
            response.setMessage(BaseResponseStatus.getMessageByStatus(status));
        }
        response.setData(data);
        return response;
    }



}
