package com.decoration.bp.marketing.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.excption.ReturnCode;
import com.decoration.bp.common.model.Infoer;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.marketing.dao.IInfoerDao;
import com.decoration.bp.marketing.dao.IOrderDao;
import com.decoration.bp.marketing.dao.IOrderVisitDao;
import com.decoration.bp.sysmgr.usermgr.dao.IUserDao;

@Service
public class InfoerService implements IInfoerService{

	@Autowired
	private IInfoerDao infoerDao;
	
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IOrderVisitDao orderVisitDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Override
	public Infoer getInfoerByName(String InfoerName, User loginUser)
	{
		Infoer infoer = infoerDao.getInfoerByName(InfoerName);
		Optional.ofNullable(infoer).ifPresent(i -> i.hideAllTel(loginUser));
		return infoer;
	}

	@Override
	public Infoer getInfoerById(Integer id, User loginUser)
	{
		Infoer infoer = infoerDao.getInfoerById(id);
		Optional.ofNullable(infoer).ifPresent(i -> i.hideAllTel(loginUser));
		return infoer;
	}

	@Override
	public void updateInfoer(Infoer infoer)
	{
		infoerDao.updateInfoer(infoer);
	}

	@Override
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination, User loginUser, 
			String name, String tel, Integer salesmanId, boolean containsUnderling, Integer isWait, Integer... levels) throws BusinessException
	{
		if(!loginUser.isBelongMarketing())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查看信息员。"));
		User dbUser = userDao.getUserById(loginUser.getId());
		DatagridVo<Infoer> is;
		if(dbUser.isLeader() && dbUser.getGroupId() == null)		//主管，尚未分配组
			is = infoerDao.getInfoersBySalesman(pagination, dbUser, name, tel, salesmanId, isWait, levels);
		else
		{
			if(containsUnderling)
				is = infoerDao.getAllInfoer(pagination, loginUser, name, tel, salesmanId, isWait, levels);
			else
				is = infoerDao.getInfoersBySalesman(pagination, dbUser, name, tel, salesmanId, isWait, levels);
		}
		if(loginUser.isLeader())
			is.getRows().stream().forEach(i -> i.hideAllTel(loginUser));
		return is;
	}

	@Override
	public Integer addInfoer(Infoer infoer) {
		return infoerDao.addInfoer(infoer);
	}

	@Override
	public List<Infoer> findByTel(Infoer infoer, User loginUser)
	{
		List<Infoer> infoerList = infoerDao.findByTel(infoer);
		if(CollectionUtils.isNotEmpty(infoerList)){
			infoer = infoerList.get(0);
			Optional.ofNullable(infoer).ifPresent(i -> i.hideAllTel(loginUser));
		}
		return infoerList;
	}

	@Override
	public DatagridVo<Infoer> findBySalesmanId(Integer salesmanId, Pagination pagination, User loginUser)
	{
		DatagridVo<Infoer> is = infoerDao.findBySalesmanId(salesmanId,pagination);
		if(loginUser.isLeader())
			is.getRows().stream().forEach(i -> i.hideAllTel(loginUser));
		return is;
	}

	@Override
	public Integer updateInfoerSalesmanId(Integer[] infoerIds, Integer salesmanId)
	{
		orderDao.updateOrderSalesmanIdByInfoers(infoerIds, salesmanId);
		orderVisitDao.updateVisitorIdByInfoers(infoerIds,salesmanId);
		return infoerDao.updateInfoerSalesmanId(infoerIds,salesmanId);
	}
}