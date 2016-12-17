/**
 * 
 */
package com.zoj.bp.sysmgr.usermgr.dao;

import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author Administrator
 *
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
	 * @param changePwd TODO
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
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination);

	/**
	 * @param leader
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getNotAssignUnderling(User leader, Pagination pagination);

	/**
	 * @param userId
	 * @param underlingIds
	 */
	Integer removeUnderlingFromUser(Integer userId, Integer[] underlingIds);

	/**
	 * @param userId
	 * @param underlingIds
	 * @return
	 */
	Integer addUnderlingToUser(Integer userId, Integer[] underlingIds);

	/**
	 * @param role
	 * @return
	 */
	Integer getCountByRole(Integer role);

	/**
	 * @param leaderId
	 * @param name
	 * @return
	 */
	Integer addUserGroup(Integer leaderId, String name);
}