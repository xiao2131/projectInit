package com.yirs.Demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yirs.Demo.entity.WxUser;
import com.yirs.Demo.entity.Impl.WxUserImpl;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yirs
 * @since 2020-07-22
 */
public interface WxUserService extends IService<WxUser> {

	/**
	 * 	检验用户是否存在 存在则颁布token,未存在创建用户并颁布token
	 */
	public Object diyVerifyUser(WxUserImpl wxUserImpl);

}
