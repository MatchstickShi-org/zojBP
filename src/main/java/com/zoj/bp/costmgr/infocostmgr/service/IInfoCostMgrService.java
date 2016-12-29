package com.zoj.bp.costmgr.infocostmgr.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.infocostmgr.vo.InfoCost;

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
	 */
	Integer addInfoCostRecord(InfoCost infoCost);
}
