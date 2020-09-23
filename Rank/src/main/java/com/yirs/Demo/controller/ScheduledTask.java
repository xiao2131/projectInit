//package com.yirs.Demo.controller;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.yirs.Demo.service.UserinfoService;
//
//@Component
//public class ScheduledTask {
//
//	@Autowired
//	private UserinfoService userinfoService;
//
//	@Scheduled(cron = "0/60 * *  * * ? ")
//	public void update() {
//		System.out.println("执行一次");
//		System.out.println(userinfoService.sendMail());
//	}
//
//}
