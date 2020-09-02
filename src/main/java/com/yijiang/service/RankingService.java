package com.yijiang.service;

import com.yijiang.domain.User;
import com.yijiang.domain.UserScore;
import com.yijiang.domain.UserScoreExample;
import com.yijiang.mapper.UserMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RankingService implements InitializingBean {


    private static final String RANKGNAME = "user_score";

    private static final String SALESCORE = "sale_score_rank:";

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;



    public Boolean zAdd(String uid, Integer score) {

        return redisService.zAdd(RANKGNAME, uid, score);
    }

    public void increSocre(String uid, Integer score) {

        redisService.incrementScore(RANKGNAME, uid, score);
    }

    public Long rankNum(String uid) {
        return redisService.zRank(RANKGNAME, uid);
    }

    public Long score(String uid) {
        Long score = redisService.zSetScore(RANKGNAME, uid).longValue();
        return score;
    }

    public Set<ZSetOperations.TypedTuple<Object>> rankWithScore(Long start, Long end) {
        return redisService.zRankWithScore(RANKGNAME, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(Long start, Long end) {
        return redisService.reverseZRankWithRank(RANKGNAME, start, end);
    }

    public Map<String, Object> userRank(String uid, String name) {
        Map<String, Object> retMap = new LinkedHashMap<>();
        String key = uid + ":" + name;
        Integer rank = redisService.zRank(SALESCORE, key).intValue();
        Long score = redisService.zSetScore(SALESCORE, key).longValue();
        retMap.put("userId", uid);
        retMap.put("score", score);
        retMap.put("rank", rank);
        return retMap;
    }


    public List<Map<String, Object>> reverseZRankWithRank(long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> setObj = redisService.reverseZRankWithRank(SALESCORE, start, end);
        List<Map<String, Object>> mapList = setObj.stream().map(objectTypedTuple -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("userId", objectTypedTuple.getValue().toString().split(":")[0]);
            map.put("userName", objectTypedTuple.getValue().toString().split(":")[1]);
            map.put("score", objectTypedTuple.getScore());
            return map;
        }).collect( Collectors.toList() );
        return mapList;
    }

    public List<Map<String, Object>> saleRankWithScore(Integer start, Integer end) {
        Set<ZSetOperations.TypedTuple<Object>> setObj = redisService.reverseZRankWithScore(SALESCORE, start, end);
        List<Map<String, Object>> mapList = setObj.stream().map(objectTypedTuple -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("userId", objectTypedTuple.getValue().toString().split(":")[0]);
            map.put("userName", objectTypedTuple.getValue().toString().split(":")[1]);
            map.put("score", objectTypedTuple.getScore());
            System.out.println();
            return map;
        }).collect(Collectors.toList());
        return mapList;
    }
    public void rankSaleAdd() {
        UserScoreExample example = new UserScoreExample();
        example.setOrderByClause("id desc");
        List<User> query = userMapper.queryAll();
        query.forEach(user -> {
            String key = user.getId() + ":" + user.getUserName();
            redisService.zAdd(SALESCORE, key, 10);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("======enter init bean=======");
        // 预热数据，将db查询出的数据缓存到redis中
        this.rankSaleAdd();
    }
//    实现 ApplicationRunner接口
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("======enter run bean=======");
//        Thread.sleep(100000);
//        this.rankSaleAdd();
//    }
}
