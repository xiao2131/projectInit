package com.yirs.Demo.Utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http请求工具封装
 *
 * @author ourlang
 */
public class HttpUtils {

	/**
	 * 发起GET同步请求
	 *
	 * @param uri        请求的url地址
	 * @param headers    请求头参数map
	 * @param parameters 请求参数map
	 * @return 请求的结果字符串
	 */
	public static String get(String uri, Map<String, String> headers, Map<String, String> parameters) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 封装get请求的参数
		StringBuilder urlParameters = addGetParameters(parameters);
		HttpGet httpGet;

		if (urlParameters != null) {
			httpGet = new HttpGet(uri + urlParameters);
		} else {
			httpGet = new HttpGet(uri);
		}

		return getResultStr(headers, httpClient, httpGet);

	}

	/**
	 * 发起PUT同步请求
	 *
	 * @param uri        请求的url地址
	 * @param headers    请求头参数map
	 * @param parameters 请求参数map
	 * @return 请求的结果字符串
	 */
	public static String put(String uri, Map<String, String> headers, Map<String, String> parameters, String jsonStr)
			throws UnsupportedEncodingException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut httpPut = new HttpPut(uri);
		addParameters(httpPut, parameters, jsonStr);
		return getResultStr(headers, httpClient, httpPut);
	}

	/**
	 * 发起POST同步请求
	 *
	 * @param uri        请求的url地址
	 * @param headers    请求头参数map
	 * @param parameters 请求参数map
	 * @param jsonStr    请求body
	 * @return 请求的结果字符串
	 */
	public static String post(String uri, Map<String, String> headers, Map<String, String> parameters, String jsonStr)
			throws UnsupportedEncodingException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(uri);
		addParameters(httpPost, parameters, jsonStr);
		return getResultStr(headers, httpClient, httpPost);
	}



	/**
	 * 发起DELETE请求
	 *
	 * @param uri        请求的url地址
	 * @param headers    请求头参数map
	 * @param parameters 请求参数map
	 * @return 请求的结果字符串
	 */
	public static String delete(String uri, Map<String, String> headers, Map<String, String> parameters) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 封装get请求的参数
		StringBuilder urlParameters = addGetParameters(parameters);
		HttpDelete httpDelete = new HttpDelete(uri + urlParameters);
		return getResultStr(headers, httpClient, httpDelete);
	}

	/**
	 * 添加get请求的参数
	 *
	 * @param parameters 请求参数的map
	 * @return StringBuilder
	 */
	private static StringBuilder addGetParameters(Map<String, String> parameters) {

		if (parameters == null) {
			return null;
		}

		StringBuilder urlParameters = new StringBuilder();
		urlParameters.append("?");
		for (String key : parameters.keySet()) {
			String value = parameters.get(key);
			urlParameters.append(key).append("=").append(value).append("&");
		}
		return urlParameters;
	}

	/**
	 * 设置请求头参数
	 *
	 * @param headers     请求头map集合
	 * @param httpRequest http请求对象
	 */
	private static void setHeaders(Map<String, String> headers, HttpRequestBase httpRequest) {
		if (headers != null && !headers.isEmpty()) {
			for (String key : headers.keySet()) {
				String value = headers.get(key);
				httpRequest.setHeader(key, value);
			}
		}
	}

	/**
	 * POST、GET请求的参数添加方式
	 *
	 * @param parameters     需要添加到请求参数的map集合
	 * @param postPutRequest post或者get请求对象
	 * @param jsonStr        请求json参数
	 * @throws UnsupportedEncodingException 传递参数引发的异常
	 */
	private static void addParameters(HttpEntityEnclosingRequestBase postPutRequest, Map<String, String> parameters,
			String jsonStr) throws UnsupportedEncodingException {

		if (!StringUtils.isEmpty(jsonStr)) {
			postPutRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
			postPutRequest.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
		} else {
			// 1、构造list集合，往里面存请求的数据
			List<NameValuePair> list = new ArrayList<>();
			for (String key : parameters.keySet()) {
				String value = parameters.get(key);
				BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, value);
				list.add(basicNameValuePair);
			}
			// 2 我们发现Entity是一个接口，所以只能找实现类，发现实现类又需要一个集合，集合的泛型是NameValuePair类型
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list);
			// 3 通过setEntity 将我们的entity对象传递过去
			postPutRequest.setEntity(formEntity);
		}

	}

	/**
	 * 新方法 添加
	 */
	private static void addParameters(HttpEntityEnclosingRequestBase postPutRequest, Map<String, String> parameters) throws UnsupportedEncodingException {

		postPutRequest.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 1、构造list集合，往里面存请求的数据
		List<NameValuePair> list = new ArrayList<>();
		for (String key : parameters.keySet()) {
			String value = parameters.get(key);
			BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, value);
			list.add(basicNameValuePair);
		}
		// 2 我们发现Entity是一个接口，所以只能找实现类，发现实现类又需要一个集合，集合的泛型是NameValuePair类型
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list);
		// 3 通过setEntity 将我们的entity对象传递过去
		postPutRequest.setEntity(formEntity);

	}

	/**
	 * 获取请求的返回结果字符串
	 *
	 * @param headers     请求头map集合
	 * @param httpClient  htt请求对象
	 * @param httpRequest 请求的http方式对象
	 * @return 请求结果字符串
	 */
	private static String getResultStr(Map<String, String> headers, CloseableHttpClient httpClient,
			HttpRequestBase httpRequest) {
		String body = null;
		CloseableHttpResponse response;
		// 设置请求头的参数
		setHeaders(headers, httpRequest);
		try {
			// 执行请求
			response = httpClient.execute(httpRequest);
			// 请求成功执行
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获取返回的数据
				HttpEntity entity = response.getEntity();
				// 转换成字符串
				body = EntityUtils.toString(entity);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

//	public static void main(String[] args) throws Exception {
//		Map<String, String> parameters = new HashMap<>(16);
//		User data = new User();
//		data.setID(9);
//		data.setName("我的姓名");
//		String ps = JSON.toJSONString(data);
//		String s = put("http://192.168.0.101:7777/pathway/attribute", null, parameters, ps);
//		System.out.println(s);
//	}
}