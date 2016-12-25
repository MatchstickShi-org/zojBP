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
	DatagridVo<Infoer> getAllInfoer(Pagination pagination,User loginUser,String name,String tel,String[] level);

	/**
	 * @param infoer
	 * @return 
	 */
	Integer addInfoer(Infoer infoer);

	/**
	 * 
	 * @param tel
	 * @return
	 */
	Infoer findByTel(String tel);
	
	/**
	 * 根据业务员Id查询所有信息员
	 * @param salesmanId
	 * @param pagination 
	 * @return
	 */
	DatagridVo<Infoer> findBySalesmanId(Integer salesmanId, Pagination pagination);
}
