package cn.yirs.Core.Dao;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.yirs.Common.Entity.Employee;

public interface EmployeeMapper extends BaseMapper<Employee> {

	/**
	 * 批量增加员工
	 */
	public void insertBatch(List<Employee> Employee);

}
