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

@Service
public class InfoerService implements IInfoerService{

	@Autowired
	private IInfoerDao infoerDao;
	
	@Autowired
	private IOrderDao orderDao;
	
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
			User loginUser, String name, String tel, Integer... levels) throws BusinessException
	{
		if(!loginUser.isBelongMarketing())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查看信息员。"));
		DatagridVo<Infoer> is = infoerDao.getAllInfoer(pagination, loginUser, name, tel, levels);
		if(loginUser.isLeader())
			is.getRows().stream().forEach(i -> i.hideAllTel(loginUser));
		return is;
	}

	@Override
	public Integer addInfoer(Infoer infoer) {
		return infoerDao.addInfoer(infoer);
	}

	@Override
	public Infoer findByTel(String tel, User loginUser)
	{
		Infoer infoer = infoerDao.findByTel(tel);
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