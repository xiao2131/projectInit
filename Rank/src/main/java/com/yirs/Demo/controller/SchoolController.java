package com.yirs.Demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.annotation.Authority;
import com.yirs.Demo.service.SchoolService;

/**
 * @author Yirs
 * @describe 学校api
 * @Date 2020年8月5日
 */
@RestController
@RequestMapping("/v1/api/school")
public class SchoolController {

	@Autowired
	private SchoolService schoolService;

	/**
	 * 	获取招生学校名称
	 */
	@Authority
	@GetMapping("/getSchoolName")
	public Object getListSchoolName() {
		return schoolService.diyGetListSchoolName();
	}

	/**
	 * 	获取学校专业(根据学校Id)
	 */
	@GetMapping("/getSchoolMajor/{schoolId}")
	public Object getSchoolMajorBySchoolId(@PathVariable("schoolId") String schoolId) {
		
		if (StringUtils.isNotBlank(schoolId) && schoolId.length() == 5)
			return schoolService.diyGetSchoolMajorBySchoolId(schoolId);
		// 10002 值不符合规定
		return ResultGenerator.getFailResult("10002");
	}
	
	/**
	 * 	获取学校地址(根据专业Id)
	 */
	@GetMapping("/getSchoolSite/{id}")
	public Object getSchoolSiteById(@PathVariable("id") Integer id) {
		
		if (id!=null&&id>0)
			return schoolService.diyGetSchoolSiteById(id);
		
		// 参数值未输入
		return ResultGenerator.getFailResult("10003");
	}
	


}
