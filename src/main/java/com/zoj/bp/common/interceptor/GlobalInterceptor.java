/**  */
package com.zoj.bp.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zoj.bp.common.util.HttpUtils;

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
			if(HttpUtils.instance().isAjaxRequest(request))
			{
				response.setHeader("sessionTimeout", "true");
				response.getWriter().write("{}");
			}
			else
				response.sendRedirect(request.getServletContext().getContextPath() + "/toLoginView");
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}