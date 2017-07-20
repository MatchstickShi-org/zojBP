/**  */
package com.zoj.bp.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MatchstickShi
 */
public class HttpUtils
{
	private final String requestHeaderKey_X_Requested_With = "X-Requested-With";
	
	private final String requestHeaderValue_XMLHttpRequest = "XMLHttpRequest";
	
	private static HttpUtils instance = new HttpUtils();
	
	private HttpUtils(){}
	
	public static HttpUtils instance()
	{
		return instance;
	}
	
	public boolean isAjaxRequest(HttpServletRequest request)
	{
		return requestHeaderValue_XMLHttpRequest.equals(request.getHeader(requestHeaderKey_X_Requested_With));
	}

	/**
	 * 获取客户端ip地址
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	 */
	public String getIpAddr(HttpServletRequest request) throws UnknownHostException
	{
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
			ipAddress = request.getHeader("Proxy-Client-IP");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))
			{
				// 根据网卡取本机配置的IP
				ipAddress = InetAddress.getLocalHost().getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15)		// "***.***.***.***".length() = 15
		{
			if (ipAddress.indexOf(",") > 0)
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}
		return ipAddress;
	}
}