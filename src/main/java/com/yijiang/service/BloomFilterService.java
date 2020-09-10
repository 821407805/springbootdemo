package com.yijiang.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.yijiang.domain.SysUser;
import com.yijiang.domain.SysUserExample;
import com.yijiang.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Author  Jasonxiao
 * @Date    2020/9/10
 * @Version 1.0
 * @Description: 谷歌布隆过滤器
*/
@Service
public class BloomFilterService {

    @Resource
    private SysUserMapper sysUserMapper;

    private BloomFilter<Integer> bf;

    /***
     * @PostConstruct 注解好多人以为是Spring提供的。其实是Java自己的注解。
     * Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。
     * 通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序：
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void initBloomFilter() {
        SysUserExample sysUserExample = new SysUserExample();
        List<SysUser> sysUserList = sysUserMapper.selectByExample(sysUserExample);
        if(CollectionUtils.isEmpty(sysUserList)){
            return;
        }
        //创建谷歌布隆过滤器(默认3%误差)
        bf = BloomFilter.create(Funnels.integerFunnel(),sysUserList.size());
        for (SysUser sysUser:sysUserList) {
            bf.put(sysUser.getId());
        }
    }

    /***
     * 判断id可能存在于布隆过滤器里面
     * @param id
     * @return
     */
    public boolean userIdExists(int id){
        return bf.mightContain(id);
    }

}
