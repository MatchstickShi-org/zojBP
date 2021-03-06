package com.zoj.bp.sysmgr.groupmgr.service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.Group;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author Administrator
 */
public interface IGroupService
{
	/**
	 * @param pagination
	 * @param type
	 * @return
	 */
	DatagridVo<Group> getAllGroups(Pagination pagination, Integer type);

	/**
	 * @param group
	 * @return
	 */
	Integer addGroup(Group group);

	/**
	 * @param group
	 * @throws BusinessException 
	 */
	Integer updateGroup(Group group) throws BusinessException;

	/**
	 * @param grpIds
	 */
	Integer deleteGroupByIds(Integer[] grpIds);

	/**
	 * @param groupId
	 * @return
	 */
	Group getGroupById(Integer groupId);

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
	 */
	Integer addUnderling2Group(Integer groupId, Integer[] underlingIds);

	/**
	 * @param groupId
	 * @param underlingIds
	 */
	Integer removeUnderlingFromGroup(Integer groupId, Integer[] underlingIds);

	/**
	 * @param groupId
	 * @param pagination TODO
	 * @return
	 */
	DatagridVo<User> getCanAssignLeadersByGroup(Integer groupId, Pagination pagination);

	/**
	 * @param groupId
	 * @param leaderId
	 * @return
	 * @throws BusinessException 
	 */
	Integer changeLeader4Group(Integer groupId, Integer leaderId) throws BusinessException;
}