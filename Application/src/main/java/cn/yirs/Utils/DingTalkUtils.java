package cn.yirs.Utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.request.OapiCallBackRegisterCallBackRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeListRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQuerydimissionRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQueryonjobRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.dingtalk.api.response.OapiCallBackRegisterCallBackResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse.EmpFieldInfoVO;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse.EmpFieldVO;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse.Paginator;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQueryonjobResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQueryonjobResponse.PageResult;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.taobao.api.ApiException;

import cn.yirs.Common.Entity.Employee;

public class DingTalkUtils {

	// 获取token的URL
	private static final String TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

	// 获取离职员工的URL
	private static final String MISSION_EMPLOYEE_URL = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/querydimission";

	// 获取在职员工的URL
	private static final String JOB_EMPLOYEE_URL = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/queryonjob";

	// 获取花名册的URL
	private static final String HRM_EMPLOYEE = "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/list";

	// 企业 ID ding59096e3c3fb24830
	private static final String APPKEY = "dingjtmz2elxqfr37xzw";

	// 企业 密钥 idnmnVlbj8ncGCM6F5gRm0jTz1iuJ4rvjNozLGUzI2tuMwgaYuqB6UoKumMzHKdf
	private static final String APPSECRET = "2xBVqWVia3K89fCZLX6gWcrpp4BW7scC2nBQPEWLHHFYJK1K2AgyLAGCalgC4Ioz";

	// 回调注册url
	private static final String CALLBACK_URL = "https://oapi.dingtalk.com/call_back/register_call_back";

