package com.zoj.bp.marketing.service;

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
	public Infoer getInfoerByName(String InfoerName) {
		return infoerDao.getInfoerByName(InfoerName);
	}

	@Override
	public Infoer getInfoerById(Integer id) {
		return infoerDao.getInfoerById(id);
	}

	@Override
	public void updateInfoer(Infoer infoer) {
		infoerDao.updateInfoer(infoer);
	}

	@Override
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination,
			User loginUser, String name, String tel, Integer... levels) throws BusinessException
	{
		if(!loginUser.isBelongMarketing())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部人员，无法查看信息员。"));
		return infoerDao.getAllInfoer(pagination, loginUser, name, tel, levels);
	}

	@Override
	public Integer addInfoer(Infoer infoer) {
		return infoerDao.addInfoer(infoer);
	}

	@Override
	public Infoer findByTel(String tel) {
		return infoerDao.findByTel(tel);
	}

	@Override
	public DatagridVo<Infoer> findBySalesmanId(Integer salesmanId, Pagination pagination) {
		return infoerDao.findBySalesmanId(salesmanId,pagination);
	}

	@Override
	public Integer updateInfoerSalesmanId(Integer[] infoerIds, Integer salesmanId) {
		orderDao.updateOrderSalesmanIdByInfoers(infoerIds, salesmanId);
		return infoerDao.updateInfoerSalesmanId(infoerIds,salesmanId);
	}

}
