/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.yijing.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年7月12日 下午10:31:57<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
public class TokenUtils {

	private static Map<String, Object> tokenMaps = new ConcurrentHashMap<String, Object>();
	// 1.什么Token（令牌） 表示是一个零时不允许有重复相同的值（临时且唯一）
	// 2.使用令牌方式防止Token重复提交。

	// 使用场景:在调用第API接口的时候，需要传递令牌,该Api接口 获取到令牌之后，执行当前业务逻辑，让后把当前的令牌删除掉。
	// 在调用第API接口的时候，需要传递令牌 建议15-2小时
	// 代码步骤：
	// 1.获取令牌
	// 2.判断令牌是否在缓存中有对应的数据
	// 3.如何缓存没有该令牌的话，直接报错（请勿重复提交）
	// 4.如何缓存有该令牌的话，直接执行该业务逻辑
	// 5.执行完业务逻辑之后，直接删除该令牌。

	// 获取令牌
	public static synchronized String getToken() {
		// 如何在分布式场景下使用分布式全局ID实现
		String token = "token" + System.currentTimeMillis();
		// hashMap好处可以附带
		tokenMaps.put(token, token);
		return token;
	}

	public static boolean findToken(String tokenKey) {
		// 判断该令牌是否在tokenMap 是否存在
		String token = (String) tokenMaps.get(tokenKey);
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		// token 获取成功后 删除对应tokenMapstoken
		tokenMaps.remove(token);
		return true;
	}
}
