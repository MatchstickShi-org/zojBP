/**
 * 
 */
package com.zoj.bp.design.dao;

import java.util.List;

import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IDesignCountDao
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
	 * @param userId 用户Id
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	DesignCount getTodayDesignCountByUserId(Integer userId,String startDate,String endDate);

	/**
	 * 新增设计师统计
	 * @param designCount
	 * @return
	 */
	Integer addDesignCount(DesignCount designCount);

	/**
	 * 根据用户Id获取最近统计记录
	 * @param userId
	 * @return
	 */
	List<DesignCount> getLastDesignCountByUsetrId(Integer userId);
}