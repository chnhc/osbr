package com.kkIntegration.ossd.utils;

import com.kkIntegration.common.utils.CommonUtils;

import java.util.UUID;

/**
 * description: 菜单工具
 * author: ckk
 * create: 2020-01-10 15:10
 */
public class MenuUtils {

    public static String getMenuId(String parentNo, int sum, int type) {
        // 父-顺序-类型&uuid
        return parentNo + "-" + String.format("%02d", sum+1) + "-" + type + "&" + CommonUtils.uuid32Generator();
    }


}
