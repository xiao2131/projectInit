package cn.yirs.Common.handler;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.yirs.Common.Dto.ResultGenerator;



/**
 * @Auther: zhaoxinguo
 * @Date: 2018/9/20 14:55
 * @Description: 自定义认证拦截器
 */
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Log log = LogFactory.getLog(Http401AuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// 这个值是从token校验过滤器设置的
		String errCode = (String) request.getAttribute("errCode");

		response.setCharacterEncoding("UTF-8");
		
		response.setContentType("application/json;charset=UTF8");
		
		PrintWriter writer = response.getWriter();

		if (!StringUtils.isEmpty(errCode)) {

			if (errCode.equals("40001")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("Token已过期", 40001)));

			} else if (errCode.equals("40002")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("Token格式错误", 40002)));

			} else if (errCode.equals("40003")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("Token没有被正确构造", 40003)));

			} else if (errCode.equals("40004")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("签名失败", 40004)));

			} else if (errCode.equals("40005")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("非法参数异常", 40005)));

			} else if (errCode.equals("40000")) {

				writer.write(JSON.toJSONString(ResultGenerator.getFailResult("用户未登录", 40000)));

			}

		}

		writer.flush();

		writer.close();

	}

}
