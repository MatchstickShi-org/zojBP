package com.zoj.bp.costmgr.infocostmgr.dao;

import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.infocostmgr.vo.InfoCost;

/**
 * @author MatchstickShi
 */
public interface IInfoCostMgrDao
{

	/**
	 * @param user
	 * @param status
	 * @param orderId 
	 * @param clientName 
	 * @param pagination
	 */
	DatagridVo<InfoCost> getAllInfoCosts(User user, Integer status, String clientName, String orderId, Pagination pagination);

	/**
	 * @param orderId
	 * @return
	 */
	InfoCost getInfoCostByOrder(Integer orderId);

	/**
	 * @param infoCost
	 * @return
	 */
	Integer addInfoCostRecord(InfoCost infoCost);

}