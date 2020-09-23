package com.yirs.Demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.Utils.HttpUtils;
import com.yirs.Demo.Utils.RedisUtil;
import com.yirs.Demo.Utils.TokenUtil;
import com.yirs.Demo.config.TokenConfig;
import com.yirs.Demo.entity.WxUser;
import com.yirs.Demo.entity.Impl.WxUserImpl;
import com.yirs.Demo.mapper.WxUserMapper;
import com.yirs.Demo.service.WxUserService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yirs
 * @since 2020-07-22
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

	@Autowired
	private TokenConfig tokenConfig;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 	检验用户是否存在 存在则颁布token,未存在创建用户并颁布token
	 */
	@Override
	public Object diyVerifyUser(WxUserImpl wxUserImpl) {

		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", "wx9d06237fd5318185");
		map.put("secret", "1c26d6c2c5e12f896b929a0bd9d71e23");
		map.put("js_code", wxUserImpl.getCode());
		map.put("grant_type", "authorization_code");

		JSONObject openIdResult = JSON.parseObject(HttpUtils.get(url, null, map));

		String openId = openIdResult.getString("openid");

		if (StringUtils.isNotBlank(openId)) {

			// 数据库查找用户
			WxUser wxUser = this.getById(openId);

			// 用户不存在则创建用户
			if (wxUser == null) {
				wxUser = new WxUser();
				wxUser.setOpenid(openId);
				wxUser.setAvatarUrl(wxUserImpl.getAvatarUrl());
				wxUser.setNickName(wxUserImpl.getNickName());
				this.baseMapper.insert(wxUser);
			}
			String token = TokenUtil.getToken(openId, tokenConfig.getExpireTime());
			if (redisUtil.set(token, wxUser, tokenConfig.getExpireTime())) {
				Map<String,Object> resultMap=new HashMap<String, Object>();
				//因为不需要将openid传到前端去
				wxUser.setOpenid(null);
				resultMap.put("token", token);
				resultMap.put("userInfo", wxUser);
				return ResultGenerator.getSuccessResult(resultMap);
			}

			// redis写入异常 可能未开启redis
			return ResultGenerator.getFailResult("80001");

		}

		// 非法code10003
		return ResultGenerator.getFailResult("10003");
	}

}
