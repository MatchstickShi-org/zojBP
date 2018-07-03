package com.decoration.bp.costmgr.infocostmgr.dao;

import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.infocostmgr.vo.InfoCost;

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