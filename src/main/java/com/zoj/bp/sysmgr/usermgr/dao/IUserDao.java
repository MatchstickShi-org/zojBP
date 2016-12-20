/**
 * 
 */
package com.zoj.bp.sysmgr.usermgr.dao;

import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author Administrator
 */
public interface IUserDao
{
	User getUserByName(String userName);

	/**
	 * @param id
	 * @return
	 */
	User getUserById(Integer id);

	/**
	 * @param user
	 * @param changePwd 
	 * @return
	 */
	void updateUser(User user, boolean changePwd);

	/**
	 * @param pagination
	 * @param alias 
	 * @param userName 
	 * @return
	 */
	DatagridVo<User> getAllUser(Pagination pagination, String userName, String alias);

	/**
	 * @param user
	 * @return 
	 */
	Integer addUser(User user);

	/**
	 * @param userIds
	 * @return
	 */
	Integer deleteUserByIds(Integer[] userIds);

	/**
	 * @param userId
	 * @param newPwdForMD5
	 */
	void changePwd(Integer userId, String newPwdForMD5);

	/**
	 * @param userIds
	 * @return
	 */
	Integer revertUserByIds(Integer[] userIds);

	/**
	 * @param userId
	 * @param pagination 
	 * @return
	 */
	DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination);

	/**
	 * @param leader
	 * @param pagination 
	 * @return
	 */
	DatagridVo<User> getNotAssignUnderling(User leader, Pagination pagination);

	/**
	 * @param userId
	 * @param underlingIds
	 */
	Integer removeUnderlingFromLeader(Integer userId, Integer[] underlingIds);

	/**
	 * @param userId
	 * @param underlingIds
	 * @return
	 */
	Integer addUnderlingToLeader(Integer userId, Integer... underlingIds);

	/**
	 * @param underlingIds
	 * @return
	 */
	Integer removeUnderling(Integer[] underlingIds);

	/**
	 * 移除指定用户的主管身份（从USER_GROUP表）
	 * @param leaderId
	 * @param newGroupId 
	 */
	void setLeaderToEmployee(Integer leaderId, Integer newGroupId);

	/**
	 * 设置指定用户为指定组组长
	 * @param userId
	 * @param leadGroupId
	 */
	void setUserToLeader(Integer userId, Integer leadGroupId);
}