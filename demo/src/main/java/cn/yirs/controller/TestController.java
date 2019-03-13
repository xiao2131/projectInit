//package cn.yirs.controller;
//
//import java.util.Base64;
//import java.util.Base64.Encoder;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.yirs.Utils.NasApi;
//
//@RestController
//@RequestMapping("api/user")
//public class TestController {
//
//	@RequestMapping("test")
//	public Object test() throws Exception {
//
//		String sid = new NasApi().getSid();
//
//		return sid;
//	}
//
//	/**
//	 * http://172.16.15.198:8080/cgi-bin/ wizReq.cgi? wiz_func=user_create
//	 * &action=add_user& AFP=1& FTP=1& MULTIMEDIA_STATION=1 &MUSIC_STATION=1
//	 * &PHOTO_STATION=1 &SAMBA=1 &VIDEO_STATION=1 &WEBDAV=1 &WFM=1 &a_description=
//	 * &a_email= &a_username=159357@qq.com &comment=&create_priv=0 &email=
//	 * &gp0=everyone &gp_len=1 &hidden=no &no_share_len=0 &oplocks=1 &ps=
//	 * &rd_share0=Public &rd_share_len=1 &recursive=1 &recycle_bin=0
//	 * &recycle_bin_administrators_only=0 &rw_share0=Multimedia &rw_share_len=1
//	 * &send_mail=0 &set_application_privilege=1 &sid=f7a1fwks&vol_no=1
//	 */
//	@RequestMapping("create")
//	public Object create() throws Exception {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		return map;
//
//	}
//
//	/**
//	 * http:/ / 172. 17. 20. 26:8080/ cgi-bin/ priv/ privRequest. cgi?&
//	 * subfunc=user& getdata=1& sort=13& filter=&
//	 * lower=0&refresh=0&sid=lpeq0qkj&type=0&upper=10 R 得到用户列表
//	 */
//	@RequestMapping("getuserlist")
//	public Object getuserlist() throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("subfunc", "user");
//		map.put("getdata", 1);
//		map.put("sort", 13);
//		map.put("refresh", 1);
//		map.put("sid", NasApi.getSid());
//		map.put("type", 0);
//		return NasApi.getUserList(map);
//	}
//
//}
