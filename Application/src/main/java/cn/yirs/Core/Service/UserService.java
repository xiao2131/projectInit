package cn.yirs.Core.Service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yirs.Common.Entity.Employee;
import cn.yirs.Common.Entity.User;

public interface UserService extends IService<User> {


	User getUserByName(String name);

}
