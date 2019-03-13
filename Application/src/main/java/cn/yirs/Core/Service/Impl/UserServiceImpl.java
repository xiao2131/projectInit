package cn.yirs.Core.Service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yirs.Common.Dto.ResultGenerator;
import cn.yirs.Common.Entity.User;
import cn.yirs.Core.Dao.UserMapper;
import cn.yirs.Core.Service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	/**
	 * 根据用户名查询用户
	 */
	@Override
	public User getUserByName(String name) {

		return this.baseMapper.selectOne(new QueryWrapper<User>().eq("username", name));

	}

}
