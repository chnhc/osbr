package com.chu163.jieba.controller;

import com.chu163.jieba.analysis.ResumeAnalyzer;
import com.chu163.jieba.analysis.SegmentRecruitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 18:38
 */

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    ResumeAnalyzer resumeAnalyzer;

    @GetMapping("/resume")
    public Object test(@RequestParam( name = "centext") String centext) {
        if(centext.trim().isEmpty()){
            return null;
        }
        try {
            return resumeAnalyzer.analyzer(centext);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



}
