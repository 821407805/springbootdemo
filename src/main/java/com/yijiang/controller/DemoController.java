package com.yijiang.controller;

import com.yijiang.service.RedisService;
import com.yijing.utils.HttpDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author jasonxiao
 */
@RestController
public class DemoController {

    @Resource
    RedisService redisService;

    /*@ResponseBody
    @RequestMapping("/redis/GetSet")
    public String redisGetAndSet(String name, String value ){
        redisTemplate.opsForValue().set(name, value);
        System.out.println("---------------------------------");
        return (String) redisTemplate.opsForValue().get(name);
    }*/

    @ResponseBody
    @RequestMapping("/redis/GetAndSet")
    public String redisGetAndSet2(String name, String value ){

        redisService.set(name, value);
        return (String) redisService.get(name);
    }

    @RequestMapping("/api/{name}")
    public String filterTest( @PathVariable String name ){
        HttpDeal.get("https://www.baidu.com/");
        System.out.println("^^^^^^^^^^^^^^^^访问的是/api/xxxxx");
        return "^^^^^^^^^^^^^^^^访问的是/api:" + name;
    }

    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        HttpDeal.get("https://www.baidu.com/");
        return String.format("**********Hello %s!", name);
    }

    @GetMapping("/student")
    public String student( @RequestBody Student student) {
        return String.format("Hello Hello %s!", student.getName());
    }
}
