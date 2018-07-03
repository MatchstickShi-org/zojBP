package com.decoration.bp.sysmgr.groupmgr.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.excption.ReturnCode;
import com.decoration.bp.common.model.Group;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.model.Group.Type;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.sysmgr.groupmgr.dao.IGroupDao;
import com.decoration.bp.sysmgr.usermgr.dao.IUserDao;

/**
 * @author MatchstickShi
 */
@Service
public class GroupService implements IGroupService
{
	@Autowired
	private IGroupDao grpDao;
	
	@Autowired
	private IUserDao userDao;

	@Override
	public DatagridVo<Group> getAllGroups(Pagination pagination, Integer type)
	{
		return grpDao.getAllGroups(pagination, type);
	}

	@Override
	public Integer addGroup(Group group)
	{
		Integer grpId = grpDao.addGroup(group.getName(), Type.valueOf(group.getType()) == Type.marketingGroup);
		if(group.getLeaderId() != null)
			grpDao.addUnderling2Group(grpId, group.getLeaderId());
		return grpId;
	}

	@Override
	public Integer updateGroup(Group group) throws BusinessException
	{
		if(group.getId() == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户组。");
		Group dbGrp = grpDao.getGroupByName(group.getName());
		if(dbGrp != null && dbGrp.getId() != group.getId())
			throw new BusinessException(
					ReturnCode.VALIDATE_FAIL, MessageFormat.format("已存在名称为{0}的用户组，请修改组名后再试。", group.getName()));
		dbGrp = grpDao.getGroupById(group.getId());
		if(dbGrp == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户组。");
		if(dbGrp.getLeaderId() != null && dbGrp.getLeaderId() != group.getLeaderId())	//主管有修改
			grpDao.removeUnderlingFromGroup(group.getId(), dbGrp.getLeaderId());	//将原主管从当前group移除
		if(group.getLeaderId() != null)
			grpDao.addUnderling2Group(group.getId(), group.getLeaderId());
		return grpDao.updateGroup(group);
	}

	@Override
	public Integer deleteGroupByIds(Integer[] grpIds)
	{
		grpDao.removeUnderlingFromGroups(grpIds);
		return grpDao.deleteGroupByIds(grpIds);
	}

	@Override
	public Group getGroupById(Integer groupId)
	{
		return grpDao.getGroupById(groupId);
	}

	@Override
	public DatagridVo<User> getAssignedUnderling(Integer groupId, Pagination pagination)
	{
		return grpDao.getAssignedUnderling(groupId, pagination);
	}

	@Override
	public DatagridVo<User> getNotAssignUnderling(Integer groupId, Pagination pagination)
	{
		return grpDao.getNotAssignUnderling(groupId, pagination);
	}

	@Override
	public Integer addUnderling2Group(Integer groupId, Integer[] underlingIds)
	{
		return grpDao.addUnderling2Group(groupId, underlingIds);
	}

	@Override
	public Integer changeLeader4Group(Integer groupId, Integer leaderId) throws BusinessException
	{
		User leader = userDao.getUserById(leaderId);
		if(leader == null)
			throw new BusinessException(ReturnCode.NOT_FIND_RECORD);
		if(!leader.isLeader())
			throw new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("选择用户不是主管，无法变更。"));
		grpDao.removeLeaderFromGroup(groupId);
		return grpDao.addUnderling2Group(groupId, leaderId);
	}

	@Override
	public Integer removeUnderlingFromGroup(Integer groupId, Integer[] underlingIds)
	{
		return grpDao.removeUnderlingFromGroup(groupId, underlingIds);
	}

	@Override
	public DatagridVo<User> getCanAssignLeadersByGroup(Integer groupId, Pagination pagination)
	{
		return grpDao.getCanAssignLeadersByGroup(groupId, pagination);
	}
}