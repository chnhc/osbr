package com.kkIntegration.OAuth2Server.provider.error;

import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpWriterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuit
 * @create 2018/11/2 10:48
 * @description token 校验失败
 * @modify
 */
@Component
public class BootOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        System.out.println("校验失败！");
        //HttpUtils.writerError(HttpResponse.baseResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage()),response);
        HttpWriterUtils.writerResponse(response , BaseResponseStatus.Token_Failed);
    }

    @Override
    protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> response, Exception exception) {
        System.out.println("校验失败1！");
        return super.enhanceResponse(response, exception);
    }
}
