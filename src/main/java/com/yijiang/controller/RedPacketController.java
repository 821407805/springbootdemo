package com.yijiang.controller;


import com.yijiang.domain.RedPacketInfo;
import com.yijiang.domain.RedPacketRecord;
import com.yijiang.mapper.RedPacketInfoMapper;
import com.yijiang.mapper.RedPacketRecordMapper;
import com.yijiang.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
/**
 * @Author  Jasonxiao
 * @Date    2020/9/9
 * @Version 1.0
 * @Description: 
*/
@RestController
public class RedPacketController {


    @Autowired
    private RedisService redisService;

    @Autowired
    RedPacketInfoMapper redPacketInfoMapper;


    @Autowired
    private RedPacketRecordMapper redPacketRecordMapper;

    private static final String TOTAL_NUM = "_totalNum";
    private static final String TOTAL_AMOUNT = "_totalAmount";

    /**
     * @param uid
     * @param totalNum
     * @return
    */
    @ResponseBody
    @RequestMapping("/addPacket")
    public String saveRedPacket(Integer uid, Integer totalNum, Integer totalAmount) {
        RedPacketInfo record = new RedPacketInfo();
        record.setUid(uid);
        record.setTotalAmount(totalAmount);
        record.setTotalPacket(totalNum);
        record.setCreateTime(new Date());
        record.setRemainingAmount(totalAmount);
        record.setRemainingPacket(totalNum);
        //此时无法保证红包id唯一，最好是用雪花算法进行生成分布式系统唯一键
        long redPacketId = System.currentTimeMillis();
        record.setRedPacketId(redPacketId);
        redPacketInfoMapper.insert(record);
        redisService.set(redPacketId + "_totalNum", totalNum + "");
        redisService.set(redPacketId + "_totalAmount", totalAmount + "");
        return "success";
    }


    /**
     * 抢红包
     *
     * @param redPacketId
     * @return
    */
    @ResponseBody
    @RequestMapping("/getPacket")
    public Integer getRedPacket(long redPacketId) {
        //TODO 将无效的请求过滤掉（抢红包时记录请求个数>红包个数*2时直接返回红包已抢完），实际进入到后台的量不大。cache记录红包个数，原子操作进行个数递减，到0表示被抢光
        String redPacketName = redPacketId + TOTAL_NUM;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isNotBlank(num)) {
            return Integer.parseInt(num);
        }
        return 0;
    }

    /**
     * 拆红包
     *
     * @param redPacketId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRedPacketMoney")
    public String getRedPacketMoney(int uid, long redPacketId) {
        Integer randomAmount = 0;
        String redPacketName = redPacketId + TOTAL_NUM;
        String totalAmountName = redPacketId + TOTAL_AMOUNT;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isBlank(num) || Integer.parseInt(num) == 0) {
            return "抱歉！红包已经抢完了";
        }
        String totalAmount = (String) redisService.get(totalAmountName);
        if (StringUtils.isNotBlank(totalAmount)) {
            Integer totalAmountInt = Integer.parseInt(totalAmount);
            Integer totalNumInt = Integer.parseInt(num);
            Integer maxMoney = totalAmountInt / totalNumInt * 2;
            Random random = new Random();
            randomAmount = random.nextInt(maxMoney);
        }
        //TODO lua脚本将这两个命令一起请求
        redisService.incre(redPacketName, -1);
        redisService.incre(totalAmountName,-randomAmount);
        updateRacketInDB(uid, redPacketId,randomAmount);
        return randomAmount + "";
    }

    public void updateRacketInDB(int uid, long redPacketId, int amount) {
        RedPacketRecord redPacketRecord = new RedPacketRecord();
        redPacketRecord.setUid(uid);
        redPacketRecord.setRedPacketId(redPacketId);
        redPacketRecord.setAmount(1111);
        redPacketRecord.setCreateTime(new Date());
        redPacketRecordMapper.insertSelective(redPacketRecord);
        //这里应该查出RedPacketInfo的数量，将总数量和总金额减去
    }

}
