package com.yijiang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.rmi.CORBA.ClassDesc;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author  Jasonxiao
 * @Date    2020/8/27
 * @Version 1.0
 * @Description: 
*/
@RestController
public class SessionController {

    @RequestMapping(value = "/setSession", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> setSession (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        request.getSession().setAttribute("request Url", request.getRequestURL());
        map.put("request Url", request.getRequestURL());
        return map;
    }

    @RequestMapping(value = "/getSession", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSession (HttpServletRequest request){
        Map<String, Object> map = new HashMap();
        map.put("sessionIdUrl",request.getSession().getAttribute("request Url"));
        map.put("sessionId", request.getSession().getId());
        return map;
    }
}
