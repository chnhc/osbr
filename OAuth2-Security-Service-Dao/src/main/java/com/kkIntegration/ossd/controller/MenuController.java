package com.kkIntegration.ossd.controller;

import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.ossd.service.menu.MenuService;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description: 菜单表
 * author: ckk
 * create: 2020-01-07 14:21
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @GetMapping("/getAllMenu")
    public BaseResponse getAllMenu() {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.selectAllMenuNotUrl());
    }

    /**
     * 添加菜单
     * @return
     */
    @PostMapping("/addMenu")
    public BaseResponse addMenu(@RequestParam(value = "parent_no" ,required =  false ,defaultValue = "00") String parentNo,
                                @RequestParam("menu_name") String menuName,
                                @RequestParam("type") int type,
                                @RequestParam("menu_url") String menuUrl){
        if(parentNo.trim().isEmpty() || menuName.trim().isEmpty() || menuUrl.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        menuService.insertMenu(parentNo, menuName, type, menuUrl);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 获取所有的接口列表 -- 分页
     * @return
     */
    @GetMapping("/getLimitMenu")
    public BaseResponse getLimitMenu(@RequestParam("current_size") int currentSize, @RequestParam("page_size") int pageSize) {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.getLimitMenu(currentSize, pageSize));
    }

    /**
     * 获取主菜单
     * @return
     */
    @GetMapping("/getMainMenu")
    public BaseResponse getMainMenu(){
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.getMainMenu());
    }

    /**
     * 获取所有菜单父级
     * @return
     */
    @GetMapping("/getParMenu")
    public BaseResponse getParMenu(){
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.getParMenu());
    }


    /**
     * 通过no 查询子菜单
     * @return
     */
    @GetMapping("/getSonMenu")
    public BaseResponse getSonMenu(@RequestParam("no") String no){
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.getSonMenu(no));
    }


}
