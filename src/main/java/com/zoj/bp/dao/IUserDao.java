/**
 * 
 */
package com.zoj.bp.dao;

import com.zoj.bp.model.User;
import com.zoj.bp.vo.DatagridVo;
import com.zoj.bp.vo.Pagination;

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
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getAssignedOperatorDatagridVoByBrandId(Integer brandId, Pagination pagination);

	/**
	 * @param brandId
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getNotAssignedOperatorDatagridVoByBrandId(Integer brandId, Pagination pagination);

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