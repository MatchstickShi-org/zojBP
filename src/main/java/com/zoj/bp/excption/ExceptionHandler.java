/**
 * 
 */
package com.zoj.bp.excption;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zoj.bp.util.ResponseUtils;

/**
 * @author MatchstickShi
 *
 */
public class ExceptionHandler extends ExceptionHandlerExceptionResolver
{
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
	
	@Override
	public void afterPropertiesSet()
	{
		super.getMessageConverters().add(0, getApplicationContext().getBean(FastJsonHttpMessageConverter.class));
		super.afterPropertiesSet();
	}

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
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
			if (responseBodyAnn != null)
			{
				Map<String, Object> returnMap = ResponseUtils.buildRespMap(e);
				try
				{
					ResponseStatus responseStatusAnn = AnnotationUtils.findAnnotation(method, ResponseStatus.class);
					if (responseStatusAnn != null)
					{
						HttpStatus responseStatus = responseStatusAnn.value();
						String reason = responseStatusAnn.reason();
						if (!StringUtils.hasText(reason))
							response.setStatus(responseStatus.value());
						else
						{
							try
							{
								response.sendError(responseStatus.value(), reason);
							}
							catch (IOException e1)
							{}
						}
					}
					else
						response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					return handleResponseBody(returnMap, request, response);
				}
				catch (Exception e1)
				{
					return null;
				}
			}
			else if(method.getReturnType() == ModelAndView.class)		//默认为视图
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