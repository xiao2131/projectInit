package com.yirs.Demo.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.Utils.HttpUtils;
import com.yirs.Demo.Utils.RedisUtil;
import com.yirs.Demo.config.TokenConfig;
import com.yirs.Demo.entity.School;
import com.yirs.Demo.mapper.SchoolMapper;
import com.yirs.Demo.service.SchoolService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yirs
 * @since 2020-07-21
 */
@Service
@SuppressWarnings("unchecked")
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private TokenConfig tokenConfig;
	/**
	 * 更新招生院校信息
	 */
	@Override
	public boolean upadteSchoolInfo() {

		// 清除院校表数据
		this.baseMapper.diyTrunCateFormSchool();
		// 发送psot请求
		String uri = "https://www.eeagd.edu.cn/bcks/ybmks/queryzy.jsp";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ksklh", "1");
		// 获取初始化网页源码
		String str = null;
		try {
			str = HttpUtils.post(uri, null, parameters, null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		str = str.replaceAll("\r|\n", "");
		str = str.replaceAll("\\s{2,}", "");
		str = str.replaceAll("&nbsp;", " ");
		// 获取招生院校名称和专业 Start
		String pattern = "<table id=\"customers\">(.*)</table>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);

		if (m.find()) {
			str = m.group(1);
			pattern = "<tr>(.*)</tr>";
			r = Pattern.compile(pattern);
			m = r.matcher(str);
			if (m.find()) {
				str = m.group(0);
			}
		}
		// 消除无用字符
		str = str.substring(0, str.length() - 18);

		// 处理tr字符串
		String[] arr1 = str.split("<tr>|</tr>");// 不能直接用".",因为匹配的是任意字符，如果要使用格式就写为"//."
		List<String> trlist = new LinkedList<String>();
		for (String string : arr1) {
			if (StringUtils.isNotBlank(string)) {
				trlist.add(string);
			}
		}
		// 处理<a>字符串
		pattern = "<a (.*)>(.*)</a>";
		r = Pattern.compile(pattern);
		List<String> tdlist = new LinkedList<String>();
		for (String tdStr : trlist) {
			// 分割td字符串
			String[] tdArr = tdStr.split("<td>|</td>");
			for (String aStr : tdArr) {
				// 去除空白字符
				if (StringUtils.isNotBlank(aStr)) {
					if (aStr.contains("<a")) {
						m = r.matcher(aStr);
						if (m.find()) {
							tdlist.add(m.group(2));
							continue;
						}
					}
					tdlist.add(aStr);
				}
			}
		}

		int index;
		School scholl = new School();
		List<School> schollList = new LinkedList<School>();
		//tdlist就是已经处理好的数据
		for (int i = 1; i <= tdlist.size(); i++) {
			index = i % 7;
			switch (index) {
			case 1:
				String[] schoolArr=tdlist.get(i).split(" ");
				scholl.setSchoolId(schoolArr[0]);
				scholl.setSchoolName(schoolArr[1]);
				break;
			case 2:
				scholl.setSchoolMajor(tdlist.get(i));
				break;
			case 4:
				scholl.setNumber(tdlist.get(i));
				break;
			case 5:
				scholl.setSite(tdlist.get(i));
				break;
			default:
				break;
			}
			if (index == 0) {
				schollList.add(scholl);
				scholl = new School();
			}
		}
		return this.saveBatch(schollList);
	}

	/**
	 * 	获取招生学校名称(初始化获取学校专业和地点)
	 */
	@Override
	public Object diyGetListSchoolName() {
		
		//缓存获取
		Map<String,Object> stroeMap=(Map<String, Object>) redisUtil.get("getInitSchoolData");
		
		if(stroeMap!=null) {
			return ResultGenerator.getSuccessResult(stroeMap);
		}
		
		//查询学校名称
		//select school_id,school_name from school group by school_name order by school_id ASC
		List<School> schoolList=this.baseMapper.selectList(new QueryWrapper<School>()
				.groupBy("school_name")
				.orderByAsc("school_id")
				.select("school_id","school_name"));
		
		//查询第一个学校的招生专业
		List<School> majorList=this.baseMapper.diyGetListMajorBySchoolId(schoolList.get(0).getSchoolId());
		//对应的地址
		List<School> siteList=this.baseMapper.diyGetSchoolSiteById(majorList.get(0).getId());
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("schoolList",schoolList);
		map.put("majorList",majorList);
		map.put("siteList",siteList);
		
		if(redisUtil.set("getInitSchoolData", map, tokenConfig.getExpireTime()*60*24))
			return ResultGenerator.getSuccessResult(map);
		
		return ResultGenerator.getFailResult("80001");
	}
	/**
	 * 	获取学校专业(根据学校Id)
	 * 	users.forEach(System.out::println);
	 */
	@Override
	public Object diyGetSchoolMajorBySchoolId(String schoolId) {
		
		//缓存获取
		List<School> stroeList=(List<School>) redisUtil.get("getSchoolMajor_"+schoolId);
		
		if(stroeList!=null) {
			return ResultGenerator.getSuccessResult(stroeList);
		}
		
		//根据学校id查询专业
		//select school_major,site from school where school_id="10571"
		List<School> list=this.baseMapper.diyGetListMajorBySchoolId(schoolId);
		if(redisUtil.set("getSchoolMajor_"+schoolId, list, tokenConfig.getExpireTime()*60*24))
			return ResultGenerator.getSuccessResult(list);

		return ResultGenerator.getFailResult("80001");
	}
	
	
	/**
	 * 	获取学校地址(根据专业Id)
	 */
	@Override
	public Object diyGetSchoolSiteById(Integer id) {
		
//		//缓存获取
		List<School> stroeList=(List<School>) redisUtil.get("getSchoolSite_"+id);
//		
		if(stroeList!=null) {
			return ResultGenerator.getSuccessResult(stroeList);
		}
		
		
		//select site from school where id=1
		List<School> school=this.baseMapper.diyGetSchoolSiteById(id);
		
		if(school!=null) {
			if(redisUtil.set("getSchoolSite_"+id, school, tokenConfig.getExpireTime()*60*24))
				return ResultGenerator.getSuccessResult(school);
			
			return ResultGenerator.getFailResult("80001");
		}
		//	专业id不存在
		return ResultGenerator.getFailResult("10004");
	}
	
	
	
	

}
