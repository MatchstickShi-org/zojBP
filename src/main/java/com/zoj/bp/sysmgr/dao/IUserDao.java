/**
 * 
 */
package com.zoj.bp.sysmgr.dao;

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
	 * @return
	 */
	DatagridVo<User> getAllUser(Pagination pagination);

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
	 * @param userId
	 * @param brandIds
	 */
	Integer removeBrandsFromUser(Integer userId, Integer[] brandIds);

	/**
	 * @param userId
	 * @param brandIds
	 */
	Integer addBrandsToUser(Integer userId, Integer[] brandIds);

	/**
	 * @param brandId
	 * @param userIds
	 * @return 
	 */
	Integer removeOperatorsFromBrand(Integer brandId, Integer[] userIds);

	/**
	 * @param brandId
	 * @param userIds
	 * @return
	 */
	Integer addOperatorsToBrand(Integer brandId, Integer[] userIds);
}