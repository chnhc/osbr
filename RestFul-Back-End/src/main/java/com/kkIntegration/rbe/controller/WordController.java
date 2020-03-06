package com.kkIntegration.rbe.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.ossd.service.user.UserService;
import com.kkIntegration.processor.word.annotation.WordAnnotation;
import com.kkIntegration.processor.word.annotation.WordOneAnnotation;
import com.kkIntegration.processor.word.annotation.WordParamAnnotation;
import com.kkIntegration.rbe.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * description: 测试文档接口
 * author: ckk
 * create: 2019-12-17 13:53
 */
@WordAnnotation(title = "文档接口测试标题1111")
@RestController
@RequestMapping("/word")
public class WordController {

    @WordOneAnnotation(title = "test1接口", response = BaseResponse.class)
    @GetMapping("/test1")
    public String test1(){
        return "test1";
    }

    @WordOneAnnotation(title = "test2接口", response = Integer.class)
    @GetMapping("/test2")
    public String test2(
            @WordParamAnnotation (desc="param参数1") @RequestParam("param111") String param1,
            @WordParamAnnotation (desc="param参数2") @RequestParam("param222") String param2){
        return param1;
    }

    @PostMapping("/test3")
    public String test3(@RequestParam("paramsss") String param){
        return param;
    }


}
