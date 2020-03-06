package com.kkIntegration.security.authentication;

import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpWriterUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:
 * author: ckk
 * create: 2020-01-14 15:48
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpWriterUtils.writerResponse(response , BaseResponseStatus.Verify_Failed);

    }
}
