package com.zoj.bp.marketing.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IInfoerService {

	Infoer getInfoerByName(String InfoerName, User loginUser);

	/**
	 * @param id
	 * @param loginUser TODO
	 * @return
	 */
	Infoer getInfoerById(Integer id, User loginUser);

	/**
	 * @param Infoer
	 * @return
	 */
	void updateInfoer(Infoer infoer);

	/**
	 * @param pagination
	 * @param contains 
	 * @return
	 * @throws BusinessException 
	 */
	DatagridVo<Infoer> getAllInfoer(Pagination pagination, 
			User loginUser,String name,String tel, boolean containsUnderling, Integer... levels) throws BusinessException;

	/**
	 * @param infoer
	 * @return 
	 */
	Integer addInfoer(Infoer infoer);

	/**
	 * 
	 * @param tel
	 * @param loginUser TODO
	 * @return
	 */
	Infoer findByTel(Infoer infoer, User loginUser);
	
	/**
	 * 根据业务员Id查询所有信息员
	 * @param salesmanId
	 * @param pagination 
	 * @param loginUser TODO
	 * @return
	 */
	DatagridVo<Infoer> findBySalesmanId(Integer salesmanId, Pagination pagination, User loginUser);

	/**
	 * 信息员业务转移
	 * @param infoerId
	 * @param salesmanId
	 * @return
	 */
	Integer updateInfoerSalesmanId(Integer[] infoerId, Integer salesmanId);
}
