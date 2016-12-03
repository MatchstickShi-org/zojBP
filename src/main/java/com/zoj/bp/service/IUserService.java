/**
 * 
 */
package com.zoj.bp.service;

import com.zoj.bp.excption.BusinessException;
import com.zoj.bp.model.User;
import com.zoj.bp.vo.DatagridVo;
import com.zoj.bp.vo.Pagination;

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
	 * @param changePwd TODO
	 * @throws BusinessException 
	 */
	void updateUser(User user, boolean changePwd) throws BusinessException;

	/**
	 * @param pagination
	 * @return
	 */
	DatagridVo<User> getAllUser(Pagination pagination);

	/**
	 * @param user
	 */
	void addUser(User user);

	/**
	 * @param userIds
	 * @return TODO
	 */
	Integer deleteUserByIds(Integer[] userIds);

	/**
	 * @param id
	 * @param newPwdForMD5
	 */
	void changPwd(Integer userId, String newPwdForMD5);

	/**
	 * @param userId
	 * @param brandIds
	 * @throws BusinessException 
	 */
	void addBrandsToUser(Integer userId, Integer[] brandIds) throws BusinessException;

	/**
	 * @param userId
	 * @param brandIds
	 * @throws BusinessException 
	 */
	void removeBrandsFromUser(Integer userId, Integer[] brandIds) throws BusinessException;

	/**
	 * @param brandId
	 * @param userIds
	 * @return
	 */
	Integer addOperatorsToBrand(Integer brandId, Integer[] userIds);

	/**
	 * @param brandId
	 * @param userIds
	 * @return
	 */
	Integer removeOperatorsFromBrand(Integer brandId, Integer[] userIds);
}