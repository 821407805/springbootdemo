package com.yijiang.test;

import com.yijiang.DemoApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author yi
 * @date 2020/6/5 9:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class}) //启动整个springBoot工程
@AutoConfigureMockMvc
public class StringBootDemoTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public  void testTwo(){
        System.out.printf("springBoot test %s", " two");
        System.out.println(  );
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hello").param("name", "test****"))
                    .andExpect( MockMvcResultMatchers.status().isOk() ).andReturn();
            //int status = mvcResult.getResponse().getStatus();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            System.out.println("****"+contentAsString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOne(){
        String.format("springBoot test %s", " one");
        System.out.printf("springBoot test %s", " one");
        Assert.assertEquals("错误信息",1, 1);
    }

    @Test
    public void testThree(){
        /*Boolean result = redisTemplate.opsForValue().setIfAbsent("test001", "ddddddd");
        System.out.println(result+"33333333333333333333333333");*/
        redisTemplate.opsForValue().set("test005","ddddddd",10, TimeUnit.SECONDS);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
