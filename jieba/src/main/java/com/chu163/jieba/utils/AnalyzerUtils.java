package com.chu163.jieba.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 18:43
 */
public class AnalyzerUtils {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};


    // 获取字符在字符串的所有位置
    public static Integer[] getAllDistances(String text, String check) {
        // 存储 位置
        List list = new ArrayList();
        // 如果有多个位置，下次查找开始位置
        int index = 0;
        // 位置
        int position;
        while ((position = text.indexOf(check, index)) != -1) {
            // 下次查找开始位置 ， 需要加上字符的长度大小
            index = position + check.length();
            // 位置保存
            list.add(position);
        }
        // 将list转换数组
        return (Integer[]) list.toArray(new Integer[list.size()]);
    }


    // 计算距离 ---  判断当前位置于map数组中的哪个近
    //  currentPosition  10
    // mapDistance [ 8,15,20]  , [ 6,12,18]
    //
    public static String CalculatedDistance(int currentPosition, Map<String, Integer[]> mapDistance) {
        // 存储  对应字符串 -- 最小距离
        Map<String, Integer> distance = new HashMap<>();
        for (Map.Entry<String, Integer[]> entry : mapDistance.entrySet()) {
            // 查找一个的距离，并存到distance中
            checkAndStore(currentPosition, entry, distance);
        }
        // 最小值所在的字符串，用于返回
        String minString = null;
        // 记录最小距离, 默认为整数最大值
        int minDistance = Integer.MAX_VALUE;
        // 判断对应字符串，与记录最小距离比较
        for (Map.Entry<String, Integer> entry : distance.entrySet()) {
            if (entry.getValue() < minDistance) {
                minString = entry.getKey();
                minDistance = entry.getValue();
            }
        }
        return minString;
    }

    // 查找一个的距离，并存到distance中
    public static void checkAndStore(int currentPosition, Map.Entry<String, Integer[]> entry, Map<String, Integer> distance) {
        for (int i = 0, len = entry.getValue().length; i < len; i++) {
            // 如果只有一个元素，则不需要拦截
            // 如果 i 到了最后一个元素，则没必要继续判断
            /*if(i != 0 && i == len-1){
                return;
            }*/
            // 如果第一个 i=0 的时候，只需要判断 i+1 即可
            if (i == 0) {
                if (entry.getValue()[i] > currentPosition) {
                    distance.put(entry.getKey(), entry.getValue()[i]);
                    return;
                }
            } else {
                // 如果当前位置为10 ， 数组为[ 8,15,20]
                // 只要 [i-1] < 10  && [i] > 10  成立
                // 那么 [i] 就是需要的值
                if (entry.getValue()[i - 1] < currentPosition && entry.getValue()[i] > currentPosition) {
                    distance.put(entry.getKey(), entry.getValue()[i]);
                    return;
                }
            }
        }
    }

    // 获取电话号码
    public static String getTelnum(String context) {
        String sParam = context;
        // 去除干扰字符
        for (String f : fu) {
            sParam = sParam.replace(f, "");
        }
        if (sParam == null || sParam.length() <= 0)
            return null;
        Pattern pattern = Pattern.compile("(1|861)([0-9])\\d{9}$*");
        Matcher matcher = pattern.matcher(sParam);
        StringBuffer bf = new StringBuffer();
        while (matcher.find()) {
            bf.append(matcher.group());
            break;
        }
        return bf.toString();
    }



}
