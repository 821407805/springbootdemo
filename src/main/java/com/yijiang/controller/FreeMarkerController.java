package com.yijiang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.Name;

/**
 * @author yi
 * @date 2020/6/8 18:22
 */
@Controller
@RequestMapping("/freemarker")
public class FreeMarkerController {
    @Value("${server.port}")
    private String server;
    @RequestMapping("/hello")
    public String hello(ModelMap map, String nikeName){
        map.addAttribute("sex", "男");
        map.addAttribute("nickName", nikeName);
        map.addAttribute("server", server);
        System.out.println("nikeNamen传入是：" +  nikeName);
        return "fm/index";
    }
}
