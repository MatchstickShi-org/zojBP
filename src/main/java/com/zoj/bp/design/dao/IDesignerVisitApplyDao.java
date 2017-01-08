/**
 * 
 */
package com.zoj.bp.design.dao;

import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.vo.DesignerVisitApply;

/**
 * @author wangw
 *
 */
public interface IDesignerVisitApplyDao
{
	/**
	 * @param designerVisitApply
	 * @return 
	 */
	Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply);
	/**
	 * @param pagination
	 * @param designerName
	 * @param orderId
	 * @return
	 */
	DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,String designerName,Integer orderId);

	
}