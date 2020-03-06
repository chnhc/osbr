package com.kkIntegration.common.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.response.HttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuit
 * @create 2018/11/5 16:34
 * @description
 * @modify
 */
public class HttpWriterUtils {


    public static void writerResponse(HttpServletResponse response , BaseResponseStatus Status) throws IOException {
        response.setContentType("application/json,charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), HttpUtils.Response(Status));
    }

    public static void writerResponse(HttpServletResponse response , BaseResponse data) throws IOException {
        response.setContentType("application/json,charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), data);
    }

}
