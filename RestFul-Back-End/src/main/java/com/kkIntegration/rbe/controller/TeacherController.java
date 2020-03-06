package com.kkIntegration.rbe.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * author: ckk
 * create: 2019-12-17 13:57
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {


    @GetMapping("/t1")
    public BaseResponse t1() {

        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

}
