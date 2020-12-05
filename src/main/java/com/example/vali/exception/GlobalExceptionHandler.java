package com.example.vali.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ControllerAdvice默认拦截本工程的所有外抛异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 针对异常处理的方法
     * ResponseBody转换异常的返回值为json
     */
    @ExceptionHandler(value = Exception.class)
    /**
     * 对异常处理的返回结果直接返回一个json格式的数据
     */
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        System.out.println("GlobalExceptionHandler globalErrorHandler()......");
        // 创建返回对象Map并设置属性，会被@ResponseBody注解转换为JSON返回
        Map<String, Object> map = new HashMap<>();
        map.put("code", 100);
        map.put("message", e.getMessage());
        map.put("url", request.getRequestURL().toString());
        map.put("data", "请求失败");
        return map;
    }
}
