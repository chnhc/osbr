package com.kkIntegration.ossd.utils;

/**
 * description: 权限-菜单使用的工具
 * author: ckk
 * create: 2019-12-31 10:04
 */
public class RoleMenuUtils {



    /**
     * 检查 all& 开头 数据库菜单权限和 请求的菜单权限
     *
     * @param roleMenu   all&00-01-1
     * @param menuPrefix 00.01-01-0
     * @return
     */
    public static boolean checkStartWithAllRoleMenu(String roleMenu, String menuPrefix) {
        if (!menuPrefix.startsWith("all&") && roleMenu.startsWith("all&")) {

            String roleNo = roleMenu.split("&")[1]; // 00-01-1
            String[] roleNos = roleNo.split("-"); //   00 , 01 , 1
            String[] menuNos = menuPrefix.split("-"); // 00.01 , 01 , 0
            // 判断全等
            if(roleNos[0].equals(menuNos[0]) && roleNos[1].equals(menuNos[1])){
                return true;
            }
            String[] Nos = menuNos[0].split("\\."); // 00.01 = 00, 01, 01, ....
            String no = mergeNosByRoleNos(roleNos[1], Nos);
            //String[] menuNos = menuPrefix.split("-"); // 01 , 00.01.01 ,0
            // 只需要判断前2位
            return (roleNos[0] + "-" + roleNos[1]).equals(no);
        }
        return false;

    }


    private static String mergeNosByRoleNos(String roleNo, String[] Nos){
        //
        StringBuffer buffer = new StringBuffer();
        for(String n : Nos){

            // 如果是 和 权限相同
            if(roleNo.equals(n)){
                return buffer.toString()+"-"+n;
            }else{
                if(buffer.length() == 0){
                    buffer.append(n);
                }else{
                    buffer.append(".");
                    buffer.append(n);
                }
            }
        }
        return buffer.toString();
    }

}
