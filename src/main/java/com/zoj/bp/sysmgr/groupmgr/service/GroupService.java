package com.zoj.bp.sysmgr.groupmgr.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Group;
import com.zoj.bp.common.model.Group.Type;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.sysmgr.groupmgr.dao.IGroupDao;

/**
 * @author MatchstickShi
 */
@Service
public class GroupService implements IGroupService
{
	@Autowired
	private IGroupDao grpDao;

	@Override
	public DatagridVo<Group> getAllGroups(Pagination pagination, Integer type)
	{
		return grpDao.getAllGroups(pagination, type);
	}

	@Override
	public Integer addGroup(Group group)
	{
		return grpDao.addGroup(group.getName(), Type.valueOf(group.getType()) == Type.marketingGroup, group.getLeaderId());
	}

	@Override
	public Integer updateGroup(Group group) throws BusinessException
	{
		if(group.getId() == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户组。");
		Group dbGrp = grpDao.getGroupById(group.getId());
		if(dbGrp == null)
			throw new BusinessException(ReturnCode.VALIDATE_FAIL, "找不到要修改的用户。");
		dbGrp = grpDao.getGroupByName(group.getName());
		if(dbGrp != null && dbGrp.getId() != group.getId())
			throw new BusinessException(
					ReturnCode.VALIDATE_FAIL, MessageFormat.format("已存在名称为{0}的用户组，请修改组名后再试。", group.getName()));
		return grpDao.updateGroup(group);
	}

	@Override
	public Integer deleteGroupByIds(Integer[] grpIds)
	{
		return grpDao.deleteGroupByIds(grpIds);
	}

	@Override
	public Group getGroupById(Integer groupId)
	{
		return grpDao.getGroupById(groupId);
	}

	@Override
	public DatagridVo<Group> getAssignedUnderling(Integer groupId, Pagination pagination)
	{
		return grpDao.getAssignedUnderling(groupId, pagination);
	}

	@Override
	public DatagridVo<Group> getNotAssignUnderling(Integer groupId, Pagination pagination)
	{
		return grpDao.getNotAssignUnderling(groupId, pagination);
	}

	@Override
	public Integer addUnderlingToGroup(Integer groupId, Integer[] underlingIds)
	{
		return grpDao.addUnderlingToGroup(groupId, underlingIds);
	}

	@Override
	public Integer removeUnderlingFromGroup(Integer groupId, Integer[] underlingIds)
	{
		return grpDao.removeUnderlingFromGroup(groupId, underlingIds);
	}
}