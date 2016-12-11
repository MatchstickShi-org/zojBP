/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IInfoCostDao
{
	/**
	 * @param infoCost
	 * @return 
	 */
	Integer addInfoCost(InfoCost infoCost);
	/**
	 * @param pagination
	 * @param infoer
	 * @return
	 */
	DatagridVo<InfoCost> getAllInfoCost(Pagination pagination,Infoer infoer);

	
}