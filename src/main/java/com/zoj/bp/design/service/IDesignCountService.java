/**
 * 
 */
package com.zoj.bp.design.service;

import java.util.List;

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
	 * 根据日期获取设计部统计记录
	 * @param pagination
	 * @param designerName 设计师名称
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	DatagridVo<DesignCount> getDesignerCountByDate(Pagination pagination,String designerName,String startDate,String endDate);
	
	/**
	 * 获取当天的设计部统计记录
	 * @param pagination
	 * @param designerName 设计师名称
	 * @return
	 */
	DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName);
	
	/**
	 * 获取前一天的统计数据（定时任务用）
	 * @param userId 用户Id
	 * @param countDate 统计日期（数据日期）
	 * @return
	 */
	DesignCount getTodayDesignCountByUserId(Integer userId,String countDate);

	/**
	 * 新增设计师统计
	 * @param designCount
	 * @return
	 */
	Integer addDesignCount(DesignCount designCount);

	/**
	 * 根据用户Id获取最近一次统计记录
	 * @param userId
	 * @return
	 */
	List<DesignCount> getLastDesignCountByUsetrId(Integer userId);
}