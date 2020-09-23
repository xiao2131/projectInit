package com.yirs.Demo.service;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.entity.School;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yirs
 * @since 2020-07-21
 */
public interface SchoolService extends IService<School> {

	/**
	 * 添加招生院校信息
	 * 
	 * @param list
	 * @return
	 */
	public boolean upadteSchoolInfo();

	/**
	 * 	获取招生学校名称
	 */
	public Object diyGetListSchoolName();
	
	/**
	 * 	获取学校专业(根据学校Id)
	 */
	public Object diyGetSchoolMajorBySchoolId(String schoolId);
	
	/**
	 * 	获取学校地址(根据专业Id)
	 */
	public Object diyGetSchoolSiteById(Integer id);
	
}
