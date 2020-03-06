package com.kkIntegration.ossd.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.ossd.entity.role.AuthRoleForm;
import com.kkIntegration.ossd.service.role.RoleUserService;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description: 用户权限
 * author: ckk
 * create: 2020-01-14 11:21
 */
@RestController
@RequestMapping("/roleUser")
public class RoleUserController {

    @Autowired
    RoleUserService roleUserService;

    /**
     * 获取所有的权限列表 -- 分页
     * @return
     */
    @GetMapping("/getLimitRoleUser")
    public BaseResponse getLimitRole(@RequestParam("current_size") int currentSize, @RequestParam("page_size") int pageSize) {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, roleUserService.getLimitRoleUser(currentSize, pageSize));
    }


    /**
     * 授权处理 -- 撤权 、 授权
     * @return
     */
    @PostMapping("authRoleUser")
    public BaseResponse authRoleUser(AuthRoleForm authRoleForm){
        if(authRoleForm.getTarget_id().trim().isEmpty() ){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        return HttpUtils.Response(BaseResponseStatus.SUCCESS,  roleUserService.authRoleUser(authRoleForm.getTarget_id(), authRoleForm.getTarget_roles()));

    }


}
