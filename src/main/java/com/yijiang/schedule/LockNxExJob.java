package com.yijiang.schedule;

import com.yijiang.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;


@Service
public class LockNxExJob {

    private static final Logger logger = LoggerFactory.getLogger(LockNxExJob.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static String LOCK_PREFIX = "prefix_";


    @Scheduled(cron = "0/10 * * * * *")
    public void lockJob() {
        String lock = LOCK_PREFIX + "LockNxExJob";
        boolean nxRet = false;
        try{
            //redistemplate setnx操作
            nxRet = redisTemplate.opsForValue().setIfAbsent(lock, getHostIp());
            Object lockValue = redisService.get(lock);

            //获取锁失败
            if(!nxRet){
                String value = (String)redisService.get(lock);
                //打印当前占用锁的服务器IP
                logger.info("get lock fail,lock belong to:{}",value);
                return;
            }else{
                //获取锁成功
                logger.info("start lock lockNxExJob success");
                Thread.sleep(5000);

               /* SETEX key seconds value
                含义：
                将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
                如果 key 已经存在， SETEX 命令将覆写旧值。*/

                redisTemplate.opsForValue().set(lock,getHostIp(),3600, TimeUnit.SECONDS);
            }
        }catch (Exception e){
            logger.error("lock error",e);

        }finally {
            if(nxRet){
                redisService.remove(lock);
            }
        }
    }

    /**
     * 获取本机内网IP地址方法
     * @return
     */
   private static String getHostIp(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":")==-1){
                        return ip.getHostAddress().replaceAll("\\.", "a");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        String localIP = "";
        try {
            localIP = getHostIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取本机IP
        System.out.println(localIP);
    }




}
