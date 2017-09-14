package com.zoj.bp.marketing.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.dao.IMsgLogDao;
import com.zoj.bp.common.dao.IOrderApproveDao;
import com.zoj.bp.common.dao.IOrderChangeLogDao;
import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.Infoer.Level;
import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.OrderApprove.Operate;
import com.zoj.bp.common.model.OrderChangeLog;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.model.User.Role;
import com.zoj.bp.common.msg.MsgManager;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IClientDao;
import com.zoj.bp.marketing.dao.IInfoerDao;
import com.zoj.bp.marketing.dao.IOrderDao;
import com.zoj.bp.marketing.dao.IOrderVisitDao;
import com.zoj.bp.sysmgr.usermgr.dao.IUserDao;

@Service
public class OrderService implements IOrderService
{
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IClientDao clientDao;
	
	@Autowired
	private IOrderApproveDao approveDao;
	
	@Autowired
	private IOrderVisitDao orderVisitDao;
	
	@Autowired
	private IOrderChangeLogDao orderChangeLogDao;
	
	@Autowired
	private IInfoerDao infoerDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IMsgLogDao msgDao;
	
	@Autowired
	private IClientService clientSvc;
	
	@Override
	public Order getOrderById(Integer id, User loginUser)
	{
		 Order o = orderDao.getOrderById(id);
		 o.hideAllTel(loginUser);
		 return o;
	}

	@Override
	public void updateOrder(Order order, User loginUser)
	{
		Client client = clientSvc.getClientByOrderId(order.getId());
		client.setName(order.getName());
		client.setOrgAddr(order.getOrgAddr());
		client.setIsKey(order.getIsKey());
		client.setIsWait(order.getIsWait());
		client.setTel2(order.getTel2());
		client.setTel3(order.getTel3());
		client.setTel4(order.getTel4());
		client.setTel5(order.getTel5());
		Order dbOrder = getOrderById(order.getId(), loginUser);
		dbOrder.setProjectName(order.getProjectName());
		dbOrder.setProjectAddr(order.getProjectAddr());
		
		orderDao.updateOrder(dbOrder);
		clientDao.updateClient(client);
	}

	@Override
	public DatagridVo<Order> getOrdersByInfoer(Pagination pagination, User loginUser, Integer infoerId, Integer... status)
	{
		DatagridVo<Order> os = orderDao.getOrdersByInfoer(pagination, infoerId, status);
		if(loginUser.isLeader())
			os.getRows().stream().forEach(o -> o.hideAllTel(loginUser));
		return os;
	}

	@Override
	public DatagridVo<Order> getOrdersByUser(User loginUser, Pagination pagination, String clientName,Integer orderId,
			String tel, String infoerName, Integer salesmanOrDesignerId, Integer isKey, Integer isWait, Integer... status)
	{
		User dbUser = userDao.getUserById(loginUser.getId());
		DatagridVo<Order> os = null;
		if(dbUser.isLeader() && dbUser.getGroupId() == null)		//主管，尚未分配组
		{
			if(dbUser.isMarketingLeader())
				os = this.getOrdersBySalesman(
						dbUser, pagination, clientName, orderId, tel, infoerName, salesmanOrDesignerId, isKey, isWait, status);
			else
				os = this.getOrdersByDesigner(
						pagination, dbUser, clientName, orderId, tel, infoerName, salesmanOrDesignerId, status);
		}
		else
			os = orderDao.getOrdersByUser(
					pagination, loginUser, clientName, orderId, tel, infoerName, salesmanOrDesignerId, isKey, isWait, status);
		
		if(loginUser.isLeader())
			os.getRows().stream().forEach(o -> o.hideAllTel(loginUser));
		return os;
	}
	
	@Override
	public DatagridVo<Order> getOrdersBySalesman(User salesman, Pagination pagination, 
			String clientName,Integer orderId, String tel, String infoerName, Integer salesmanId, Integer isKey, Integer isWait, Integer... status)
	{
		return orderDao.getOrdersBySalesman(pagination, salesman, clientName,orderId, tel, infoerName, salesmanId, isKey, isWait, status);
	}
	
