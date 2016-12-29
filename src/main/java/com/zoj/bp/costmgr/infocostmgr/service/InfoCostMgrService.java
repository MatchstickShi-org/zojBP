package com.zoj.bp.costmgr.infocostmgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.infocostmgr.dao.IInfoCostMgrDao;
import com.zoj.bp.costmgr.infocostmgr.vo.InfoCost;

/**
 * @author MatchstickShi
 */
@Service
public class InfoCostMgrService implements IInfoCostMgrService
{
	@Autowired
	private IInfoCostMgrDao infoCostDao;
	
	@Override
	public DatagridVo<InfoCost> getAllInfoCosts(
			User user, Integer status, String clientName, String orderId, Pagination pagination) throws BusinessException
	{
		if(user.isDesignLeader() || user.isDesignDesigner() || user.isDesignManager() || user.isAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查询。"));
		return infoCostDao.getAllInfoCosts(user, status, clientName, orderId, pagination);
	}

	@Override
	public InfoCost getInfoCostByOrder(Integer orderId)
	{
		return infoCostDao.getInfoCostByOrder(orderId);
	}
	
	@Override
	public Integer addInfoCostRecord(InfoCost infoCost)
	{
		return infoCostDao.addInfoCostRecord(infoCost);
	}
}