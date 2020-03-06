package com.kkIntegration.common.exception;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpUtils;
import com.kkIntegration.common.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: ckk
 * create: 2020-01-14 17:37
 */
@RestController
public class ExceptionController implements ErrorController  {

    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * 默认错误
     */
    private static final String path_default = "/error";



    @Override
    public String getErrorPath() {
        return path_default;
    }

    /**
     * JSON格式错误信息
     */
    @RequestMapping(value = path_default,  produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity error(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        //Map<String, Object> body = this.errorAttributes.getErrorAttributes(request, true);
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> body = this.errorAttributes.getErrorAttributes(webRequest, true);
        if(body.containsKey("status")){
            body.remove("status");
        }
        return ResponseUtils.Response(BaseResponseStatus.Running_Failed, body );
    }



}
