/**  */
package com.zoj.bp.util;

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
}