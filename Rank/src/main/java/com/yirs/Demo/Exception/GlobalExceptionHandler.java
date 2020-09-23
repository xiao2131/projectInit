package com.yirs.Demo.Exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.yirs.Demo.DTO.ResultGenerator;

import io.lettuce.core.RedisConnectionException;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	// Token异常类
	@SuppressWarnings("static-access")
	@ExceptionHandler(TokenErrorException.class)
	@ResponseBody
	public Object errorException(HttpServletRequest req, Exception e) {
		String url = req.getServletPath();
		logger.info("=============Start===============");
		logger.info("API Request : URL=" + url);
		logger.info("API Result : Result=" + e.getMessage());
		logger.info("==============END================");
		return new ResultGenerator().getFailResult(e.getMessage());

	}

	// redis连接异常
	@SuppressWarnings("static-access")
	@ExceptionHandler(RedisConnectionException.class)
	@ResponseBody
	public Object redisConnectionFailureException(HttpServletRequest req, Exception e) {
		String url = req.getServletPath();
		logger.info("=============Start===============");
		logger.info("Redis未开启，请联系管理员开启redis");
		logger.info("==============END================");
		return new ResultGenerator().getFailResult(e.getMessage());

	}

}