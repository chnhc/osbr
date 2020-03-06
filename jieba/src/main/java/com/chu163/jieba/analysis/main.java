package com.chu163.jieba.analysis;

import com.qianxinyao.analysis.jieba.keyword.Keyword;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chu163.jieba.utils.FileUtils.getDictAbsolutePath;
import static com.chu163.jieba.utils.FileUtils.getIDFAbsolutePath;

public class main {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};

    private static String[] zi = new String[]{"合作"};

    public static void main(String[] args) throws Exception {
        //String content = "福州晋安区东二环泰禾广场商场5楼蛙功夫招聘：炒锅数名（新手锅也可以）：4000-6000，洗碗大姐2名:3500-4500，烧烤人员1名:3500-4500，打荷人员1名:3500-4500，以上人员月休4天，国家法定节假日补休，正常班，包吃住，做的好每3个月进行调薪1次，要求过年找厨师去，年前年后都可以安排回去，做满一年带薪年假7天，联系人张店：18650398903";
        //String content = "招配菜一位工资5500有休息有年假地址姑苏区西环路新庄鑫园楼酒店电话13451982484,沈店，谢谢平台，";
        //String content = "店里直招 中餐厨师：1名 工资：6000到8000 中餐厨师长 1名工资面议 服务员：数名 工资：4000――6000 捣江湖的勿扰 联系电话15775663165 联系人：唐主管 把址 苏州工业园区";
        //String content = "招打荷一名 3500到4000 过年不放假 月休四天 节假日另有休息 包吃包住 福利多 每月有聚餐 工作地点金鸡湖 电话:13914095814 -谢谢";

        //String content = "2F-0315NB新西兰厨师：要求四川人，有四川火锅经验至少3年。吃苦。待遇一周6天，每周税后680-700纽币，按照工作表现，三个月适用期后会再加一次工资。包工作餐，不包住宿。（人力境外就业 www.sinosm.com 陈经理 手机 18149557009 微信 17121891399 商务部资质，有收费敬请谅解）";
        String content = "急招学徒打杂人员多名工资4000～4500，配菜一名工资4500～5000，月休两天，年底不放假双薪，佛山祖庙上班，联系电话17665053293向师傅微信同步";
        //String content = "招聘服务员，要求：女，形象好，开朗活泼，工资底薪3200 提成，一月能拿3500 ，包食宿，宿舍环境很好，家具齐全，店里生意不忙，有意者联系 18509290713 叶 微信同号";
        //String content = "招聘展板小工一名3000左右 月休3天 活简单好干 店里管吃管住 宿舍空调洗衣机WIFI 设备齐全 地址在东郊纺织城附近 没上班的 欢迎打扰15535985267微信同步";
        //String content = "鄞州餐饮店招切配师傅4500-5500.服务员4000_4500以上 男女不限 月休3 天，包吃住，联系方式18268692683";


        // 去除干扰字符
        for (String z : zi) {
            content = content.replace(z, "");
        }

        // 获取电话
        String phone = getTelnum(content);
        // 没有手机号码不执行
        if (phone == null || phone.isEmpty()) {
            return;
        }



        // 区域
        PositionAnalyzer positionAnalyzer = new PositionAnalyzer();
        String position = positionAnalyzer.analyzer(content, phone);




        // 菜系
        CaixiAnalyzer caixiAnalyzer = new CaixiAnalyzer();
        String caixi;
        if (!"暴力查询".equals(position)) {
            caixi = caixiAnalyzer.analyzer(content, position);
        } else {
            caixi = caixiAnalyzer.analyzer(content, null);
        }



        int topN = 15;

        // 工资解析
        String salary_ciKuPath =  getDictAbsolutePath("jieba_salary.dict");//"D:/workspace/jiebafile/jieba_salary.dict";
        String salary_idfPath = getIDFAbsolutePath("idf_salary.txt");//"/idf_salary.txt";
        ChuAnalyzer salaryAnalyzer = new ChuAnalyzer(salary_ciKuPath, salary_idfPath);
        List<Keyword> salaryList = salaryAnalyzer.analyze(content, topN);
        // 多个工资问题
        Map<String, Integer[]> salaryDistance = new HashMap<>();
        for (Keyword word : salaryList) {
            // 大于1 就是我们需要的 word.getName()
            if (word.getTfidfvalue() > 1) {
                salaryDistance.put(word.getName(), getAllDistances(content, word.getName()));
                //System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
            }
        }

        // 人数解析
        String num_ciKuPath = getDictAbsolutePath("jieba_num.dict");//"D:/workspace/jiebafile/jieba_num.dict";
        String num_idfPath = getIDFAbsolutePath("idf_num.txt");//"/idf_num.txt";
        ChuAnalyzer numAnalyzer = new ChuAnalyzer(num_ciKuPath, num_idfPath);
        List<Keyword> numList = numAnalyzer.analyze(content, topN);
        // 多个人数问题
        Map<String, Integer[]> numDistance = new HashMap<>();
        for (Keyword word : numList) {
            // 大于1 就是我们需要的 word.getName()
            if (word.getTfidfvalue() > 0.5) {
                numDistance.put(word.getName(), getAllDistances(content, word.getName()));
                //System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
            }
        }

        // 联系人解析
        String name = null;
        String xing_ciKuPath = getDictAbsolutePath("jieba_xing.dict");//"D:/workspace/jiebafile/jieba_xing.dict";
        String xing_idfPath = getIDFAbsolutePath("idf_xing.txt");//"/idf_xing.txt";
        ChuAnalyzer xingAnalyzer = new ChuAnalyzer(xing_ciKuPath, xing_idfPath);
        List<Keyword> xingList = null;
        String contextXing = null;
        if (content.contains("联系")) {
            contextXing = content.substring(content.indexOf("联系"));
        } else if (content.contains("电话")) {
            contextXing = content.substring(content.indexOf("电话"));
        } else if (content.contains("话")) {
            contextXing = content.substring(content.indexOf("话"));
        } else if (content.contains("电")) {
            contextXing = content.substring(content.indexOf("电"));
        } else if (content.contains("手机")) {
            contextXing = content.substring(content.indexOf("手机"));
        } else if (content.contains("微信")) {
            contextXing = content.substring(content.indexOf("微信") - 15);
        }

        if (contextXing != null) {
            // 从contextXing去除电话号码
            contextXing = contextXing.replace(phone, "");
            // 去除干扰字符
            for (String f : fu) {
                contextXing = contextXing.replace(f, "");
            }
            xingList = xingAnalyzer.analyze(contextXing, topN, true);

            for (Keyword word : xingList) {
                // 大于1 就是我们需要的 word.getName()
                if (word.getTfidfvalue() > 1) {
                    if (name == null) {
                        name = word.getName();
                        //System.out.print(word.getName() + ":" + word.getTfidfvalue() + ",");
                    } else {
                        name = "师傅";
                    }
                }
            }
        }
        if (name == null) {
            name = "师傅";
        } else if (name.length() == 1) {
            name += "师傅";
        }

        // 福利
        WelfareAnalyzer welfareAnalyzer = new WelfareAnalyzer();
        String welfares = welfareAnalyzer.analyzer(content);


        // 职位解析
        String job_ciKuPath = getDictAbsolutePath("jieba_job.dict");//"D:/workspace/jiebafile/jieba_job.dict";
        String job_idfPath = getIDFAbsolutePath("idf_job.txt");//"/idf_job.txt";
        ChuAnalyzer jobAnalyzer = new ChuAnalyzer(job_ciKuPath, job_idfPath);
        List<Keyword> jobList = jobAnalyzer.analyze(content, topN);
        for (Keyword word : jobList) {
            if (word.getTfidfvalue() > 1) {
                String jobName = word.getName();
                String salary = CalculatedDistance(content.indexOf(jobName), salaryDistance);
                String num = CalculatedDistance(content.indexOf(jobName), numDistance);
                if (salary == null) {
                    salary = "面议";
                }
                if (num == null) {
                    num = "默认一个人";
                }
                System.out.println(position + " : 菜系 " + caixi + " : 职位 " + jobName + " : " + num + " : " + salary + " : 福利 " + welfares + " : 联系-" + name + " : 电话" + phone + ",");
            }
        }


    }


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
