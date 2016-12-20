package com.zoj.bp.marketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IInfoerDao;

@Service
public class InfoerService implements IInfoerService{

	@Autowired
	private IInfoerDao infoerDao;
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
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination, User loginUser,String name,String tel,String[] level) {
		return infoerDao.getAllInfoer(pagination, loginUser,name,tel,level);
	}

	@Override
	public Integer addInfoer(Infoer infoer) {
		return infoerDao.addInfoer(infoer);
	}

	@Override
	public Infoer findByTel(String tel) {
		return infoerDao.findByTel(tel);
	}

}
