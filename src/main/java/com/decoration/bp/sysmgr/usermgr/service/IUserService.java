/**
 * 
 */
package com.decoration.bp.sysmgr.usermgr.service;

import java.util.List;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

/**
 * @author Administrator
 *
 */
public interface IUserService
{
	User getUserByName(String userName);

	/**
	 * @param id
	 */
	User getUserById(Integer id);

	/**
	 * @param user
	 * @param changePwd 
	 * @throws BusinessException 
	 */
	void updateUser(User user, boolean changePwd) throws BusinessException;

	/**
	 * @param pagination
	 * @param alias 
	 * @param userName 
	 * @return
	 */
	DatagridVo<User> getAllUser(Pagination pagination, String userName, String alias);
	
	/**
	 * @param pagination
	 * @param userName
	 * @param alias
	 * @param roles
	 * @return
	 */
	DatagridVo<User> getAllUserByRole(Pagination pagination, String userName, String alias,Integer... roles);

	/**
	 * @param user
	 * @throws BusinessException 
	 */
	void addUser(User user) throws BusinessException;

	/**
	 * @param userIds
	 * @return 
	 */
	Integer setUserToDimission(Integer[] userIds);

	/**
	 * @param id
	 * @param newPwdForMD5
	 */
	void changPwd(Integer userId, String newPwdForMD5);

	/**
	 * @param userIds
	 */
	Integer revertUserByIds(Integer[] userIds);

	/**
	 * @param userId
	 * @param pagination 
	 * @return
	 */
	DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination);

	/**
	 * @param userId
	 * @param pagination 
	 * @return
	 */
	DatagridVo<User> getNotAssignUnderling(Integer userId, Pagination pagination);

	/**
	 * @param userId
	 * @param underlingIds
	 * @throws BusinessException
	 */
	Integer addUnderlingToUser(Integer userId, Integer[] underlingIds) throws BusinessException;

	/**
	 * @param userId
	 * @param underlingIds
	 * @throws BusinessException
	 */
	Integer removeUnderlingFromLeader(Integer userId, Integer[] underlingIds) throws BusinessException;

	/**
	 * @param userIds
	 */
	void deleteUsers(Integer[] userIds);

	/**
	 * 获取所有在职的业务员
	 * @return
	 */
	List<User> getInServiceMarketingUsers();
	
	/**
	 * 获取所有在职的设计师
	 * @return
	 */
	List<User> getInServiceDesignUsers();

	/**
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	List<User> getMarketUnderlingByUser(User user) throws BusinessException;

	/**
	 * @param user
	 * @return
	 * @throws BusinessException
	 */
	List<User> getDesignUnderlingByUser(User user) throws BusinessException;
}