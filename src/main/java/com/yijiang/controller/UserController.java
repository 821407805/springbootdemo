package com.yijiang.controller;

import com.yijiang.domain.User;
import com.yijiang.mapper.UserMapper;
import com.yijiang.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: UserController
 * @Description:
 * @Author Jason
 * @Date 2020/8/25 19:26
 */
@RestController
public class UserController {
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;

    @ResponseBody
    @RequestMapping("/getUser")
    public String getUser(int id){
        User user = userMapper.find(id);
        return user.getUserName();
    }

    @ResponseBody
    @RequestMapping("/findByIdCach")
    public String findByIdCach(int id){
        User user = userService.findById(id);
        return user.getUserName();
    }

    @ResponseBody
    @RequestMapping("/findByIdTtl")
    public String findByIdTtl(int id){
        User user = userService.findByIdTtl(id);
        return user.getUserName();
    }

}



