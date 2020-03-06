package com.kkIntegration.security.authentication;

import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpWriterUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:
 * author: ckk
 * create: 2020-01-14 15:50
 */
public class MyAccessDeineHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpWriterUtils.writerResponse(response , BaseResponseStatus.Verify_Failed);
    }
}
