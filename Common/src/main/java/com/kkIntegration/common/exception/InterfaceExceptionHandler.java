package com.kkIntegration.common.exception;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * description: 自定义异常处理器
 * author: ckk
 * create: 2020-01-14 16:58
 */
@RestControllerAdvice
public class InterfaceExceptionHandler {

    /**
     * 拦截所有地址找不到时的全局异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public BaseResponse runtimeException(NoHandlerFoundException e) {
        return HttpUtils.Response(BaseResponseStatus.Running_Failed, e.getMessage());
    }

    /**
     * 拦截所有运行时的全局异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse runtimeException(RuntimeException e) {
       return HttpUtils.Response(BaseResponseStatus.Running_Failed, e.getMessage());
    }

    /**
     * 系统异常捕获处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse exception(Exception e) {
        return HttpUtils.Response(BaseResponseStatus.Running_Failed, e.getMessage());
    }


}
