package com.yijiang.controller;


import com.yijiang.service.BloomFilterService;
import com.yijiang.service.RedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BloomFilterController {

    @Resource
    private BloomFilterService bloomFilterService;

    @Resource
    private RedisService redisService;

    @RequestMapping("/bloom/idExists")
    public boolean ifExists(int id){
        return bloomFilterService.userIdExists(id);
    }

    @RequestMapping("/bloom/redisIdAdd")
    public boolean redisidAdd(String key, String value){
        return redisService.bloomFilterAdd(key, value);
    }

    @RequestMapping("/bloom/redisIdExists")
    public boolean redisidExists(String key, String value){
        return redisService.bloomFilterExists(key, value);
    }

}











