/**
 * 
 */
package com.zoj.bp.design.dao;

import java.util.List;

import com.zoj.bp.common.model.DesignCount;

/**
 * @author wangw
 *
 */
public interface IDesignCountDao
{
	/**
	 * 获取当前日期的设计部统计记录
	 * @param orderId
	 * @return
	 */
	List<DesignCount> getTodayDesignerCount();
}