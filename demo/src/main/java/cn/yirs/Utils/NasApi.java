package cn.yirs.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * @time 2019年2月20日11:06:28
 * @author by Yirs
 */
@Component
public class NasApi {

	// 管理员用户名
	private static String USERNAME;
	// 管理员密码
	private static String PASSWORD;
	// api
	private static String API;

	@Value(value = "${NAS.username}")
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	@Value(value = "${NAS.password}")
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	@Value(value = "${NAS.api}")
	public void setAPI(String aPI) {
		API = aPI;
	}

	/**
	 * 返回用户列表
	 * 例子：http://172.17.20.26:8080/cgi-bin/priv/privRequest.cgi?&subfunc=user&getdata=1&sort=13&filter=&lower=0&refresh=0&sid=lpeq0qkj&type=0&upper=10
	 */
	public static JSONObject getUserList(Map<String, Object> params) {

		return HttpsUtils.doGet(API + "priv/privRequest.cgi", params);

	}

	/**
	 * 创建用户
	 * 列子：http://172.16.15.198:8080/cgi-bin/wizReq.cgi?wiz_func=user_create&action=check_user&AFP=1&FTP=1&MULTIMEDIA_STATION=1&MUSIC_STATION=1&PHOTO_STATION=1&SAMBA=1&VIDEO_STATION=1&WEBDAV=1&WFM=1&a_description=&a_email=&a_username=12345@qq.com&comment=&create_priv=0&email=&gp0=everyone&gp_len=1&hidden=no&no_share_len=0&oplocks=1&ps=&rd_share0=Public&rd_share_len=1&recursive=1&recycle_bin=0&recycle_bin_administrators_only=0&rw_share0=Multimedia&rw_share_len=1&send_mail=0&set_application_privilege=1&sid=tqfp5fpp&vol_no=1
	 */
	public static JSONObject createUser(Map<String, Object> params) {
		params.put("wiz_func", "user_create");
		params.put("action", "add_user");
		params.put("sid", getSid());
		params.put("a_description", "自动创建用户");
		params.put("create_priv", 0);
		params.put("hidden", "no");
		params.put("no_share_len", 0);
		params.put("oplocks", 1);
		params.put("rd_share0", "Public");
		params.put("rd_share_len", 1);
		params.put("recursive", 1);
		params.put("recycle_bin", 0);
		params.put("recycle_bin_administrators_only", 0);
		params.put("rw_share0", "Multimedia");
		params.put("rw_share_len", 1);
		params.put("send_mail", 0);
		params.put("set_application_privilege", 1);
		params.put("vol_no", 1);

		return HttpsUtils.doGet(API + "wizReq.cgi", params);
	}

	/**
	 * 登录获取sid
	 * 例子：http://172.16.15.198:8080/cgi-bin/authLogin.cgi?plain_pwd=123456sw&user=admin
	 */
	public static String getSidByLogin(Map<String, Object> params) {
		return HttpsUtils.doGet(API + "authLogin.cgi", params).getString("authSid");
	}

	/**
	 * 管理员登录直接获取sid
	 */
	public static String getSid() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", USERNAME);
		params.put("pwd", encodeToString(PASSWORD));
		return HttpsUtils.doGet(API + "authLogin.cgi", params).getString("authSid");
	}

	/**
	 * Base64加密
	 */
	public static String encodeToString(String str) {
		try {
			return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取网络权限和文件权限
	 */
	public static Map<String, Object> getNetAndFileAuthor(Map<String, Object> params) {
		params.put("AFP", 1);
		params.put("FTP", 1);
		params.put("MULTIMEDIA_STATION", 1);
		params.put("MUSIC_STATION", 1);
		params.put("PHOTO_STATION", 1);
		params.put("VIDEO_STATION", 1);
		params.put("WEBDAV", 1);
		params.put("WFM", 1);
		return params;
	}
}