	@Override
	public DatagridVo<Order> getOrdersByDesigner(Pagination pagination, User designer, 
			String clientName,Integer orderId, String tel, String infoerName, Integer designerId, Integer... status)
	{
		return orderDao.getOrdersByDesigner(pagination, designer, clientName,orderId, tel, infoerName, designerId, status);
	}

	@Override
	public Integer addOrder(Order order)
	{
		return orderDao.addOrder(order);
	}

	@Override
	public void addOrderAndClient(Order order,User loginUser) throws BusinessException
	{
		Order orderTel = null;
		if(order != null)
		{
			List<String> tels = order.getTels();
			orderTel = this.findByTels(loginUser, tels.toArray(new String[tels.size()]));
			if(orderTel != null)
				throw new BusinessException(ReturnCode.TEL_EXISTS.setMsg(
						"重复！该客户[" + order.getId() + "]于 "+orderTel.getInsertTime()+" 被业务员["+orderTel.getSalesmanName()+"]录入。"));
		}
		if(!loginUser.isSuperAdmin())
			order.setSalesmanId(loginUser.getId());
		order.setStatus(Status.tracing.value());//订单初始状态设为正跟踪
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
			client.setIsKey(order.getIsKey());
			List<String> tels = order.getTels();
			int clientId = clientDao.addClientDistinctTel(client, tels.toArray(new String[tels.size()]));
			if(clientId <1){
				throw new BusinessException(ReturnCode.TEL_EXISTS.setMsg("重复！该客户已被录入。"));
			}
			/**
			 * 订单状态变动日志
			 */
			OrderChangeLog orderChangeLog = new OrderChangeLog();
			orderChangeLog.setOrderId(orderId);
			orderChangeLog.setStatus(order.getStatus());
			orderChangeLogDao.addOrderChangeLog(orderChangeLog);
			
			Infoer infoer = infoerDao.getInfoerById(order.getInfoerId());
			/**
			 * 如果当前信息员等级为铁牌，则新增客户的时候更新等级为铜牌
			 */
			if (infoer.setInfoerLevel(Level.bronze.value()))
			{
				infoerDao.updateInfoer(infoer);
			}
		}
		
	}
	
	@Override
	public void addNewOrderAndClient(Order order)
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
			client.setIsKey(order.getIsKey());
			clientDao.addClient(client);
		}
		/**
		 * 订单状态变动日志
		 */
		OrderChangeLog orderChangeLog = new OrderChangeLog();
		orderChangeLog.setOrderId(orderId);
		orderChangeLog.setStatus(order.getStatus());
		orderChangeLogDao.addOrderChangeLog(orderChangeLog);
		
		Infoer infoer = infoerDao.getInfoerById(order.getInfoerId());
		/**
		 * 如果当前信息员等级为铁牌，则新增客户的时候更新等级为铜牌
		 */
		if (infoer.getLevel() == Level.iron.value())
		{
			infoer.setLevel(Level.bronze.value());
			infoerDao.updateInfoer(infoer);
		}
	}
	
	@Override
	public Order findByTels(User loginUser, String... tels)
	{
		List<Order> os = clientDao.getClientByTels(tels);
		if(CollectionUtils.isNotEmpty(os))
		{
			os.get(0).hideAllTel(loginUser);
			return os.get(0);
		}
		return null;
	}

	@Override
	public Integer giveUpOrders(User loginUser,Integer... orderIds)
	{
		orderDao.updateOrderStatus(Status.abandoned, orderIds);
		for(int i = 0 ;i < orderIds.length; i++){
			/**
			 * 审批流程信息
			 */
			OrderApprove orderApprove = new OrderApprove();
			orderApprove.setOperate(Operate.reject.value());
			orderApprove.setClaimer(loginUser.getId());
			orderApprove.setStatus(Status.abandoned.value());
			orderApprove.setOrderId(orderIds[i]);
			approveDao.addOrderApprove(orderApprove);
			/**
			 * 状态变更日志
			 */
			OrderChangeLog orderChangeLog = new OrderChangeLog();
			orderChangeLog.setOrderId(orderIds[i]);
			orderChangeLog.setStatus(Status.abandoned.value());
			orderChangeLogDao.addOrderChangeLog(orderChangeLog);
		}
		return 1;
	}

	@Override
	public Integer addOrderApprove(OrderApprove orderApprove,Integer orderStatus) throws BusinessException
	{
		synchronized (String.valueOf(orderApprove.getOrderId()).intern())
		{
			Order order = orderDao.getOrderById(orderApprove.getOrderId());
			if(!order.getStatus().equals(orderStatus))
				throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，数据已过期，请刷新后再试。"));
			
			if(orderApprove.getDesignerId() !=null && orderApprove.getDesignerId() > 0)
				order.setDesignerId(orderApprove.getDesignerId());
			Infoer infoer = infoerDao.getInfoerById(order.getInfoerId());
			boolean updateInfoerFlag = false;
			List<MsgLog> msgs = new ArrayList<>();
			//订单状态变动日志记录
			OrderChangeLog orderChangeLog = new OrderChangeLog();
			orderChangeLog.setOrderId(orderApprove.getOrderId());
			
			switch (Operate.valueOf(orderApprove.getOperate()))
			{
				case reject:		//驳回操作
					switch (Status.valueOf(order.getStatus()))
					{
						case rejectingDesignManagerAuditing://状态为：打回中-主案部经理审核中：驳回-更新为在谈单-设计师跟踪中
							order.setStatus(Status.talkingDesignerTracing.value());
							orderApprove.setStatus(Status.talkingDesignerTracing.value());
							orderChangeLog.setStatus(Status.talkingDesignerTracing.value());
							msgs.add(new MsgLog(order.getDesignerId(), 
									MessageFormat.format("你提交的在谈单[{0}]打回申请被主案部经理打回，请知悉。", order.getId())));
							break;
						case rejectingMarketingManagerAuditing://状态为：打回中-商务部经理审核中：驳回-更新为打回中-主案部经理审核中
							order.setStatus(Status.rejectingDesignManagerAuditing.value());
							orderApprove.setStatus(Status.rejectingDesignManagerAuditing.value());
							orderChangeLog.setStatus(Status.rejectingDesignManagerAuditing.value());
							msgs.add(new MsgLog(order.getSalesmanId(), 
									MessageFormat.format("你提交的在谈单[{0}]打回申请被商务部经理打回，请知悉。", order.getId())));
							break;
						case talkingMarketingManagerAuditing://状态为：在谈单-商务部经理审核中：驳回-更新为正跟踪
							order.setStatus(Status.tracing.value());
							orderApprove.setStatus(Status.tracing.value());
							orderChangeLog.setStatus(Status.tracing.value());
							msgs.add(new MsgLog(order.getSalesmanId(), 
									MessageFormat.format("你提交的在谈单[{0}]申请被商务部经理打回，请知悉。", order.getId())));
							break;
						case talkingDesignManagerAuditing://状态为：在谈单-主案部经理审核中：驳回-打回到商务部经理
							order.setStatus(Status.talkingMarketingManagerAuditing.value());
							orderApprove.setStatus(Status.talkingMarketingManagerAuditing.value());
							orderChangeLog.setStatus(Status.talkingMarketingManagerAuditing.value());
							List<User> targetUsers = userDao.getUsersByRole(Role.marketingManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(), 
										MessageFormat.format("你提交的在谈单[{0}]申请被主案部经理打回，请知悉。", order.getId()))));
							break;
						case talkingDesignerTracing://状态为：在谈单-设计师跟踪中：判定为死单
							order.setStatus(Status.dead.value());
							orderApprove.setStatus(Status.dead.value());
							orderChangeLog.setStatus(Status.dead.value());
							msgs.add(new MsgLog(order.getSalesmanId(), 
									MessageFormat.format("你的在谈单[{0}]被设计师[{1}]判定为死单，请知悉。",
											order.getId(), order.getDesignerName())));
							break;
						case disagreeDesignManagerAuditing://状态为：不准单-主案部经理审核中：驳回->设计师跟踪中
							order.setStatus(Status.talkingDesignerTracing.value());
							orderApprove.setStatus(Status.talkingDesignerTracing.value());
							orderChangeLog.setStatus(Status.talkingDesignerTracing.value());
							msgs.add(new MsgLog(order.getDesignerId(), 
									MessageFormat.format("你提交的不准单[{0}]申请被主案部经理驳回，请知悉。", order.getId())));
							break;
						case disagreeMarketingManagerAuditing://状态为：不准单-商务部经理审核中：驳回->主案部经理审核中
							order.setStatus(Status.disagreeDesignManagerAuditing.value());
							orderApprove.setStatus(Status.disagreeDesignManagerAuditing.value());
							orderChangeLog.setStatus(Status.disagreeDesignManagerAuditing.value());
							targetUsers = userDao.getUsersByRole(Role.designManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(),
										MessageFormat.format("你提交的不准单[{0}]申请被商务部经理驳回，请知悉。", order.getId()))));
							break;
					default:
						break;
					}
					break;
				case permit:		//批准操作
					switch (Status.valueOf(order.getStatus()))
					{
						case rejectingDesignManagerAuditing://状态为：打回中-主案部经理审核中：提交商务部部经理审核
							order.setStatus(Status.rejectingMarketingManagerAuditing.value());
							orderApprove.setStatus(Status.rejectingMarketingManagerAuditing.value());
							orderChangeLog.setStatus(Status.rejectingMarketingManagerAuditing.value());
							List<User> targetUsers = userDao.getUsersByRole(Role.marketingManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
							{
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(),
										MessageFormat.format("主案部经理批准了在谈单[{0}]的打回申请，请尽快审核。", order.getId()))));
							}
							break;
						case rejectingMarketingManagerAuditing://状态为：打回中-商务部经理审核中：设计师已打回
							order.setStatus(Status.designerRejected.value());
							orderApprove.setStatus(Status.designerRejected.value());
							orderChangeLog.setStatus(Status.designerRejected.value());
							msgs.add(new MsgLog(order.getSalesmanId(), 
									MessageFormat.format("你的在谈单[{0}]被设计师[{1}]打回，请知悉。", order.getId(), orderApprove.getDesignerName())));
							break;
						case talkingMarketingManagerAuditing://状态为：在谈单-商务部经理审核中：提交主案部经理审核
							order.setStatus(Status.talkingDesignManagerAuditing.value());
							orderApprove.setStatus(Status.talkingDesignManagerAuditing.value());
							orderChangeLog.setStatus(Status.talkingDesignManagerAuditing.value());
							targetUsers = userDao.getUsersByRole(Role.designManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
							{
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(),
										MessageFormat.format("商务部经理批准了在谈单[{0}]的申请，请尽快审核。", order.getId()))));
							}
							break;
						case talkingDesignManagerAuditing://状态为：在谈单-主案部经理审核中：分配设计师跟踪
							/** 如果客户为在谈单，则更新该客户的信息员等级为银牌 */
							if(infoer.setInfoerLevel(Level.silver.value())){//信息员等级为银牌以下才更新
								updateInfoerFlag = true;
							}
							order.setStatus(Status.talkingDesignerTracing.value());
							orderApprove.setStatus(Status.talkingDesignerTracing.value());
							orderChangeLog.setStatus(Status.talkingDesignerTracing.value());
							msgs.add(new MsgLog(order.getDesignerId(), 
									MessageFormat.format("主案部经理批准了在谈单[{0}]的申请，并分配你为设计师，请尽快跟踪。", order.getId())));
							break;
						case talkingDesignerTracing://状态为：在谈单-设计师跟踪中：更新为已签单
							/** 如果客户为已签单，则更新该客户的信息员等级为金牌 */
							if(infoer.setInfoerLevel(Level.gold.value())){//信息员等级为金牌以下才更新
								updateInfoerFlag = true;
							}
							order.setStatus(Status.deal.value());
							order.setDealAmount(orderApprove.getDealAmount());
							orderApprove.setStatus(Status.deal.value());
							orderChangeLog.setStatus(Status.deal.value());
							String msg = MessageFormat.format(
									"在谈单[{0}]已被设计师[{1}]签单，请知悉。", order.getId(), order.getDesignerName());
							msgs.add(new MsgLog(order.getSalesmanId(), msg));
							targetUsers = userDao.getUsersByRole(Role.marketingManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(), msg)));
							targetUsers = userDao.getUsersByRole(Role.designManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(), msg)));
							break;
						case disagreeDesignManagerAuditing://状态为：不准单-主案部经理审核中：提交商务部经理审核
							order.setStatus(Status.disagreeMarketingManagerAuditing.value());
							orderApprove.setStatus(Status.disagreeMarketingManagerAuditing.value());
							orderChangeLog.setStatus(Status.disagreeMarketingManagerAuditing.value());
							targetUsers = userDao.getUsersByRole(Role.marketingManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(), 
										MessageFormat.format("主案部经理批准了在谈单[{0}]的不准单申请，请尽快审核。", order.getId()))));
							break;
						case disagreeMarketingManagerAuditing://状态为：不准单-商务部经理审核中：通过-更新为不准单
							order.setStatus(Status.disagree.value());
							orderApprove.setStatus(Status.disagree.value());
							orderChangeLog.setStatus(Status.disagree.value());
							msgs.add(new MsgLog(order.getSalesmanId(), 
									MessageFormat.format("你的在谈单[{0}]被商务部经理判定为不准单，请知悉。", order.getId())));
							msgs.add(new MsgLog(order.getDesignerId(), 
									MessageFormat.format("你提交的不准单[{0}]申请已通过，请知悉。", order.getId())));
							break;
						default:
							break;
					}
					break;
				case apply:		//申请操作
					switch (Status.valueOf(order.getStatus()))
					{
						case tracing://状态为：正跟踪
						case designerRejected://状态为：在谈单-设计师已打回
							order.setStatus(Status.talkingMarketingManagerAuditing.value());
							orderApprove.setStatus(Status.talkingMarketingManagerAuditing.value());
							orderChangeLog.setStatus(Status.talkingMarketingManagerAuditing.value());
							List<User> targetUsers = userDao.getUsersByRole(Role.marketingManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
							{
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(), 
										MessageFormat.format("业务员[{0}]申请了在谈单，请尽快审核。", orderApprove.getClaimerName()))));
							}
							break;
						case disagreeDesignManagerAuditing://状态为：不准单-主案部经理审核中：判定为死单
							order.setStatus(Status.dead.value());
							orderApprove.setStatus(Status.dead.value());
							orderChangeLog.setStatus(Status.dead.value());
							msgs.add(new MsgLog(order.getSalesmanId(),
									MessageFormat.format("你的在谈单[{0}]被主案部经理判定为死单，请知悉。", order.getId())));
							msgs.add(new MsgLog(order.getDesignerId(),
									MessageFormat.format("你提交的不准单申请[{0}]被主案部经理判定为死单，请知悉。", order.getId())));
							break;
						case talkingDesignerTracing://状态为：在谈单-设计师跟踪中：申请不准单
							order.setStatus(Status.disagreeDesignManagerAuditing.value());
							orderApprove.setStatus(Status.disagreeDesignManagerAuditing.value());
							orderChangeLog.setStatus(Status.disagreeDesignManagerAuditing.value());
							targetUsers = userDao.getUsersByRole(Role.designManager.value());
							if(CollectionUtils.isNotEmpty(targetUsers))
							{
								targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(),
										MessageFormat.format("设计师[{0}]对在谈单[{1}]申请了不准单，请尽快审核。", 
												orderApprove.getClaimerName(), order.getId()))));
							}
							break;
					default:
						break;
					}
					break;
				case repulse:		//打回操作
					order.setStatus(Status.rejectingDesignManagerAuditing.value());
					orderApprove.setStatus(Status.rejectingDesignManagerAuditing.value());
					orderChangeLog.setStatus(Status.rejectingDesignManagerAuditing.value());
					List<User> targetUsers = userDao.getUsersByRole(Role.designManager.value());
					if(CollectionUtils.isNotEmpty(targetUsers))
					{
						targetUsers.stream().forEach(u -> msgs.add(new MsgLog(u.getId(),
								MessageFormat.format("设计师[{0}]对在谈单[{1}]申请了打回，请尽快审核。", 
										orderApprove.getDesignerName(), order.getId()))));
					}
					break;
			}
			/**
			 * 新增审批流程记录
			 */
			approveDao.addOrderApprove(orderApprove);
			
			int status = orderDao.updateOrderStatus(order);
			/**
			 * 新增状态变动日志记录
			 */
			orderChangeLogDao.addOrderChangeLog(orderChangeLog);
			if(status > 0 && updateInfoerFlag)
				infoerDao.updateInfoer(infoer);
			if(CollectionUtils.isNotEmpty(msgs))		//发送消息
			{
				msgs.stream().forEach(m ->
				{
					msgDao.addMsgLog(m);
					MsgManager.instance().addMsg(m);
				});
			}
			return status;
		}
	}

	@Override
	public Integer updateOrderSalesmanId(Integer[] orderIds, Integer salesmanId)
	{
		orderVisitDao.updateVisitorIdByOrderIds(orderIds, salesmanId);
		return orderDao.updateOrderSalesmanId(orderIds, salesmanId);
	}

	@Override
	public Integer updateOrderDesigerId(Integer[] orderIds, Integer designerId)
	{
		orderVisitDao.updateVisitorIdByOrderIds(orderIds, designerId);
		return orderDao.updateOrderDesigerId(orderIds, designerId);
	}

	@Override
	public Integer updateOrderSalesmanIdByInfoers(Integer[] infoerIds, Integer salesmanId)
	{
		return orderDao.updateOrderSalesmanIdByInfoers(infoerIds, salesmanId);
	}

	@Override
	public DatagridVo<Order> getOrdersByStatus(
			User loginUser, String clientName, Integer orderId, Pagination pagination, Status... status) throws Exception
	{
		return orderDao.getOrdersByStatus(loginUser, clientName, orderId, pagination, status);
	}

	@Override
	public Integer setOrder2Tracing(Integer orderId,User loginUser) throws BusinessException
	{
		Order order = orderDao.getOrderById(orderId);
		if (order.getStatus() != Status.abandoned.value())		//只有已放弃客户才能设置为正跟踪
			throw new BusinessException(ReturnCode.ILLEGALITY_OPERATION.setMsg("只有已放弃状态的客户才能设置为正跟踪客户。"));
		/**
		 * 新增审批流程记录
		 */
		OrderApprove orderApprove = new OrderApprove();
		orderApprove.setClaimer(order.getSalesmanId());
		orderApprove.setApprover(loginUser.getId());
		orderApprove.setStatus(Status.tracing.value());
		orderApprove.setOperate(Operate.permit.value());
		orderApprove.setOrderId(orderId);
		approveDao.addOrderApprove(orderApprove);
		
		/**
		 * 新增状态变动记录
		 */
		OrderChangeLog orderChangeLog = new OrderChangeLog();
		orderChangeLog.setStatus(Status.tracing.value());
		orderChangeLog.setOrderId(order.getId());
		orderChangeLogDao.addOrderChangeLog(orderChangeLog);
		
		return orderDao.updateOrderStatus(Status.tracing, order.getId());
	}
}