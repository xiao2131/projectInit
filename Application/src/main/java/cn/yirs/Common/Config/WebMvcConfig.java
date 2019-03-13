package cn.yirs.Common.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置 非spring security的配置
 *
 * @author: Li Jinhui
 * @since: 2018-12-04 11:00
 * @update 2018-12-28 10:41
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private String origins = "http://localhost:8080";

	/**
	 * 开启跨域
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		System.out.println("访问一次");
		// 设置允许跨域的路由
		registry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOrigins(origins)
				// 是否允许证书（cookies）
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("*")
				// 跨域允许时间
				.maxAge(3600);
	}

}