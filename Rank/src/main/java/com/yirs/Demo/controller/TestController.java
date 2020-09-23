package com.yirs.Demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yirs.Demo.Utils.HttpUtils;
import com.yirs.Demo.Utils.RedisUtil;
import com.yirs.Demo.entity.School;
import com.yirs.Demo.service.SchoolService;

@RestController
@RequestMapping
public class TestController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 任务：把springboot的jackjson改为fastjson
	 */

	@RequestMapping(value = "/")
	public Object index(String name) throws UnsupportedEncodingException {
		String key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJvWkdsSTR6SFh0bEZnUXdlcDItaDFJWXMxOTNvIiwiZXhwIjowLCJpYXQiOjE1OTYzNzkxOTh9.Gw_5kIXczdsfxxbmwRMCWCdHeFUBSA7PSOnHEVcNjmM";
		
		
		
		return redisUtil.get(key);

	}
}