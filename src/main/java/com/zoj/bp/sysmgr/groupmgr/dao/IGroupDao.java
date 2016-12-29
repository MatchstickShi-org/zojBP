/**
 * 
 */
package com.zoj.bp.sysmgr.groupmgr.dao;

import com.zoj.bp.common.model.Group;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author Administrator
 *
 */
public interface IGroupDao
{
	/**
	 * @param pagination
	 * @param type
	 * @return
	 */
	DatagridVo<Group> getAllGroups(Pagination pagination, Integer type);

	/**
	 * @param name
	 * @param isMarketingGroup
	 * @return
	 */
	Integer addGroup(String name, boolean isMarketingGroup);

	/**
	 * @param groupId
	 * @return
	 */
	Group getGroupById(Integer groupId);

	/**
	 * @param name
	 * @return
	 */
	Group getGroupByName(String name);

	/**
	 * @param group
	 */
	Integer updateGroup(Group group);

	/**
	 * @param grpIds
	 * @return
	 */
	Integer deleteGroupByIds(Integer[] grpIds);

	/**
	 * @param groupId
	 * @param pagination
	 * @return
	 */
	DatagridVo<User> getAssignedUnderling(Integer groupId, Pagination pagination);

	/**
	 * @param groupId
	 * @param pagination
	 * @return
	 */
	DatagridVo<User> getNotAssignUnderling(Integer groupId, Pagination pagination);

	/**
	 * @param groupId
	 * @param underlingIds
	 * @return
	 */
	Integer addUnderling2Group(Integer groupId, Integer... underlingIds);

	/**
	 * @param groupId
	 * @param underlingIds
	 * @return
	 */
	Integer removeUnderlingFromGroup(Integer groupId, Integer... underlingIds);

	/**
	 * @param groupId
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getCanAssignLeadersByGroup(Integer groupId, Pagination pagination);

	/**
	 * @param grpIds
	 */
	Integer removeUnderlingFromGroups(Integer... grpIds);

	/**
	 * @param groupId
	 */
	Integer removeLeaderFromGroup(Integer groupId);
}