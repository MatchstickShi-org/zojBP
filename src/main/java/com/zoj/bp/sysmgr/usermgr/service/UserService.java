package com.zoj.bp.sysmgr.usermgr.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.sysmgr.usermgr.dao.IUserDao;

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

	/**
	 * @param pagination
	 * @return
	 */
	@Override
	public DatagridVo<User> getAllUser(Pagination pagination, String userName, String alias)
	{
		return userDao.getAllUser(pagination, userName, alias);
	}

	/**
	 * @param user
	 */
	@Override
	public void addUser(User user)
	{
		Integer leaderId = userDao.addUser(user);
		if (user.isMarketingLeader() || user.isDesignLeader())		//主管
		{
			Integer count = userDao.getCountByRole(user.getRole());
			userDao.addUserGroup(leaderId, (user.isMarketingLeader() ? "市场部" : "设计部") + "-组" + count);
			userDao.addUnderlingToUser(leaderId, leaderId);
		}
	}

	@Override
	public Integer deleteUserByIds(Integer[] userIds)
	{
		return userDao.deleteUserByIds(userIds);
	}
	
	@Override
	public Integer revertUserByIds(Integer[] userIds)
	{
		return userDao.revertUserByIds(userIds);
	}

	@Override
	public void changPwd(Integer userId, String newPwdForMD5)
	{
		userDao.changePwd(userId, newPwdForMD5);
	}

	@Override
	public Integer addUnderlingToUser(Integer userId, Integer[] underlingIds) throws BusinessException
	{
		User user = userDao.getUserById(userId);
		if(!user.isDesignLeader() && !user.isMarketingLeader())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("要为其分配下属的用户不是[主管]，无法分配。"));
		userDao.removeUnderling(underlingIds);
		return userDao.addUnderlingToUser(userId, underlingIds);
	}

	@Override
	public Integer removeUnderlingFromUser(Integer userId, Integer[] underlingIds) throws BusinessException
	{
		return userDao.removeUnderlingFromUser(userId, underlingIds);
	}

	@Override
	public DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination)
	{
		return userDao.getAssignedUnderling(userId, pagination);
	}

	@Override
	public DatagridVo<User> getNotAssignUnderling(Integer userId, Pagination pagination)
	{
		User leader = this.getUserById(userId);
		if(!leader.isMarketingLeader() && !leader.isDesignLeader())
			return DatagridVo.<User>emptyVo();
		return userDao.getNotAssignUnderling(leader, pagination);
	}
}