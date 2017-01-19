/**
 * 
 */
package com.zoj.bp.design.service;

import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IDesignCountService
{
	/**
	 * 获取当前日期的设计部统计记录
	 * @param pagination
	 * @param designerName 设计师名称
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName,String startDate,String endDate);
	
	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId
	 * @return
	 */
	DesignCount getTodayDesignCountByUserId(Integer userId);

	/**
	 * 新增设计师统计
	 * @param designCount
	 * @return
	 */
	Integer addDesignCount(DesignCount designCount);
}