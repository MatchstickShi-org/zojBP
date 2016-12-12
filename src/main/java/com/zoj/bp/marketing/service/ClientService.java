package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IClientDao;

@Service
public class ClientService implements IClientService{

	@Autowired
	private IClientDao dao;
	@Override
	public Client getClientByName(String name) {
		return dao.getClientByName(name);
	}

	@Override
	public Client getClientById(Integer id) {
		return dao.getClientById(id);
	}

	@Override
	public void updateClient(Client client) {
		dao.updateClient(client);
	}

	@Override
	public DatagridVo<Client> getAllClient(Pagination pagination, User loginUser) {
		return dao.getAllClient(pagination, loginUser);
	}

	@Override
	public Integer addClient(Client client) {
		return dao.addClient(client);
	}

}
