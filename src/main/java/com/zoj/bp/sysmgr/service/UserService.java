package com.zoj.bp.sysmgr.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.sysmgr.dao.IUserDao;

/**
 * @author MatchstickShi
 */
@Service
public class UserService implements IUserService
{
	@Autowired
	private IUserDao userDao;

	@Override
	public User getUserByName(String userName)
	{
		return userDao.getUserByName(userName);
	}

	@Override
	public User getUserById(Integer id)
	{
		return userDao.getUserById(id);
	}

	@Override
	public void updateUser(User user, boolean changePwd) throws BusinessException
	{
		if(user.getId() == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户。");
		User dbUser = userDao.getUserById(user.getId());
		if(dbUser == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户。");
		dbUser = userDao.getUserByName(user.getName());
		if(dbUser != null && dbUser.getId() != user.getId())
			throw new BusinessException(
					ReturnCode.VALIDATE_FAIL, MessageFormat.format("已存在用户名为{0}，请修改用户名后再试。", user.getName()));
		userDao.updateUser(user, changePwd);
	}

	@Override
	/**
	 * @param pagination
	 * @return
	 */
	public DatagridVo<User> getAllUser(Pagination pagination)
	{
		return userDao.getAllUser(pagination);
	}

	@Override
	/**
	 * @param user
	 */
	public void addUser(User user)
	{
		userDao.addUser(user);
	}

	@Override
	public Integer deleteUserByIds(Integer[] userIds)
	{
		return userDao.deleteUserByIds(userIds);
	}

	@Override
	public void changPwd(Integer userId, String newPwdForMD5)
	{
		userDao.changePwd(userId, newPwdForMD5);
	}

	@Override
	public void addBrandsToUser(Integer userId, Integer[] brandIds) throws BusinessException
	{
		User user = userDao.getUserById(userId);
		if(user.isAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("无法修改[管理员]的产品权限。"));
		userDao.removeBrandsFromUser(userId, brandIds);
		userDao.addBrandsToUser(userId, brandIds);
	}

	@Override
	public void removeBrandsFromUser(Integer userId, Integer[] brandIds) throws BusinessException
	{
		User user = userDao.getUserById(userId);
		if(user.isAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("无法修改[管理员]的产品权限。"));
		userDao.removeBrandsFromUser(userId, brandIds);
	}

	@Override
	public Integer addOperatorsToBrand(Integer brandId, Integer[] userIds)
	{
		userDao.removeOperatorsFromBrand(brandId, userIds);
		return userDao.addOperatorsToBrand(brandId, userIds);
	}

	@Override
	public Integer removeOperatorsFromBrand(Integer brandId, Integer[] userIds)
	{
		return userDao.removeOperatorsFromBrand(brandId, userIds);
	}
}