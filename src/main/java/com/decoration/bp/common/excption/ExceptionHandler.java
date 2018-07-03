/**
 * 
 */
package com.decoration.bp.common.excption;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.decoration.bp.common.util.HttpUtils;
import com.decoration.bp.common.util.ResponseUtils;

/**
 * @author MatchstickShi
 *
 */
public class ExceptionHandler extends ExceptionHandlerExceptionResolver
{
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	{
		BusinessException e = null;
		if(ex instanceof BusinessException)
		{
			e = (BusinessException) ex;
			logger.error(e.getReturnMsg());
		}
		else if(ex instanceof EmptyResultDataAccessException)
		{
			logger.error(ex.getMessage(), ex);
			e = new BusinessException(ReturnCode.NOT_FIND_RECORD, ex);
		}
		else if(ex instanceof Exception)
		{
			logger.error(ex.getMessage(), ex);
			e = new BusinessException(ReturnCode.SYSTEM_INTERNAL_ERROR, ex);
		}
		
		if (handler != null)
		{
			if (HttpUtils.instance().isAjaxRequest(request))
			{
				Map<String, Object> returnMap = ResponseUtils.buildRespMap(e);
				try
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return handleResponseBody(returnMap, request, response);
				}
				catch (Exception e1)
				{
					return new ModelAndView();
				}
			}
			else		//默认为视图
				return new ModelAndView("global/500", "msg", e.getMessage());
		}
		return new ModelAndView();
	}

	private ModelAndView handleResponseBody(Map<String, Object> returnMap, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		super.getApplicationContext().getBean(FastJsonHttpMessageConverter.class).write(returnMap, MediaType.APPLICATION_JSON, outputMessage);
		/*if (logger.isWarnEnabled())
		{
			logger.warn(
					"Could not find HttpMessageConverter that supports return type [" + "returnValueType" + "] and " + "acceptedMediaTypes");
		}*/
		return new ModelAndView();
	}
}