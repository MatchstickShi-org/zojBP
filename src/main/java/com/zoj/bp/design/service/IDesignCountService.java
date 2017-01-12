/**
 * 
 */
package com.zoj.bp.design.service;

import java.util.List;

import com.zoj.bp.common.model.DesignCount;

/**
 * @author wangw
 *
 */
public interface IDesignCountService
{
	/**
	 * 获取当天的设计部统计记录
	 * @return
	 */
	List<DesignCount> getTodayDesignerCount();
}