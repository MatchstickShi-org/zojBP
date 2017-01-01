package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

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
