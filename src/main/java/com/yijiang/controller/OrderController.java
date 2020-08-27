/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.yijiang.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年7月12日 下午3:41:02<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
@RestController
public class OrderController {
/*
	@Autowired
	private OrderMapper orderMapper;

	@RequestMapping("/getToken")
	public String getToken() {
		return TokenUtils.getToken();
	}

	// 验证Token
	@RequestMapping(value = "/addOrder", produces = "application/json; charset=utf-8")
	public String addOrder(@RequestBody OrderEntity orderEntity, HttpServletRequest request) {
		// 代码步骤：
		// 1.获取令牌 存放在请求头中
		String token = request.getHeader("token");
		if (StringUtils.isEmpty(token)) {
			return "参数错误!";
		}
		// 2.判断令牌是否在缓存中有对应的令牌
		// 3.如何缓存没有该令牌的话，直接报错（请勿重复提交）
		// 4.如何缓存有该令牌的话，直接执行该业务逻辑
		// 5.执行完业务逻辑之后，直接删除该令牌。
		if (!TokenUtils.findToken(token)) {
			return "请勿重复提交!";
		}
		int result = orderMapper.addOrder(orderEntity);
		return result > 0 ? "添加成功" : "添加失败" + "";
	}*/
}
