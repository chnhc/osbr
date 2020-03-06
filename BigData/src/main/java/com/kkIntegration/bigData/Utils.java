package com.kkIntegration.bigData;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * description: 工具
 * author: ckk
 * create: 2020-01-20 15:26
 */
public class Utils {

    public static String getInfo(Object o, String msg){
        return getHostName() + " : " +getPID() + " : " + getTID() + " : " + getObjInfo(o) + " : " + msg;
    }

    public static String getInfo(Object o){
        return getHostName() + " : " +getPID() + " : " + getTID() + " : " + getObjInfo(o) ;
    }

    /**
     * 获取主机名
     * @return
     */
    public static String getHostName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前进程id
     * @return
     */
    public static Integer getPID(){
        String info = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(info.substring(0, info.indexOf("@")));
    }


    /**
     * 获取当前线程id
     * @return
     */
    public static String getTID(){
        return Thread.currentThread().getName();
    }


    /**
     * 获取当前对象 信息
     * @return
     */
    public static String getObjInfo(Object o){
        return o.getClass().getSimpleName() +" @ "+ o.hashCode();
    }

}
