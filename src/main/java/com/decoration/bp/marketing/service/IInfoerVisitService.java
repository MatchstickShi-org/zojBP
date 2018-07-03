package com.decoration.bp.marketing.service;

import com.decoration.bp.common.model.InfoerVisit;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

public interface IInfoerVisitService {

	/**
	 * @param pagination
	 * @param infoerId ��ϢԱID
	 * @return
	 */
	DatagridVo<InfoerVisit> getAllInfoerVisit(Pagination pagination,Integer infoerId);

	/**
	 * @param infoerVisit
	 * @return 
	 */
	Integer addInfoerVisit(InfoerVisit infoerVisit);
}
