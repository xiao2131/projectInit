package com.yirs.Demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 设置Token过期时间
 */

@Component
public class TokenConfig {

	@Value("${token.expireTime}")
	private int expireTime;

	public int getExpireTime() {
		return expireTime * 60;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

}
