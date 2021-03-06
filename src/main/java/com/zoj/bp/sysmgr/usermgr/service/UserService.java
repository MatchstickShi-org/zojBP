package com.zoj.bp.sysmgr.usermgr.service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.dao.IOrderApproveDao;
import com.zoj.bp.common.dao.IOrderChangeLogDao;
import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.dao.IClientDao;
import com.zoj.bp.marketing.dao.IInfoerDao;
import com.zoj.bp.marketing.dao.IInfoerVisitDao;
import com.zoj.bp.marketing.dao.IOrderDao;
import com.zoj.bp.marketing.dao.IOrderVisitDao;
import com.zoj.bp.sysmgr.groupmgr.dao.IGroupDao;
import com.zoj.bp.sysmgr.usermgr.dao.IUserDao;

/**
 * @author MatchstickShi
 */
@Service
public class UserService implements IUserService
{
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IGroupDao grpDao;
	
	@Autowired
	private IInfoerDao infoerDao;
	
	@Autowired
	private IClientDao clientDao;
	
	@Autowired
	private IOrderDao orderDao;
	
	@Autowired
	private IOrderApproveDao approveDao;
	
	@Autowired
	private IInfoerVisitDao infoerVisitDao;
	
	@Autowired
	private IOrderVisitDao orderVisitDao;
	
	@Autowired
	private IOrderChangeLogDao orderChangeLogDao;

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
		if(dbUser.getRole() != user.getRole())		//角色有修改
		{
			if(!dbUser.isLeader() && user.isLeader())	//非主管 -> 主管：从当前组移除（如有有的话）
				grpDao.removeUnderlingFromGroup(user.getGroupId(), user.getId());
		}
		dbUser = userDao.getUserByName(user.getName());
		if(dbUser != null && dbUser.getId() != user.getId())
			throw new BusinessException(
					ReturnCode.VALIDATE_FAIL, MessageFormat.format("已存在名称为{0}的用户，请修改用户名后再试。", user.getName()));
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
	 * 
	 * @param pagination
	 * @param userName
	 * @param alias
	 * @param roles
	 * @return
	 */
	@Override
	public DatagridVo<User> getAllUserByRole(Pagination pagination, String userName, String alias,Integer... roles)
	{
		return userDao.getAllUserByRole(pagination, userName, alias, roles);
	}

	/**
	 * @param user
	 * @throws BusinessException 
	 */
	@Override
	public void addUser(User user) throws BusinessException
	{
		User dbUser = userDao.getUserByName(user.getName());
		if(dbUser != null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.
					setMsg(MessageFormat.format("已存在名称为{0}的用户，请重新输入用户名。", dbUser.getName())));
		userDao.addUser(user);
	}

	@Override
	public Integer setUserToDimission(Integer[] userIds)
	{
		return userDao.setUserToDimission(userIds);
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
		return userDao.addUnderlingToLeader(userId, underlingIds);
	}

	@Override
	public Integer removeUnderlingFromLeader(Integer userId, Integer[] underlingIds) throws BusinessException
	{
		return userDao.removeUnderlingFromLeader(userId, underlingIds);
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
		if(!leader.isLeader() && !leader.isSuperAdmin())
			return DatagridVo.<User>emptyVo(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是主管，不能查询下属。"));
		return userDao.getNotAssignUnderling(leader, pagination);
	}

	@Override
	public void deleteUsers(Integer[] userIds)
	{
		infoerDao.deleteBySalesmans(userIds);
		infoerVisitDao.deleteBySalesmans(userIds);
		clientDao.deleteBySalesmans(userIds);
		clientDao.deleteByDesigners(userIds);
		approveDao.deleteBySalesmans(userIds);
		approveDao.deleteByDesigners(userIds);
		orderChangeLogDao.deleteBySalesmans(userIds);
		orderChangeLogDao.deleteByDesigners(userIds);
		orderVisitDao.deleteBySalesmanId(userIds);
		orderVisitDao.deleteByDesignerId(userIds);
		orderDao.deleteBySalesmans(userIds);
		orderDao.deleteByDesigners(userIds);
		userDao.deleteByUsers(userIds);
	}
	
	@Override
	public List<User> getInServiceMarketingUsers()
	{
		return userDao.getMarketingUsersByStatus(1);
	}
	
	@Override
	public List<User> getInServiceDesignUsers() {
		return userDao.getDesignUsersByStatus(1);
	}
	
	@Override
	public List<User> getMarketUnderlingByUser(User user) throws BusinessException
	{
		if(user.isBelongDesign() && !user.isSuperAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不能查询商务部人员。"));
		if(user.isMarketingSalesman())
			return Collections.<User>emptyList();
		if(user.isMarketingLeader())
			return userDao.getUnderlingByLeader(user.getId());
		if(user.isMarketingManager() || user.isSuperAdmin())
			return userDao.getMarketingUsersByStatus(1);
		return Collections.<User>emptyList();
	}
	
	@Override
	public List<User> getDesignUnderlingByUser(User user) throws BusinessException
	{
		if(user.isBelongMarketing() && !user.isSuperAdmin())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不能查询主案部人员。"));
		if(user.isDesignDesigner())
			return Collections.<User>emptyList();
		if(user.isDesignLeader())
			return userDao.getUnderlingByLeader(user.getId());
		if(user.isDesignManager() || user.isSuperAdmin())
			return userDao.getDesignUsersByStatus(1);
		return Collections.<User>emptyList();
	}
}