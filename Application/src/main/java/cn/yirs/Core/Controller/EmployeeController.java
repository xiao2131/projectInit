package cn.yirs.Core.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.yirs.Common.Dto.ResultGenerator;
import cn.yirs.Common.Entity.Employee;
import cn.yirs.Core.Service.EmployeeService;

@RestController
@RequestMapping("user")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 分页获取员信息
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("get/page/{id}")
	public Object getUserInfo(@PathVariable("id") Integer id) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("count", employeeService.getCountEmployee());

		map.put("infolist", employeeService.getEmployeeInfoList(id, 20));

		return new ResultGenerator().getSuccessResult(map);

	}

	/**
	 * 根据员工名字查找
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Object searchResultByName(@RequestParam("name") String name, @RequestParam("current") Integer current) {

		if (StringUtils.isNotBlank(name)) {

			return new ResultGenerator().getSuccessResult(employeeService.selectEmployeeByName(name, current));
		}

		return new ResultGenerator().getFailResult("请求参数不能为空", 10000);
	}

}
