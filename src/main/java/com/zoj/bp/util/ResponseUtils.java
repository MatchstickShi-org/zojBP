/**
 * 
 */
package com.zoj.bp.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoj.bp.excption.BusinessException;
import com.zoj.bp.excption.ReturnCode;
import com.zoj.bp.vo.DatagridVo;

/**
 * @author Administrator
 *
 */
public class ResponseUtils
{
	private static Logger logger = LoggerFactory.getLogger(ResponseUtils.class);
	
	public static Map<String, Object> buildRespMap(BusinessException e, Map<String, Object> returnMap)
	{
		if(returnMap == null)
			returnMap = new TreeMap<>();
		returnMap.put("returnCode", e.getReturnCode());
		returnMap.put("msg", e.getReturnMsg());
		return returnMap;
	}
	
	public static Map<String, Object> buildRespMap(ReturnCode returnCode)
	{
		Map<String, Object> returnMap = new TreeMap<>();
		returnMap.put("returnCode", returnCode.getReturnCode());
		returnMap.put("msg", returnCode.getMsg());
		return returnMap;
	}
	
	public static Map<String, Object> buildRespMapByBean(Serializable bean)
	{
		return buildRespMapByBean(ReturnCode.SUCCESS, bean);
	}
	
	public static Map<String, Object> buildRespMapByBean(ReturnCode returnCode, Serializable bean)
	{
		try
		{
			Map<String, Object> map = PropertyUtils.describe(bean);
			map.remove("class");
			return buildRespMap(returnCode, map);
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1)
		{
			logger.error(e1.getMessage(), e1);
			return buildRespMap(new BusinessException(ReturnCode.SYSTEM_INTERNAL_ERROR), new HashMap<String, Object>());
		}
	}
	
	public static Map<String, Object> buildRespMap(BusinessException e, Object bean)
	{
		try
		{
			Map<String, Object> map = PropertyUtils.describe(bean);
			return buildRespMap(e, map);
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1)
		{
			logger.error(e1.getMessage(), e1);
			return buildRespMap(new BusinessException(ReturnCode.SYSTEM_INTERNAL_ERROR), new HashMap<String, Object>());
		}
	}
	
	public static Map<String, Object> buildRespMap(BusinessException e)
	{
		return buildRespMap(e, new HashMap<String, Object>());
	}

	/**
	 * @param e
	 * @return
	 */
	public static <T> DatagridVo<T> buildRespDatagridVo(BusinessException e, DatagridVo<T> vo)
	{
		if(vo == null)
			vo = DatagridVo.buildDatagridVo(new ArrayList<T>(), 0);
		vo.setReturnCode(e.getReturnCode());
		vo.setMsg(e.getReturnMsg());
		return vo;
	}

	/**
	 * @param returnCode
	 * @param map
	 * @return
	 */
	public static Map<String, Object> buildRespMap(ReturnCode returnCode, Map<String, Object> returnMap)
	{
		if(returnMap == null)
			returnMap = new TreeMap<>();
		returnMap.put("returnCode", returnCode.getReturnCode());
		returnMap.put("msg", returnCode.getMsg());
		return returnMap;
	}
}