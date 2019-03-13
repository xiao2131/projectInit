package cn.yirs.Core.Service.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yirs.Common.Entity.Employee;
import cn.yirs.Core.Dao.EmployeeMapper;
import cn.yirs.Core.Service.CallbackService;
import cn.yirs.Utils.CallBackEvent;
import cn.yirs.Utils.Constant;
import cn.yirs.Utils.DingTalkUtils;

@Service
public class CallbackServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements CallbackService {

	/**
	 * 通讯录监听事件
	 */
	@Override
	public String BookListener(String plainText) {

		JSONObject obj = JSON.parseObject(plainText);

//		System.out.println(obj);

		// 回调事件
		String eventType = obj.getString("EventType");

		List<String> userIdList = null;

		if (StringUtils.isNotBlank(eventType)) {

			switch (eventType) {
			case CallBackEvent.USER_ADD_ORG: // 添加事件
				userIdList = JSON.parseArray(obj.getString("UserId"), String.class);
				this.addUser(userIdList);
				break;

			case CallBackEvent.USER_LEAVE_ORG: // 删除事件
				userIdList = JSON.parseArray(obj.getString("UserId"), String.class);
				this.deleteUser(userIdList);
				break;

			case CallBackEvent.USER_MODIFY_ORG: // 修改事件
				this.updateUser(userIdList);
				break;

			default:
				break;
			}

			return Constant.CALLBACK_RESPONSE_SUCCESS;

		}

		return Constant.CALLBACK_RESPONSE_FAIL;

	}

	/**
	 * 增加用户
	 */
	public void addUser(List<String> userIdList) {

		String accessToken = DingTalkUtils.getAccessToken();

		if (userIdList.size() > 0) {
			for (String string : userIdList) {
				List<Employee> list = DingTalkUtils.getHrmEmployeeList(accessToken, string);
				this.baseMapper.insertBatch(list);
			}
		}

	}

	/**
	 * 删除用户
	 */
	public void deleteUser(List<String> userIdList) {

		if (userIdList.size() > 0) {
			for (String string : userIdList) {
				this.baseMapper.delete(new QueryWrapper<Employee>().eq("user_id", string));
			}
		}

	}

	/**
	 * 更新用户
	 */
	public void updateUser(List<String> userIdList) {

		String accessToken = DingTalkUtils.getAccessToken();

		Employee employee = null;

		if (userIdList.size() > 0) {
			for (String string : userIdList) {
				List<Employee> list = DingTalkUtils.getHrmEmployeeList(accessToken, string);
				employee = new Employee();
				employee.setAddress(list.get(0).getAddress());
				employee.setBirthTime(list.get(0).getBirthTime());
				employee.setGraduateSchool(list.get(0).getGraduateSchool());
				employee.setGraduationTime(list.get(0).getGraduationTime());
				employee.setHighestEdu(list.get(0).getHighestEdu());
				employee.setMajor(list.get(0).getMajor());
				employee.setMarriage(list.get(0).getMarriage());
				employee.setMobile(list.get(0).getMobile());
				employee.setName(list.get(0).getName());
				employee.setSexType(list.get(0).getSexType());
				this.baseMapper.update(employee, new UpdateWrapper<Employee>().eq("user_id", string));
			}
		}

	}
}
