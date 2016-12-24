/**
 * 
 */
package com.zoj.bp.sysmgr.groupmgr.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Group;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.sysmgr.groupmgr.service.IGroupService;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/sysMgr/userGrpMgr")
public class GroupMgrCtrl
{
	@Autowired
	private IGroupService grpSvc;
	
	@RequestMapping(value = "/toIndexView")
	public String toIndexView() throws BusinessException
	{
		return "sysMgr/userGrpMgr/index";
	}
	
	@RequestMapping(value = "/getAllGroups")
	@ResponseBody
	public DatagridVo<Group> getAllGroups(Pagination pagination, @RequestParam(required=false) Integer type)
	{
		return grpSvc.getAllGroups(pagination, type);
	}
	
	@RequestMapping(value = "/addGroup")
	@ResponseBody
	public Map<String, ?> addGroup(@Valid Group group, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		grpSvc.addGroup(group);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/editGroup")
	@ResponseBody
	public Map<String, ?> editGroup(@Valid Group group, Errors errors) throws BusinessException, Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		grpSvc.updateGroup(group);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/deleteGroupByIds")
	@ResponseBody
	public Map<String, ?> deleteGroupByIds(@RequestParam("delIds[]") Integer[] grpIds) throws Exception
	{
		grpSvc.deleteGroupByIds(grpIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/getGroupById")
	@ResponseBody
	public Map<String, Object> getGroupById(@RequestParam("groupId") Integer groupId) throws Exception
	{
		return ResponseUtils.buildRespMapByBean(grpSvc.getGroupById(groupId));
	}
	
	@RequestMapping(value = "/getCanAssignLeadersByGroup")
	@ResponseBody
	public Map<String, Object> getCanAssignLeadersByGroup(
			@RequestParam(value="groupId", required=false) Integer groupId, Pagination pagination) throws Exception
	{
		return ResponseUtils.buildRespMapByBean(grpSvc.getCanAssignLeadersByGroup(groupId, pagination));
	}
	
	@RequestMapping(value = "/changeLeader4Group")
	@ResponseBody
	public Map<String, Object> changeLeader4Group(
			@RequestParam("groupId") Integer groupId, @RequestParam("leaderId") Integer leaderId) throws Exception
	{
		return ResponseUtils.buildRespMapByBean(grpSvc.changeLeader4Group(groupId, leaderId));
	}
	
	@RequestMapping("/showAssignUnderlingWindow")
	public String showAssignUnderlingWindow(@RequestParam Integer groupId)
	{
		return "sysMgr/userGrpMgr/assignUnderling";
	}
	
	@RequestMapping("/showSelectLeaderWindow")
	public String showSelectLeaderWindow(@RequestParam Integer groupId)
	{
		return "sysMgr/userGrpMgr/selectLeader";
	}
	
	@RequestMapping(value = "/getAssignedUnderlingByGroup")
	@ResponseBody
	public DatagridVo<User> getAssignedUnderlingByGroup(@RequestParam("groupId") Integer groupId, Pagination pagination) throws BusinessException
	{
		return grpSvc.getAssignedUnderling(groupId, pagination);
	}
	
	@RequestMapping(value = "/getNotAssignUnderlingByGroup")
	@ResponseBody
	public DatagridVo<User> getNotAssignUnderlingByGroup(@RequestParam("groupId") Integer groupId, Pagination pagination) throws BusinessException
	{
		return grpSvc.getNotAssignUnderling(groupId, pagination);
	}
	
	@RequestMapping(value = "/addUnderlingToGroup")
	@ResponseBody
	public Map<String, Object> addUnderlingToGroup(HttpSession session,
			@RequestParam("groupId") Integer groupId, @RequestParam("underlingIds[]") Integer[] underlingIds) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("您没有修改用户组组员的权限。"));
		if(ArrayUtils.isEmpty(underlingIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可分配的组员。"));
		grpSvc.addUnderlingToGroup(groupId, underlingIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/removeUnderlingFromGroup")
	@ResponseBody
	public Map<String, Object> removeUnderlingFromGroup(HttpSession session,
			@RequestParam("groupId") Integer groupId, @RequestParam("underlingIds[]") Integer[] underlingIds) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("您没有修改用户组员的权限。"));
		if(ArrayUtils.isEmpty(underlingIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可移除的组员。"));
		grpSvc.removeUnderlingFromGroup(groupId, underlingIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}