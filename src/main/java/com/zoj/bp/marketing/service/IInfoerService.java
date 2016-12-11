package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IInfoerService {

	Infoer getInfoerByName(String InfoerName);

	/**
	 * @param id
	 * @return
	 */
	Infoer getInfoerById(Integer id);

	/**
	 * @param Infoer
	 * @return
	 */
	void updateInfoer(Infoer infoer);

	/**
	 * @param pagination
	 * @return
	 */
	DatagridVo<Infoer> getAllInfoer(Pagination pagination,User loginUser);

	/**
	 * @param Infoer
	 * @return 
	 */
	Integer addInfoer(Infoer Infoer);
}
