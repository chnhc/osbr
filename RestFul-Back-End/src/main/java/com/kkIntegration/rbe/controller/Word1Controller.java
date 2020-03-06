package com.kkIntegration.rbe.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.processor.word.annotation.WordAnnotation;
import com.kkIntegration.processor.word.annotation.WordOneAnnotation;
import com.kkIntegration.processor.word.annotation.WordParamAnnotation;
import org.springframework.web.bind.annotation.*;

/**
 * description: 测试文档接口
 * author: ckk
 * create: 2019-12-17 13:53
 */
@WordAnnotation(title = "文档接口测试标题2222")
@RestController
@RequestMapping("/word22")
public class Word1Controller {

    @WordOneAnnotation(title = "test1接口2", response = BaseResponse.class)
    @GetMapping("/test122")
    public String test1(){
        return "test12";
    }

    @WordOneAnnotation(title = "test2接口2", response = Integer.class)
    @GetMapping("/test222")
    public String test2(
            @WordParamAnnotation (desc="param参数1") @RequestParam("param1112") String param1,
            @WordParamAnnotation (desc="param参数2") @RequestParam("param2222") String param2){
        return param1;
    }

    @PostMapping("/test322")
    public String test3(@RequestParam("paramsss2") String param){
        return param;
    }


}
