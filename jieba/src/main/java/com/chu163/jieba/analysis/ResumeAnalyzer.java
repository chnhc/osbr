package com.chu163.jieba.analysis;

import com.qianxinyao.analysis.jieba.keyword.Keyword;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chu163.jieba.utils.AnalyzerUtils.*;
import static com.chu163.jieba.utils.FileUtils.getDictAbsolutePath;
import static com.chu163.jieba.utils.FileUtils.getIDFAbsolutePath;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 18:39
 */
@Component
public class ResumeAnalyzer {

    private static String[] fu = new String[]{" ", ",", "，", ":", "：", "!", "！", "（", "）", "(", ")", ".", "。", "、", "\r\n", "-", "_",
            "~", "`", "#", "@", "$", "%", "^", "*", "&", "{", "}", "[", "]", "【", "】", "'", "\"", "|", "\\", "<", ">", "?", "？", "/", "=", "+", ";", "；"};


    // 混淆的字，词语
    private static String[] zi = new String[]{"合作"};
    // idf 前几位输出
    private int topN = 15;
    // 位置解析器
    private PositionAnalyzer positionAnalyzer;
    // 菜系解析器
    private CaixiAnalyzer caixiAnalyzer;
    // 工资解析器
    private ChuAnalyzer salaryAnalyzer;
    // 人数解析器
    private ChuAnalyzer numAnalyzer;
    // 联系人解析器
    private ChuAnalyzer xingAnalyzer;
    // 福利解析器
    private WelfareAnalyzer welfareAnalyzer;
    //职位解析器
    private ChuAnalyzer jobAnalyzer;

    @PostConstruct
    public void initPost() throws FileNotFoundException {
        // 位置
        this.positionAnalyzer = new PositionAnalyzer();
        // 菜系
        this.caixiAnalyzer = new CaixiAnalyzer();
        // 工资
        String salary_ciKuPath =  getDictAbsolutePath("jieba_salary.dict");//"D:/workspace/jiebafile/jieba_salary.dict";
        String salary_idfPath = getIDFAbsolutePath("idf_salary.txt");//"/idf_salary.txt";
        this.salaryAnalyzer = new ChuAnalyzer(salary_ciKuPath, salary_idfPath);
        // 人数
        String num_ciKuPath = getDictAbsolutePath("jieba_num.dict");//"D:/workspace/jiebafile/jieba_num.dict";
        String num_idfPath = getIDFAbsolutePath("idf_num.txt");//"/idf_num.txt";
        numAnalyzer = new ChuAnalyzer(num_ciKuPath, num_idfPath);
        // 联系人
        String xing_ciKuPath = getDictAbsolutePath("jieba_xing.dict");//"D:/workspace/jiebafile/jieba_xing.dict";
        String xing_idfPath = getIDFAbsolutePath("idf_xing.txt");//"/idf_xing.txt";
        xingAnalyzer = new ChuAnalyzer(xing_ciKuPath, xing_idfPath);
        // 福利
        welfareAnalyzer = new WelfareAnalyzer();
        // 职位
        String job_ciKuPath = getDictAbsolutePath("jieba_job.dict");//"D:/workspace/jiebafile/jieba_job.dict";
        String job_idfPath = getIDFAbsolutePath("idf_job.txt");//"/idf_job.txt";
        jobAnalyzer = new ChuAnalyzer(job_ciKuPath, job_idfPath);

    }

    public List<SegmentRecruitVO> analyzer(String contentOld) throws FileNotFoundException {
        String content = contentOld;
        // 去除干扰字符
        for (String z : zi) {
            content = content.replace(z, "");
        }

        // 获取电话
        String phone = getTelnum(content);
        // 没有手机号码不执行
        if (phone == null || phone.isEmpty()) {
            return null;
        }

        // 区域
        String position = positionAnalyzer.analyzer(content, phone);


        // 菜系
        String caixi;
        if (!"暴力查询".equals(position)) {
            caixi = caixiAnalyzer.analyzer(content, position);
        } else {
            caixi = caixiAnalyzer.analyzer(content, null);
        }



        // 工资解析
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
        String welfares = welfareAnalyzer.analyzer(content);


        ArrayList<SegmentRecruitVO> result = new ArrayList<SegmentRecruitVO>();

        // 职位解析
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
                /*String foodTypeId, String foodTypeName, String jobId,
                        String jobName, String salary,
                        String recruitPeopleNum, String experience,
                        String description,
                        String contactWay,List<String> welfares*/
                result.add(new SegmentRecruitVO(
                        "foodTypeID",
                        caixi,
                        "jobId",
                        jobName,
                        salary,
                        num,
                        "经验",
                        contentOld,
                        phone,
                        name,
                        welfares,
                        position
                ));
                //System.out.println(position + " : 菜系 " + caixi + " : 职位 " + jobName + " : " + num + " : " + salary + " : 福利 " + welfares + " : 联系-" + name + " : 电话" + phone + ",");
            }
        }






        return result;
    }

}
