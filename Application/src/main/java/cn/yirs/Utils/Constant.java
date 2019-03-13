package cn.yirs.Utils;

/**
 * 项目中的常量定义类
 */
public class Constant {
	/**
	 * 应用的SuiteKey，登录开发者后台，点击应用管理，进入应用详情可见 cordId
	 */
	public static final String SUITE_KEY = "ding549eb990a2e6ee0135c2f4657eb6378f";
	/**
	 * 应用的SuiteSecret，登录开发者后台，点击应用管理，进入应用详情可见
	 */
	public static final String SUITE_SECRET = "ding549eb990a2e6ee0135c2f4657eb6378f";

	/**
	 * 回调URL加解密用。应用的数据加密密钥，登录开发者后台，点击应用管理，进入应用详情可见
	 */
	public static final String ENCODING_AES_KEY = "1234567890123456789012345678901234567890123";

	/**
	 * 回调URL签名用。应用的签名Token, 登录开发者后台，点击应用管理，进入应用详情可见
	 */
	public static final String TOKEN = "szpdc";

	/**
	 * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
	 */
	public static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";
	/**
	 * 创建套件后，验证回调URL变更有效事件（第一次保存回调URL之后）
	 */
	public static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";

	/**
	 * suite_ticket推送事件
	 */
	public static final String EVENT_SUITE_TICKET = "suite_ticket";
	/**
	 * 企业授权开通应用事件
	 */
	public static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";
	/**
	 * 相应钉钉回调时的值
	 */
	public static final String CALLBACK_RESPONSE_SUCCESS = "success";
	
	public static final String CALLBACK_RESPONSE_FAIL = "fail";

}
