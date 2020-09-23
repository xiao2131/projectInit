//package com.yirs.Demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//	/**
//	 * 通过 createRestApi函数来构建一个DocketBean 函数名,可以随意命名,喜欢什么命名就什么命名
//	 */
//	@Bean
//	public Docket createRestApi() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())// 调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
//				.select()
//				// 控制暴露出去的路径下的实例
//				// 如果某个接口不想暴露,可以使用以下注解
//				// @ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
//				.apis(RequestHandlerSelectors.basePackage("com.yirs.Demo.controller"))
//				.paths(PathSelectors.any()).build();
//	}
//
//	// 构建 api文档的详细信息函数
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				// 页面标题
//				.title("Spring Boot Swagger2 构建RESTful API")
//				// 条款地址
//				.termsOfServiceUrl("http://localhost:8080/swagger-ui.html").version("1.0")
//				// 描述
//				.description("API 描述").build();
//	}
//}