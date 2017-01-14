/**
 * 
 */
package com.zoj.bp.design.dao;

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
	 * @return
	 */
	DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName);
}