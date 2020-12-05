package com.example.vali.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
//    要打包成jar包，一些静态资源通过webjar的方式引入（springmvc通过这个路径访问，classpath:/META-INFO/resources/webjars），
//    放在pom引入，最后直接放在lib中

//    @RequestMapping("/indexPage")
////    thymeleaf默认前缀"classpath:/templates/，后缀"html"
//    public String index(Model model){
//        System.out.println("调用起始页");
//        System.out.println("haha");
//        return "index";
//    }

    @RequestMapping("/globalExceptionTest")
//    thymeleaf默认前缀"classpath:/templates/，后缀"html"
    public String t(Model model){
        System.out.println("调用全局异常处理");
        throw new RuntimeException();
    }

}
