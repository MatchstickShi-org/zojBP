package com.decoration.bp.costmgr.infocostmgr.service;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.infocostmgr.vo.InfoCost;

/**
 * @author MatchstickShi
 */
public interface IInfoCostMgrService
{
	/**
	 * @param user 
	 * @param status
	 * @param orderId 
	 * @param clientName 
	 * @param pagination
	 * @return
	 * @throws BusinessException 
	 */
	DatagridVo<InfoCost> getAllInfoCosts(User user, Integer status, String clientName, String orderId, Pagination pagination) throws BusinessException;

	/**
	 * @param orderId
	 * @return
	 * @throws BusinessException 
	 */
	InfoCost getInfoCostByOrder(Integer orderId);

	/**
	 * @param infoCost
	 * @return
	 * @throws BusinessException 
	 */
	Integer addInfoCostRecord(InfoCost infoCost) throws BusinessException;
}
