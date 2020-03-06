package com.kkIntegration.ossd.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.ossd.entity.function.FunctionEntity;
import com.kkIntegration.ossd.service.function.FunctionService;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description: 接口方法
 * author: ckk
 * create: 2020-01-02 14:35
 */
@RestController
@RequestMapping("/fun")
public class FunctionController {

    @Autowired
    FunctionService functionService;


    /**
     * 获取所有的接口列表 -- 分页
     * @return
     */
    @GetMapping("/getLimitFun")
    public BaseResponse getLimitFun(@RequestParam("current_size") int currentSize,@RequestParam("page_size") int pageSize) {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, functionService.getLimitFun(currentSize, pageSize));
    }

    /**
     * 获取所有的接口列表
     * @return
     */
    @GetMapping("/getAllFun")
    public BaseResponse getAllFun() {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, functionService.selectAllFun());
    }

    /**
     * 插入一个接口
     * @return
     */
    @PostMapping("/addFun")
    public BaseResponse addFun(@RequestParam("fun_url") String fun_url, @RequestParam("fun_name") String fun_name) {

        if(fun_url.trim().isEmpty() || fun_name.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setFun_id(fun_url);
        functionEntity.setFun_name(fun_name);
        functionService.insertFun(functionEntity);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 更新 接口url
     * @param new_fun_id
     * @param id
     * @param fun_id
     * @return
     */
    @PostMapping("/updateFunUrl")
    public BaseResponse updateFunUrl(@RequestParam("new_fun_id") String new_fun_id, @RequestParam("id") int id,  @RequestParam("fun_id") String fun_id) {

        if(new_fun_id.trim().isEmpty() || fun_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        functionService.updateFunId(new_fun_id, id, fun_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 更新 接口名称
     * @param new_fun_name
     * @param id
     * @param fun_id
     * @return
     */
    @PostMapping("/updateFunName")
    public BaseResponse updateFunName(@RequestParam("new_fun_name") String new_fun_name, @RequestParam("id") int id,  @RequestParam("fun_id") String fun_id) {

        if(new_fun_name.trim().isEmpty() || fun_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        functionService.updateFunName(new_fun_name, id, fun_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 更新 接口
     * @param new_fun_name
     * @param new_fun_id
     * @param id
     * @param fun_id
     * @return
     */
    @PostMapping("/updateFun")
    public BaseResponse updateFun(@RequestParam("new_fun_id") String new_fun_id, @RequestParam("new_fun_name") String new_fun_name, @RequestParam("id") int id,  @RequestParam("fun_id") String fun_id) {

        if(new_fun_id.trim().isEmpty() || new_fun_name.trim().isEmpty() || fun_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        functionService.updateFun(new_fun_id, new_fun_name, id, fun_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }


    /**
     * 删除 接口
     * @param id
     * @param fun_id
     * @return
     */
    @PostMapping("/deleteFun")
    public BaseResponse deleteFun( @RequestParam("id") int id,  @RequestParam("fun_id") String fun_id) {

        if( fun_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        functionService.deleteFun( id, fun_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

}
