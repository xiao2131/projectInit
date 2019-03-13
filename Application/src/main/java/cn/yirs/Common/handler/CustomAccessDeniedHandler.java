package cn.yirs.Common.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.yirs.Common.Dto.ResultGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: zhaoxinguo
 * @Date: 2018/8/13 16:38
 * @Description:
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	public static final Log LOG = LogFactory.getLog(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {

		LOG.info("自定义权限认证---------->权限不足");

		response.setCharacterEncoding("UTF-8");

		response.setContentType("application/json;charset=UTF8");

		PrintWriter writer = response.getWriter();

		writer.write(JSON.toJSONString(ResultGenerator.getFailResult("权限不足 无法访问", 20001)));
		
		writer.flush();

		writer.close();

	}
}
