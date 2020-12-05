package com.example.vali.exception;

import com.example.vali.exception.BaseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ParentExceptionTestController extends BaseException {

    @RequestMapping("/parentExceptionTest")
    public String hello() throws Exception {
        System.out.println("调用继承父类的异常处理");
        throw new Exception();
    }

    @RequestMapping("/ExceptionError")
    public String s(){
        return "error";
    }

}