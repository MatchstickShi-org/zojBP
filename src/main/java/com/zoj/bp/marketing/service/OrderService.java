package com.zoj.bp.marketing.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IClientDao;
import com.zoj.bp.marketing.dao.IInfoerDao;
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
	
	@Autowired
	private IInfoerDao infoerDao;
	
	@Override
	public Order getOrderById(Integer id, User loginUser)
	{
		 Order o = dao.getOrderById(id);
		 o.hideAllTel(loginUser);
		 return o;
	}

	@Override
	public void updateOrder(Order order, Client client)
	{
		dao.updateOrder(order);
		clientDao.updateClient(client);
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser, Integer infoerId, Integer... status)
	{
		DatagridVo<Order> os = dao.getAllOrder(pagination, loginUser, infoerId, status);
		if(loginUser.isLeader())
			os.getRows().stream().forEach(o -> o.hideAllTel(loginUser));
		return os;
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, Integer salesmanId, Integer designerId, String name, String tel,
			String infoerName, String designerName, User loginUser,Integer... status)
	{
		DatagridVo<Order> os = dao.getAllOrder(pagination, salesmanId, designerId, name, tel, infoerName, designerName, status);
		if(loginUser.isLeader())
			os.getRows().stream().forEach(o -> o.hideAllTel(loginUser));
		return os;
	}
	
	@Override
	public Integer addOrder(Order order)
	{
		return dao.addOrder(order);
	}

	@Override
	public void addOrderAndClient(Order order)
	{
		int orderId = this.addOrder(order);
		if(orderId > 0)
		{
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
		Infoer infoer = infoerDao.getInfoerById(order.getInfoerId());
		/**
		 * 如果当前信息员等级为铁牌，则新增客户的时候更新等级为铜牌
		 */
		if (infoer.getLevel() == 4)
		{
			infoer.setLevel(3);
			infoerDao.updateInfoer(infoer);
		}
	}

	@Override
	public Order findByTel(Order order, User loginUser)
	{
		order = clientDao.getClientByTel(order);
		Optional.ofNullable(order).ifPresent(o -> o.hideAllTel(loginUser));
		return order;
	}

	@Override
	public Integer deleteOrderByIds(Integer[] orderIds)
	{
		return dao.updateOrderByIds(orderIds);
	}

	@Override
	public Integer addOrderApprove(OrderApprove orderApprove)
	{
		approveDao.addOrderApprove(orderApprove);
		Order order = dao.getOrderById(orderApprove.getOrderId());
		Infoer infoer = infoerDao.getInfoerById(order.getInfoerId());
		switch (orderApprove.getOperate())
		{
			case 0:		//驳回
				switch (order.getStatus())
				{
					case 30:
						order.setStatus(10);
						break;
					case 32:
						order.setStatus(30);
						break;
					case 34:
						order.setStatus(0);
						break;
				}
				break;
			case 1:		//批准
				switch (order.getStatus())
				{
					case 30:
						order.setStatus(32);
						break;
					case 32:
						/**
						 * 如果客户为在谈单，则更新该客户的信息员等级为银牌
						 */
						infoer.setLevel(2);
						order.setStatus(34);
						break;
					case 34:
						/**
						 * 如果客户为已签单，则更新该客户的信息员等级为金牌
						 */
						infoer.setLevel(1);
						order.setStatus(90);
						break;
					case 60:
						order.setStatus(62);
						break;
					case 62:
						order.setStatus(64);
						break;
				}
				break;
			case 2:		//申请
				switch (order.getStatus())
				{
					case 10:
						order.setStatus(30);
						break;
					case 34:
						order.setStatus(60);
						break;
				}
				break;
			case 3:		//打回
				order.setStatus(14);
				break;
		}
		int status = dao.updateOrderStatus(order);
		if(status > 0)
			infoerDao.updateInfoer(infoer);
		return status;
	}

	@Override
	public Integer updateOrderSalesmanId(Integer[] orderIds, Integer salesmanId)
	{
		return dao.updateOrderSalesmanId(orderIds, salesmanId);
	}

	@Override
	public Integer updateOrderDesigerId(Integer[] orderIds, Integer designerId)
	{
		return dao.updateOrderDesigerId(orderIds, designerId);
	}

	@Override
	public Integer updateOrderSalesmanIdByInfoers(Integer[] infoerIds, Integer salesmanId)
	{
		return dao.updateOrderSalesmanIdByInfoers(infoerIds, salesmanId);
	}
}