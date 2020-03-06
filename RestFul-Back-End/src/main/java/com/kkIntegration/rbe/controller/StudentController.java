package com.kkIntegration.rbe.controller;

import com.kkIntegration.common.utils.HttpUtils;
import com.kkIntegration.common.utils.SecurityOauth2Utils;
import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.response.HttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * author: ckk
 * create: 2019-12-17 13:56
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/s1")
    public BaseResponse s1() {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

}
