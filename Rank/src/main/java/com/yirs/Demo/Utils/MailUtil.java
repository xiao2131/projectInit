package com.yirs.Demo.Utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.yirs.Demo.DTO.ResultGenerator;
import com.yirs.Demo.Exception.TokenErrorException;

public class MailUtil {

	/**
	 * 发送邮件
	 * 
	 * @param mailAccount 接受的邮箱账号
	 * @param mailTitle   邮件标题
	 * @param mailContent 邮件内容
	 * @return
	 */
	public static Object sendMail(String mailAccount, String mailTitle, String mailContent) {

		// 创建Properties 类用于记录邮箱的一些属性
		Properties props = new Properties();
		// 表示SMTP发送邮件，必须进行身份验证
		props.put("mail.smtp.auth", "true");
		// 此处填写SMTP服务器
		props.put("mail.smtp.host", "smtp.qq.com");
		// 端口号，QQ邮箱端口587
		props.put("mail.smtp.port", "587");
		// 此处填写，写信人的账号
		props.put("mail.user", "yirsz@qq.com");
		// 此处填写16位STMP口令
		props.put("mail.password", "ppntrpstgoqdbdgc");

		// 构建授权信息，用于进行SMTP进行身份验证
		Authenticator authenticator = new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		// 设置发件人
		InternetAddress form;

		try {
			form = new InternetAddress(props.getProperty("mail.user"));

			message.setFrom(form);

			// 设置收件人的邮箱
			InternetAddress to = new InternetAddress(mailAccount);
			message.setRecipient(RecipientType.TO, to);

			// 设置邮件标题
			message.setSubject(mailTitle);

			// 设置邮件的内容体
			message.setContent(mailContent, "text/html;charset=UTF-8");

			// 最后当然就是发送邮件啦
			Transport.send(message);
		} catch (AddressException e) {
			throw new TokenErrorException(e.getMessage());
		} catch (MessagingException e) {
			throw new TokenErrorException(e.getMessage());
		}

		return new ResultGenerator().getSuccessResult();
	}

}
