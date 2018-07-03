package com.decoration.bp.costmgr.infocostmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.excption.ReturnCode;
import com.decoration.bp.common.model.Order;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.model.Order.Status;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.infocostmgr.dao.IInfoCostMgrDao;
import com.decoration.bp.costmgr.infocostmgr.vo.InfoCost;
import com.decoration.bp.marketing.dao.IOrderDao;

/**
 * @author MatchstickShi
 */
@Service
public class InfoCostMgrService implements IInfoCostMgrService
{
	@Autowired
	private IInfoCostMgrDao infoCostDao;
	
	@Autowired
	private IOrderDao orderDao;
	
	@Override
	public DatagridVo<InfoCost> getAllInfoCosts(
			User user, Integer status, String clientName, String orderId, Pagination pagination) throws BusinessException
	{
		if(!user.isBelongMarketing())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是商务部人员，无法查询。"));
		return infoCostDao.getAllInfoCosts(user, status, clientName, orderId, pagination);
	}

	@Override
	public InfoCost getInfoCostByOrder(Integer orderId)
	{
		return infoCostDao.getInfoCostByOrder(orderId);
	}
	
	@Override
	public Integer addInfoCostRecord(InfoCost infoCost) throws BusinessException
	{
		Order order = orderDao.getOrderById(infoCost.getOrderId());
		if(order.getStatus() == Status.abandoned.value() || order.getStatus() >= Status.talkingDesignerTracing.value())
			return infoCostDao.addInfoCostRecord(infoCost);
		throw new BusinessException(ReturnCode.ILLEGALITY_OPERATION.setMsg("操作失败，只有在谈单才能新增信息费。"));
	}
}