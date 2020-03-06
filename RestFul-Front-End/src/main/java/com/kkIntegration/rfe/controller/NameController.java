package com.kkIntegration.rfe.controller;

import com.kkIntegration.rfe.entity.Name;
import com.kkIntegration.rfe.service.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Name")
public class NameController {

    @Autowired
    NameService nameService;


    @GetMapping("/getName")
    public Name getIndex() {
        return nameService.selectNameById();
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
