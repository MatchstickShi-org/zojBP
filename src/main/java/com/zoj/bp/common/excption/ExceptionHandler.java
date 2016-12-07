/**
 * 
 */
package com.zoj.bp.common.excption;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.zoj.bp.common.util.HttpUtils;
import com.zoj.bp.common.util.ResponseUtils;

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
					return handleResponseBody(returnMap, request, response);
				}
				catch (Exception e1)
				{
					return null;
				}
			}
			else		//默认为视图
				return new ModelAndView("global/500", "msg", e.getMessage());
		}
		return null;
	}

	@SuppressWarnings({"unchecked", "resource" })
	private ModelAndView handleResponseBody(Map<String, Object> returnMap, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty())
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		
		MediaType.sortByQualityValue(acceptedMediaTypes);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
		if (messageConverters != null)
		{
			for (MediaType acceptedMediaType : acceptedMediaTypes)
			{
				for (HttpMessageConverter<?> messageConverter : messageConverters)
				{
					if(messageConverter.canWrite(returnMap.getClass(), acceptedMediaType))
					{
						((HttpMessageConverter<Object>)messageConverter).write(returnMap, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}
		if (logger.isWarnEnabled())
		{
			logger.warn(
					"Could not find HttpMessageConverter that supports return type [" + "returnValueType" + "] and " + "acceptedMediaTypes");
		}
		return null;
	}
}