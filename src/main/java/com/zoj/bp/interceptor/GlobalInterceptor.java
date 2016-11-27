/**  */
package com.zoj.bp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author MatchstickShi
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		Object obj = request.getSession().getAttribute("loginUser");
		if (obj == null)
		{
			response.sendRedirect("toLoginView");
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}