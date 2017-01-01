package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IClientDao;
import com.zoj.bp.marketing.dao.IOrderApproveDao;
import com.zoj.bp.marketing.dao.IOrderDao;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private IOrderDao dao;
	
	@Autowired
	private IClientDao clientDao;
	
	@Autowired
	private IOrderApproveDao approveDao;
	
	@Override
	public Order getOrderById(Integer id) {
		return dao.getOrderById(id);
	}

	@Override
	public void updateOrder(Order order,Client client) {
		dao.updateOrder(order);
		clientDao.updateClient(client);
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination,User loginUser,Integer infoerId,Integer[] status) {
		return dao.getAllOrder(pagination, loginUser,infoerId,status);
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser ,String name, String tel,
			String infoerName, String[] status) {
		return dao.getAllOrder(pagination, loginUser,name,tel,infoerName, status);
	}
	
	@Override
	public Integer addOrder(Order order) {
		return dao.addOrder(order);
	}

	@Override
	public void addOrderAndClient(Order order) {
		int orderId = this.addOrder(order);
		if(orderId > 0){
			Client client = new Client();
			client.setName(order.getName());
			client.setOrgAddr(order.getOrgAddr());
			client.setTel1(order.getTel1());
			client.setTel2(order.getTel2());
			client.setTel3(order.getTel3());
			client.setTel4(order.getTel4());
			client.setTel5(order.getTel5());
			client.setOrderId(orderId);
			clientDao.addClient(client);
		}
	}

	@Override
	public Order findByTel(String tel) {
		return clientDao.getClientByTel(tel);
	}

	@Override
	public Integer deleteOrderByIds(Integer[] orderIds) {
		return dao.updateOrderByIds(orderIds);
	}

	@Override
	public Integer addOrderApprove(OrderApprove orderApprove) {
		approveDao.addOrderApprove(orderApprove);
		Order order = dao.getOrderById(orderApprove.getOrderId());
		if(order.getStatus() == 10)
			order.setStatus(30);
		else if(order.getStatus() == 30)
			order.setStatus(32);
		else if(order.getStatus() == 62)
			order.setStatus(64);
		return dao.updateOrderStatus(order);
	}

	@Override
	public Integer updateOrderSalesmanId(Integer[] orderIds, Integer salesmanId) {
		return dao.updateOrderSalesmanId(orderIds, salesmanId);
	}

	@Override
	public Integer updateOrderDesigerId(Integer[] orderIds, Integer designerId) {
		return dao.updateOrderDesigerId(orderIds, designerId);
	}

	@Override
	public Integer updateOrderSalesmanIdByInfoers(Integer[] infoerIds, Integer salesmanId) {
		return dao.updateOrderSalesmanIdByInfoers(infoerIds, salesmanId);
	}
}
