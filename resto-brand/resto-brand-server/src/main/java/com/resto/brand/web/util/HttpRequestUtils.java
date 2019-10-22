/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT China Mobile (SuZhou) Software Technology Co.,Ltd. 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * CMSS Co.,Ltd. The programs may be used and/or copied only with written
 * permission from CMSS Co.,Ltd. or in accordance with the terms and conditions
 * stipulated in the agreement/contract under which the program(s) have been
 * supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.resto.brand.web.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;

public class HttpRequestUtils {

	private static Logger LOGGER = LoggerFactory
			.getLogger(HttpRequestUtils.class); // 日志记录

	private static final String CHARSET = "UTF-8";

	/* URL of server */
	@SuppressWarnings("unused")
	private final String serverUrl;

	/* http method : get/post/put/delete */
	@SuppressWarnings("unused")
	private final String httpMethod;

	/* the date sent to server */
	private static String clientData;

	/* timeout : 5 seconds */
	private static final int DEFAULT_TIMEOUT = 5000;

	/* connection timeout */
	@SuppressWarnings("unused")
	private final int connectTimeout;

	/* the time to wait for data from server */
	@SuppressWarnings("unused")
	private final int socketTimeout;

	/**
	 * Constructs client with the specified URL
	 *
	 * @param url
	 *            the url of server of REST
	 */
	@SuppressWarnings("static-access")
	public HttpRequestUtils(final String url) {

		this.serverUrl = url;
		this.httpMethod = "POST";
		this.clientData = null;
		this.connectTimeout = DEFAULT_TIMEOUT;
		this.socketTimeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Constructs client with the specified URL and data
	 *
	 * @param url
	 *            the url of server of REST
	 * @param data
	 *            data in JSON format
	 */
	@SuppressWarnings("static-access")
	public HttpRequestUtils(final String url, final String data) {

		this.serverUrl = url;
		this.httpMethod = "POST";
		this.clientData = data;
		this.connectTimeout = DEFAULT_TIMEOUT;
		this.socketTimeout = DEFAULT_TIMEOUT;
	}

	/**
	 * Constructs client with URL, method, and data
	 *
	 * @param url
	 *            the url of server of REST
	 * @param method
	 *            method of HTTP(get/post/delete/put)
	 * @param data
	 *            data in JSON format
	 */
	@SuppressWarnings("static-access")
	public HttpRequestUtils(final String url, final String method,
                            final String data) {

		this.serverUrl = url;
		this.httpMethod = method;
		this.clientData = data;
		this.connectTimeout = DEFAULT_TIMEOUT;
		this.socketTimeout = DEFAULT_TIMEOUT;
	}

	/**
	 * httpPost
	 *
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static String httpPost(final String url,
                                      final String jsonParam) {

		return httpPost(url, jsonParam, false);
	}

	/**
	 * post请求
	 *
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static String httpPost(String url, final String jsonParam,
                                      final boolean noNeedResponse) {

		// post请求返回结果
		String str  = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpClient = HttpClients.createDefault();
			// 设置请求和传输超时时间
			final RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000).setConnectTimeout(10000).build();
			final HttpPost method = new HttpPost(url);
			method.setConfig(requestConfig);

			if (null != jsonParam) {
				// 解决中文乱码问题
				final StringEntity entity = new StringEntity(
						jsonParam, CHARSET);
				entity.setContentEncoding(CHARSET);
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			httpResponse = httpClient.execute(method);
			url = URLDecoder.decode(url, CHARSET);
			if (httpResponse.getStatusLine().getStatusCode() >= 300) {
				LOGGER.error("post请求提交失败:" + url, "状态码："
						+ httpResponse.getStatusLine().getStatusCode());
			} else {
				/** 请求发送成功，并得到响应 **/

				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(httpResponse.getEntity(),
							CHARSET);
					if (noNeedResponse) {
						return null;
					}

				} catch (final Exception e) {
					LOGGER.error("post请求提交失败:" + url, e);
				}
			}
		} catch (final IOException e) {
			LOGGER.error("post请求提交失败:" + url, e);
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (final Exception e2) {// NOPMD
			}
		}
		return str;
	}

	/**
	 * 发送get请求
	 *
	 * @param url
	 *            路径
	 * @return
	 */
	public static String httpGet(String url) {

		// get请求返回结果
		String strResult = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			// 设置请求和传输超时时间
			final RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000).setConnectTimeout(10000).build();
			// 发送get请求
			// final HttpGet request = new HttpGet(url);
			final String getUrlStr = getUrlForMethod(url);
			final HttpGet request = new HttpGet(getUrlStr);
			request.setConfig(requestConfig);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() >= 300) {
				LOGGER.error("post请求提交失败:" + url, "状态码："
						+ response.getStatusLine().getStatusCode());
			} else {
				/** 请求发送成功，并得到响应 **/
				/** 读取服务器返回过来的json字符串数据 **/
				  strResult = EntityUtils.toString(
						response.getEntity(), CHARSET);
				/** 把json字符串转换成json对象 **/
				url = URLDecoder.decode(url, "UTF-8");
			}
		} catch (final IOException e) {
			LOGGER.error("get请求提交失败:" + url, e);
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (final Exception e2) {// NOPMD
				// do nothing
			}
		}
		return strResult;
	}

	/**
	 * method "GET" "DELETE"<br>
	 *
	 * @param serverUrl
	 * @return
	 */
	public static String getUrlForMethod(final String serverUrl) {

		if (clientData != null) {
			if (serverUrl.endsWith("?")) {
				return serverUrl + clientData;
			} else {
				return serverUrl + "?" + clientData;
			}
		}
		return serverUrl;
	}
}
