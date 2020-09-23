package com.yirs.Demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yirs.Demo.entity.School;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yirs
 * @since 2020-07-21
 */
public interface SchoolMapper extends BaseMapper<School> {

	/**
	 * 	清空school表数据Truncate 
	 */
	@Update("TRUNCATE table school")
	public void diyTrunCateFormSchool();

	/**
	 * 	根据学校id获取学校专业 
	 * 	as school_name是因为微信小程序picker的关系，暂时需要转换为school_name
	 */
	@Select("select id,school_major as school_name,school_id from school where school_id=#{schoolId}")
	public List<School> diyGetListMajorBySchoolId(@Param("schoolId") String schoolId);

	/**
	 * 	 根据专业id获取学校专业
	 *	 as school_name是因为微信小程序picker的关系，暂时需要转换为school_name
	 */
	@Select("select site as school_name from school where id=#{id}")
	public List<School> diyGetSchoolSiteById(@Param("id") Integer id);

}
