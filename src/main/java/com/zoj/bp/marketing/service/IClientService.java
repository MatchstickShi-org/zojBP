package com.zoj.bp.marketing.service;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

public interface IClientService {

	Client getClientByName(String name);

	/**
	 * @param id
	 * @return
	 */
	Client getClientById(Integer id);

	/**
	 * @param client
	 * @return
	 */
	void updateClient(Client client);

	/**
	 * @param pagination
	 * @return
	 */
	DatagridVo<Client> getAllClient(Pagination pagination,User loginUser);

	/**
	 * @param client
	 * @return 
	 */
	Integer addClient(Client client);

	/**
	 * 根据订单Id获取客户
	 * @param orderId
	 * @return
	 */
	Client getClientByOrderId(Integer orderId);
}
