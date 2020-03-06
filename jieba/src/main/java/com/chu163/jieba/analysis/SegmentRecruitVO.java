package com.chu163.jieba.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 18:57
 */
public class SegmentRecruitVO implements Serializable {
    //菜系id
    private String foodTypeId;

    private String foodTypeName;

    //职位id
    private String jobId;

    private String jobName;

    //薪资 "2-3千"格式
    private String salary;

    private String welfares;

    //招聘人数
    private String recruitPeopleNum;

    //经验
    private String experience;

    //private String welfares;

    //职位描述
    private String description;

    private String contactWay;

    private String name;

    private String position;

    public SegmentRecruitVO(String foodTypeId, String foodTypeName, String jobId,
                            String jobName, String salary,
                            String recruitPeopleNum, String experience,
                            String description,
                            String contactWay,String name,String welfares, String position) {
        this.foodTypeId = foodTypeId;
        this.foodTypeName = foodTypeName;
        this.jobId = jobId;
        this.jobName = jobName;
        this.salary = salary == null ? "3-4千" : salary;
        this.recruitPeopleNum = recruitPeopleNum == null ? "1人" : recruitPeopleNum;
        this.experience = experience == null ? "不限" : experience;
        this.description = description;
        this.contactWay = contactWay;
        this.welfares = welfares;
        this.name = name;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(String foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getWelfares() {
        return welfares;
    }

    public void setWelfares(String welfares) {
        this.welfares = welfares;
    }

    public String getRecruitPeopleNum() {
        return recruitPeopleNum;
    }

    public void setRecruitPeopleNum(String recruitPeopleNum) {
        this.recruitPeopleNum = recruitPeopleNum;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
