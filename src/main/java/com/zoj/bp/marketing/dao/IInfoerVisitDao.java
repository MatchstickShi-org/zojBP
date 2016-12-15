/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IInfoerVisitDao
{
	/**
	 * @param infoerVisit
	 * @return 
	 */
	Integer addInfoerVisit(InfoerVisit infoerVisit);
	/**
	 * @param pagination
	 * @param infoerId 
	 * @return
	 */
	DatagridVo<InfoerVisit> getAllInfoerVisit(Pagination pagination,Integer infoerId);

	
}