/**
 * 
 */
package com.zoj.bp.sysmgr.groupmgr.dao;

import com.zoj.bp.common.model.Group;
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
	 * @param leaderId
	 * @return
	 */
	Integer addGroup(String name, boolean isMarketingGroup, Integer leaderId);

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
	DatagridVo<Group> getAssignedUnderling(Integer groupId, Pagination pagination);

	/**
	 * @param groupId
	 * @param pagination
	 * @return
	 */
	DatagridVo<Group> getNotAssignUnderling(Integer groupId, Pagination pagination);

	/**
	 * @param groupId
	 * @param underlingIds
	 * @return
	 */
	Integer addUnderlingToGroup(Integer groupId, Integer[] underlingIds);

	/**
	 * @param groupId
	 * @param underlingIds
	 * @return
	 */
	Integer removeUnderlingFromGroup(Integer groupId, Integer[] underlingIds);
}