	/**
	 * 获取AccessToken
	 */
	public static String getAccessToken() {

		DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");

		OapiGettokenRequest request = new OapiGettokenRequest();

		request.setAppkey(APPKEY);

		request.setAppsecret(APPSECRET);

		request.setHttpMethod("GET");

		OapiGettokenResponse response = null;

		try {
			response = client.execute(request);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.getAccessToken();

	}

	/**
	 * 获取在职员工id
	 */
	public static List<List<String>> getJobEmployeeId(String accessToken) {

		List<List<String>> list = new LinkedList<List<String>>();

		Long offSet = 0l;

		PageResult pageResult = null;

		do {

			pageResult = getMyNextCusrsor(offSet, accessToken);

			if (pageResult.getNextCursor() != null) {

				list.add(pageResult.getDataList());

				offSet = pageResult.getNextCursor();

				System.out.println("当前坐标是" + offSet);

			}

		} while (pageResult.getNextCursor() != null);

		return list;

	}

	// 循环获取在在职员工id
	public static PageResult getMyNextCusrsor(long offset, String accessToken) {

		DingTalkClient client = getDingTalkClient(JOB_EMPLOYEE_URL);

		OapiSmartworkHrmEmployeeQueryonjobRequest req = new OapiSmartworkHrmEmployeeQueryonjobRequest();

		req.setStatusList("2,3");

		req.setOffset(offset);

		req.setSize((long) 20);

		OapiSmartworkHrmEmployeeQueryonjobResponse res = null;
		try {
			res = client.execute(req, accessToken);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		PageResult pageResult = res.getResult();

		return pageResult;

	}

	/**
	 * 获取花名册信息 部门 姓名
	 */
	public static List<Employee> getHrmEmployeeList(String acckeyToken, String userIdList) {

		DingTalkClient client = new DefaultDingTalkClient(HRM_EMPLOYEE);

		OapiSmartworkHrmEmployeeListRequest req = new OapiSmartworkHrmEmployeeListRequest();

		req.setUseridList(userIdList);

		req.setFieldFilterList(
				"sys02-birthTime,sys02-sexType,sys02-marriage,sys02-address,sys03-major,sys00-name,sys00-mobile,sys03-highestEdu,sys03-graduateSchool,sys03-graduationTime");

		OapiSmartworkHrmEmployeeListResponse rsp = null;
		try {
			rsp = client.execute(req, acckeyToken);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		List<Employee> dingList = new LinkedList<Employee>();

		Employee dingEmployee = null;

		// 得到结果集
		List<EmpFieldInfoVO> list = rsp.getResult();

		EmpFieldVO o = new EmpFieldVO();

		// 循环结果集
		for (int i = 0; i < list.size(); i++) {

			// 自定义自己的需要字段的员工类
			dingEmployee = new Employee();

			// 迭代器结果集
			Iterator<EmpFieldVO> iterator = list.get(i).getFieldList().iterator();

			// 判断是否存在下一个
			while (iterator.hasNext()) {

				// 得到钉钉员工类
				o = iterator.next();

				// 如果是姓名
				if (o.getFieldCode().equals("sys02-birthTime")) {
					dingEmployee.setBirthTime(o.getValue());
				} else if (o.getFieldCode().equals("sys02-sexType")) {
					if (o.getValue() != null) {
						if (o.getValue().equals("1")) {
							dingEmployee.setSexType("女");
						} else {
							dingEmployee.setSexType("男");
						}
					}

				} else if (o.getFieldCode().equals("sys02-marriage")) {
					if (o.getValue() != null) {
						if (o.getValue().equals("2")) {
							dingEmployee.setMarriage("已婚");
						} else if (o.getValue().equals("1")) {
							dingEmployee.setMarriage("未婚");
						} else {
							dingEmployee.setMarriage(o.getValue());
						}
					}
				} else if (o.getFieldCode().equals("sys02-address")) {
					dingEmployee.setAddress(o.getValue());
				} else if (o.getFieldCode().equals("sys03-major")) {
					dingEmployee.setMajor(o.getValue());
				} else if (o.getFieldCode().equals("sys00-name")) {
					dingEmployee.setName(o.getValue());
				} else if (o.getFieldCode().equals("sys00-mobile")) {
					dingEmployee.setMobile(o.getValue());
				} else if (o.getFieldCode().equals("sys03-highestEdu")) {
					if (o.getValue() != null) {
						if (o.getValue().equals("4")) {
							dingEmployee.setHighestEdu("本科");
						} else if (o.getValue().equals("3")) {
							dingEmployee.setHighestEdu("专科");
						} else {
							dingEmployee.setHighestEdu(o.getValue());
						}
					}
				} else if (o.getFieldCode().equals("sys03-graduateSchool")) {
					dingEmployee.setGraduateSchool(o.getValue());
				} else if (o.getFieldCode().equals("sys03-graduationTime")) {
					dingEmployee.setGraduationTime(o.getValue());
				}

			}
			// 设置钉钉员工id
			dingEmployee.setUserId(list.get(i).getUserid());

			// 集合插入自定义员工类
			dingList.add(dingEmployee);

		}

		return dingList;

//		return rsp.getBody();

	}

	/**
	 * 根据不同url获取DingTalkClient
	 */
	public static DingTalkClient getDingTalkClient(String url) {

		return new DefaultDingTalkClient(url);

	}

	/**
	 * 过滤数组[]符号
	 */
	public static String filterSymbol(String str) {

		String symbol = str.replaceAll(", ", ",");

		String newstr = symbol.substring(1, symbol.length() - 1);

		return newstr;

	}

	/**
	 * 获取离职员工id
	 */
	public static List<List<String>> getMissEmployeeId(String accessToken) {

		List<List<String>> list = new LinkedList<List<String>>();

		Long offSet = 0l;

		Paginator paginator = null;

		do {

			// 第一次获取
			paginator = getMyMissNextCusrsor(offSet, accessToken);

			if (paginator.getNextCursor() != null) {

				list.add(paginator.getDataList());

				offSet = paginator.getNextCursor();

			}

		} while (paginator.getNextCursor() != null);

		return list;

	}

	/**
	 * 获取所有的离职员工id 返回getNextCursor 存在则有分页 不存在则结束 每次取20个
	 */
	public static Paginator getMyMissNextCusrsor(long offset, String accessToken) {

		DingTalkClient clientc = new DefaultDingTalkClient(MISSION_EMPLOYEE_URL);

		OapiSmartworkHrmEmployeeQuerydimissionRequest reqc = new OapiSmartworkHrmEmployeeQuerydimissionRequest();

		reqc.setOffset(offset);

		reqc.setSize((long) 20);

		OapiSmartworkHrmEmployeeQuerydimissionResponse responsec = null;
		try {
			responsec = clientc.execute(reqc, accessToken);
		} catch (ApiException e) {

			e.printStackTrace();
		}

		Paginator paginator = responsec.getResult();

		return paginator;

	}

	/**
	 * 获取部门列表
	 * 
	 * @throws ApiException
	 */
	public static Object getDepartmentList(String accessToken) {

		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
		OapiDepartmentListRequest request = new OapiDepartmentListRequest();
		request.setTopHttpMethod("GET");
		OapiDepartmentListResponse response = null;
		try {
			response = client.execute(request, accessToken);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.getBody();

	}

	/**
	 * 获取部门详情(根据部门id获取部门员工id)
	 * 
	 * @throws ApiException
	 */
	public static List<String> getUserIdListByDeId(String accessToken) {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
		OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
		req.setDeptId("11013735");
		req.setTopHttpMethod("GET");
		OapiUserGetDeptMemberResponse rsp = null;
		try {
			rsp = client.execute(req, accessToken);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		return rsp.getUserIds();

	}

	/**
	 * 注册业务事件回调接口
	 */
	public static String callBack() {
		DingTalkClient client = new DefaultDingTalkClient(CALLBACK_URL);
		OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
		request.setUrl("http://yirs.vaiwan.com/callback");
		request.setAesKey("1234567890123456789012345678901234567890123");
		request.setToken("szpdc");
		request.setCallBackTag(Arrays.asList("user_add_org", "user_modify_org", "user_leave_org"));
		OapiCallBackRegisterCallBackResponse response = null;
		try {
			response = client.execute(request, getAccessToken());
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return response.getBody();
	}

	/**
	 * 查询事件回调接口
	 */
	public static String selectEvent() {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back");
		OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
		request.setHttpMethod("GET");
		OapiCallBackGetCallBackResponse response = null;
		try {
			response = client.execute(request, getAccessToken());
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return response.getBody();
	}

}