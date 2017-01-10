package com.zoj.bp.marketing.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IInfoerDao;
import com.zoj.bp.marketing.dao.IOrderDao;
import com.zoj.bp.sysmgr.usermgr.dao.IUserDao;

@Service
public class InfoerService implements IInfoerService{

	@Autowired
	private IInfoerDao infoerDao;
	
	@Autowired
	private IOrderDao orderDao;
	
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
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination,
			User loginUser, String name, String tel, boolean containsUnderling, Integer... levels) throws BusinessException
	{
		if(!loginUser.isBelongMarketing())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查看信息员。"));
		User dbUser = userDao.getUserById(loginUser.getId());
		DatagridVo<Infoer> is;
		if(dbUser.isLeader() && dbUser.getGroupId() == null)		//主管，尚未分配组
			is = infoerDao.getInfoersBySalesman(dbUser, name, tel, levels, pagination);
		else
		{
			if(containsUnderling)
				is = infoerDao.getAllInfoer(pagination, loginUser, name, tel, levels);
			else
				is = infoerDao.getInfoersBySalesman(dbUser, name, tel, levels, pagination);
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
	public Infoer findByTel(Infoer infoer, User loginUser)
	{
		infoer = infoerDao.findByTel(infoer);
		Optional.ofNullable(infoer).ifPresent(i -> i.hideAllTel(loginUser));
		return infoer;
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
		return infoerDao.updateInfoerSalesmanId(infoerIds,salesmanId);
	}
}