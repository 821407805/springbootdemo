package com.yijiang.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillService {

    private static final String secStartPrefix = "skuId_start_";
    private static final String secAccess = "skuId_access_";
    private static final String secCount = "skuId_count_";
    private static final String filterName = "skuId_bloomfilter_";
    private static final String bookedName = "skuId_booked_";


    @Resource
    private RedisService redisService;

    public String seckill(int uid, int skuId) {
        //流量拦截层
        //1、判断秒杀是否开始   0_1554045087    开始标识_开始时间
        String isStart = (String) redisService.get(secStartPrefix + skuId);
        if (StringUtils.isBlank(isStart)) {
            return "还未开始";
        }
        if (isStart.contains("_")) {
            Integer isStartInt = Integer.parseInt(isStart.split("_")[0]);
            Integer startTime = Integer.parseInt(isStart.split("_")[1]);
            if (isStartInt == 0) {
                if (startTime > getNow()) {
                    return "还未开始";
                } else {
                    //代表秒杀已经开始
                    redisService.set(secStartPrefix + skuId, 1+"");
                }
            } else {
                return "系统异常";
            }
        } else {
            if (Integer.parseInt(isStart) != 1) {
                return "系统异常";
            }
        }
        //流量拦截
        String skuIdAccessName = secAccess + skuId;
        Integer accessNumInt = 0;
        String accessNum = (String) redisService.get(skuIdAccessName);
        if(StringUtils.isNotBlank(accessNum)){
            accessNumInt = Integer.parseInt(accessNum);
        }
        String skuIdCountName = secCount + skuId;
        Integer countNumInt = Integer.parseInt((String) redisService.get(skuIdCountName));
        if (countNumInt * 1.2 < accessNumInt) {
            return "已抢光,欢迎下次参与";
        } else {
            redisService.incre(skuIdAccessName, 1);
        }
        //信息校验层
        if (redisService.bloomFilterExists(filterName, uid+"")){
            return "您已经抢购过该商品，请勿重复下发!";
        }else{
            redisService.bloomFilterAdd(filterName, uid+"");
        }
        // 进行秒杀库存查询与扣减，保证原子操作
        Boolean isSuccess = redisService.getAndIncrLua(bookedName+skuId);
        if(isSuccess){
            return "恭喜您抢购成功！！！";
        }else{
            return "抢购结束,欢迎下次参与";
        }
    }

    private long getNow() {
        return System.currentTimeMillis() / 1000;
    }
}
