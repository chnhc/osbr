package com.kkIntegration.OAuth2Common.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkIntegration.common.utils.HttpUtils;
import com.kkIntegration.common.utils.HttpWriterUtils;
import com.kkIntegration.common.utils.SecurityOauth2Utils;
import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.response.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不够， 访问拒绝
 */
@Component("appAccessDeniedHandler")
public class AppAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
     /*   response.setContentType(ZuulAppConstant.CONTENT_TYPE_JSON);
        response.setStatus(HttpStatus.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(ResultVo.createErrorResult("当前用户访问权限不够,请联系管理员增加对应权限",403)));
*/
       /* response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("当前用户访问权限不够,请联系管理员增加对应权限")));
*/
        //response.setContentType("application/json;charset=UTF-8");


/*        Map map = new HashMap();
        map.put("error", "40140");
        map.put("message", accessDeniedException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));*/

        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.getWriter().write(objectMapper.writeValueAsString(HttpUtils.Response(BaseResponseStatus.Role_Failed, accessDeniedException.getMessage())));
        /*response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(HttpResponse.simpleResponse(BaseResponseStatus.USER_PRIVILEGE_FAIL.status)));
*/
        HttpWriterUtils.writerResponse(response , HttpUtils.Response(BaseResponseStatus.Role_Failed));

    }

}

