package com.decoration.bp.marketing.service;

import com.decoration.bp.common.model.InfoCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

public interface IInfoCostService {

	/**
	 * @param pagination
	 * @param infoerId
	 * @param orderId
	 * @return
	 */
	DatagridVo<InfoCost> getAllInfoCost(Pagination pagination,Integer infoerId,Integer orderId);

	/**
	 * @param infoCost
	 * @return 
	 */
	Integer addInfoCost(InfoCost infoCost);
}
