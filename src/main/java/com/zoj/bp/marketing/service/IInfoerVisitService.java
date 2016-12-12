package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IInfoerVisitService {

	/**
	 * @param pagination
	 * @param infoerId –≈œ¢‘±ID
	 * @return
	 */
	DatagridVo<InfoerVisit> getAllInfoerVisit(Pagination pagination,Integer infoerId);

	/**
	 * @param infoerVisit
	 * @return 
	 */
	Integer addInfoerVisit(InfoerVisit infoerVisit);
}
