/**
 * 
 */
package com.decoration.bp.marketing.dao;

import com.decoration.bp.common.model.InfoerVisit;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

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
	
	/**
	 * @param salesmanIds
	 */
	Integer deleteBySalesmans(Integer... salesmanIds);
}