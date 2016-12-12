/**
 * 
 */
package com.zoj.bp.marketing.dao;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author wangw
 *
 */
public interface IClientDao
{
	Client getClientByName(String name);

	/**
	 * @param id
	 * @return
	 */
	Client getClientById(Integer id);

	/**
	 * 更新客户信息
	 * @param client
	 * @return
	 */
	void updateClient(Client client);

	/**
	 * 分页查询
	 * @param pagination
	 * @return
	 */
	DatagridVo<Client> getAllClient(Pagination pagination,User loginUser);

	/**
	 * 新增客户
	 * @param client
	 * @return 
	 */
	Integer addClient(Client client);

}