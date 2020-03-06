package com.kkIntegration.OAuth2Resource.provider.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkIntegration.common.utils.HttpUtils;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpWriterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 BootOAuth2AuthExceptionEntryPoint 用于tokan校验失败返回信息
 */
public class BootOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

 /*       Map map = new HashMap();
        map.put("error", "401100");
        map.put("message", authException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));*/

       /* String value = SecurityOauth2Utils.isRefresh();
        BaseResponse baseResponse;
        if(value != null){
            // 统一 成功返回 格式
            baseResponse = HttpResponse.baseTokenResponse(BaseResponseStatus.Token_Failed.status
                    ,authException.getMessage() , value);
        }else{
            // 统一 成功返回 格式
            baseResponse = HttpResponse.baseResponse(BaseResponseStatus.Token_Failed.status, authException.getMessage());
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));*/


        HttpWriterUtils.writerResponse(response , HttpUtils.Response(BaseResponseStatus.Token_Failed));


        //HttpUtils.writerError(HttpResponse.simpleResponse(BaseResponseStatus.Token_Validation_Failed.status, SecurityContextHolder.getContext(),authException.getMessage()),response);
        /*HttpUtils.writerError(HttpResponse.baseResponse(40044,"token验证失败"),response);
*/
    }
}
