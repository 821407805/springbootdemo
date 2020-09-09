package com.yijiang.controller;

import com.yijiang.service.RankingService;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RankingController {


    @Resource
    private RankingService rankingService;


    @RequestMapping("/zAdd")
    @ResponseBody
    public Boolean zAdd(String uid, Integer score) {
        return rankingService.zAdd(uid, score);
    }

    @RequestMapping("/zRankWithScore")
    @ResponseBody
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(Long begin, Long end) {

        return rankingService.rankWithScore(begin, end);
    }
    /**
     * @author jasonxiao
     * @description 从高到低排序，取前三名
     * @date 2020/8/28
     * @param 
     * @return 
     */
    @RequestMapping("/reverseZRankWithRank")
    @ResponseBody
    public List<ZSetOperations.TypedTuple<Object>>  reverseZRankWithRank(Long begin, Long end) {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = rankingService.reverseZRankWithRank(begin, end);
        List<ZSetOperations.TypedTuple<Object>> list = typedTuples.stream().limit(3).collect(Collectors.toList());
        return list;
    }

    @RequestMapping("/increScore")
    @ResponseBody
    public String increScore(String uid, Integer score) {
        rankingService.increSocre(uid, score);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/rank")
    public Map<String, Long> rank(String uid) {
        Map<String, Long> map = new HashMap<>();
        map.put(uid, rankingService.rankNum(uid));
        return map;
    }

    @ResponseBody
    @RequestMapping("/score")
    public Long rankNum(String uid) {
        return rankingService.score(uid);
    }

    @ResponseBody
    @RequestMapping("/scoreByRange")
    public Set<ZSetOperations.TypedTuple<Object>> scoreByRange(Long start, Long end) {
        return rankingService.rankWithScore(start,end);
    }


    @ResponseBody
    @RequestMapping("/sale/userScore")
    public Map<String,Object> userScore(String uid,String name) {
        return rankingService.userRank(uid,name);
    }

    @ResponseBody
    @RequestMapping("/sale/top")
    public List<Map<String,Object>> reverseZRankWithRank(long start,long end) {
        return rankingService.reverseZRankWithRank(start,end);
    }


    @ResponseBody
    @RequestMapping("/sale/scoreByRange")
    public List<Map<String,Object>> saleScoreByRange(Integer start, Integer end) {
        return rankingService.saleRankWithScore(start,end);
    }

}
