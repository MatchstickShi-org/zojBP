/**  */
package com.zoj.bp.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.util.HttpUtils;
import com.zoj.bp.common.util.ResponseUtils;

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
				response.getWriter().write(new JSONObject(
						ResponseUtils.buildRespMap(ReturnCode.SESSION_TIME_OUT, "sessionTimeout", true)).toJSONString());;
			}
			else
				response.sendRedirect(request.getServletContext().getContextPath() + "/toLoginView");
			return false;
		}
		return super.preHandle(request, response, handler);
	}
}