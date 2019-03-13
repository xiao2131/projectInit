package cn.yirs.Core.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yirs.Common.Entity.Employee;
import cn.yirs.Core.Dao.EmployeeMapper;
import cn.yirs.Core.Service.EmployeeService;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

	/**
	 * 第一次导入数据
	 */
	@Override
	public void insertBatch(List<Employee> list) {

		this.baseMapper.insertBatch(list);

	}

	/**
	 * 查询员工信息
	 */
	@Override
	public List<Employee> getEmployeeInfoList(int current, int number) {

		IPage<Employee> page = this.baseMapper.selectPage(new Page<Employee>(current, number),
				new QueryWrapper<Employee>());

		return page.getRecords();
	}

	/**
	 * 查询员工总数量
	 */
	@Override
	public Integer getCountEmployee() {

		return this.baseMapper.selectCount(new QueryWrapper<Employee>().select("count(id) as count"));
	}

	/**
	 * 根据姓名模糊查询
	 */
	@Override
	public List<Employee> selectEmployeeByName(String name, Integer current) {

		IPage<Employee> page = this.baseMapper.selectPage(new Page<Employee>(current, 20),
				new QueryWrapper<Employee>().like("name", name));

		return page.getRecords();
	}

}
