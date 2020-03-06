package com.kkIntegration.rbe.controller;

import com.kkIntegration.ossd.entity.user.UserEntity;
import com.kkIntegration.ossd.service.user.UserService;
import com.kkIntegration.rbe.service.TableService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * author: ckk
 * create: 2019-12-17 13:53
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    TableService tableService;

    @GetMapping("/user1")
    public String user1() {



        userService.insertUser();

        return "user1";
    }

    @GetMapping("/user2")
    public String user2(@RequestParam Integer num) {

        return tableService.addTableBatch(num)+" : " +num;
    }

    @GetMapping("/user3")
    public String user3() {
        return "user3";
    }

    @GetMapping("/user4")
    public ResponseEntity user4() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        System.out.println();
        return new ResponseEntity<String>("123123123", headers, HttpStatus.OK);
    }





}
