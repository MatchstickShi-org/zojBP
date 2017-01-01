/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangwei
 *
 */
public interface IInfoerDao
{
	Infoer getInfoerByName(String infoerName);

	/**
	 * @param id
	 * @return
	 */
	Infoer getInfoerById(Integer id);

	/**
	 * 更新信息员
	 * @param Infoer
	 * @return
	 */
	void updateInfoer(Infoer infoer);

	/**
	 * 分页查询信息员
	 * @param pagination
	 * @return
	 */
	DatagridVo<Infoer> getAllInfoer(Pagination pagination,User loginUser,String name,String tel,Integer... levels);

	/**
	 * 新增信息员
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

	/**
	 * 信息员业务转移
	 * @param infoerId
	 * @param salesmanId
	 * @return
	 */
	Integer updateInfoerSalesmanId(Integer[] infoerId, Integer salesmanId);

}