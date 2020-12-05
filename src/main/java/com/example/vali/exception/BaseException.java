package com.example.vali.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class BaseException {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("BaseController defaultErrorHandler()......");
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        /**
         * 对异常处理的结果，返回一个error的页面(填的是映射路径)
         */
        mav.setViewName("ExceptionError");
        return mav;
    }

}
