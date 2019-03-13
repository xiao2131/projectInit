package cn.yirs.Core.Service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yirs.Common.Entity.Employee;

public interface EmployeeService extends IService<Employee> {

	/**
	 * 第一次导入数据
	 */
	public void insertBatch(List<Employee> list);

	/**
	 * 查询员工信息
	 */
	public List<Employee> getEmployeeInfoList(int current, int number);

	/**
	 * 查询员工总数量
	 */
	public Integer getCountEmployee();

	/**
	 * 根据姓名模糊查询
	 */
	public List<Employee> selectEmployeeByName(String name,Integer current);

}
