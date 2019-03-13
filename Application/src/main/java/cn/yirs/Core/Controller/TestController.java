package cn.yirs.Core.Controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import cn.yirs.Common.Entity.Employee;
import cn.yirs.Core.Service.EmployeeService;
import cn.yirs.Utils.DingTalkUtils;

@RestController
public class TestController {

	private final Log logger = LogFactory.getLog(TestController.class);

	@Autowired
	private EmployeeService employeeService;

	// 第一导入数据插入 之后就没用啦
	@RequestMapping("insert")
	public void test() throws Exception {
		// 获取token
		String accessToken = DingTalkUtils.getAccessToken();
		// 获取在职员工id 每次只能20个
		List<List<String>> list = DingTalkUtils.getJobEmployeeId(accessToken);
		// 先往数据库插入数据
		for (List<String> list2 : list) {
			// 得到在职员工的id 每次20个
			List<Employee> dingList = DingTalkUtils.getHrmEmployeeList(accessToken,
					DingTalkUtils.filterSymbol(list2.toString()));
			// 数据库插入数据
			employeeService.insertBatch(dingList);
		}
	}

	@RequestMapping("register")
	public void register() {

		logger.info(DingTalkUtils.callBack());

	}

	@RequestMapping("select")
	public String select() {

		return DingTalkUtils.selectEvent();

	}

	@RequestMapping("get")
	public Object get() throws ApiException {

		return DingTalkUtils.getJobEmployeeId(DingTalkUtils.getAccessToken());

	}

}
