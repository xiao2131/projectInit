package cn.yirs.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;

import cn.yirs.Common.constant.ConstantKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author Yirs
 * @time 2018-12-15 20:35:20
 * @describe token工具类
 */
public class TokenUtils {

	/**
	 * 生成token
	 */
	public static String getToken(Authentication auth, List<String> roleList, Date time) {

		String token = null;

		token = Jwts.builder().setSubject(auth.getName() + "-" + roleList).setExpiration(time)
				.signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) // 采用什么算法是可以自己选择的，不一定非要采用HS512
				.compact();

		return token;
	}

	/**
	 * 生成token
	 */
	public static String getToken(String subject, Date time) {

		String token = null;

		token = Jwts.builder().setSubject(subject).setExpiration(time)
				.signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) // 采用什么算法是可以自己选择的，不一定非要采用HS512
				.compact();

		return token;
	}

	/**
	 * 设置token生命周期 下面例子的生命周期为一分钟
	 * 
	 * @param field  eg:Calendar.MINUTE
	 * @param amount eg:1
	 * @return
	 */
	public static Date setTokenLifeCycle(int field, int amount) {

		// 设置过期时间
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());

		/* calendar.add(Calendar.DAY_OF_MONTH, 30); */// 30天
		calendar.add(field, amount);// 1分钟

		Date time = calendar.getTime();

		return time;
	}

}
