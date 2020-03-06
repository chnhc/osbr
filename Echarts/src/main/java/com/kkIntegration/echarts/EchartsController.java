package com.kkIntegration.echarts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/echarts")
public class EchartsController {


    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String sayHello(){

        return "Hello Spring Boot!";

    }

    @RequestMapping(value="/test",method = RequestMethod.GET)
    public ModelAndView firstDemo(){

        return new ModelAndView("test1");//跟templates文件夹下的test.html名字一样，返回这个界面

    }

    @RequestMapping(value="/demo",method = RequestMethod.GET)
    public ModelAndView courseClickCountStat(){

        return new ModelAndView("demo1");//跟templates文件夹下的demo.html名字一样，返回这个界面

    }

}
