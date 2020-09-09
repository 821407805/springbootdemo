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
    /**
     * @author jasonxiao
     * @description  谷歌布隆过滤器
     * @date 2020/9/9
     * @param 
     * @return 
     */
    @RequestMapping("/bloom/idExists")
    public boolean ifExists(int id){
        return bloomFilterService.userIdExists(id);
    }
    /**
     * @author jasonxiao
     * @description  redis 隆过滤器 add
     * @date 2020/9/9
     * @param
     * @return
     */
    @RequestMapping("/bloom/redisIdAdd")
    public boolean redisidAdd(String key, String value){
        return redisService.bloomFilterAdd(key, value);
    }
    /**
     * @author jasonxiao
     * @description  redis 隆过滤器 exiests
     * @date 2020/9/9
     * @param
     * @return
     */
    @RequestMapping("/bloom/redisIdExists")
    public boolean redisidExists(String key, String value){
        return redisService.bloomFilterExists(key, value);
    }

}











