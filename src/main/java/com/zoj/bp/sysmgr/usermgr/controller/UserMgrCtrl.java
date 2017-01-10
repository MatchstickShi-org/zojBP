/**
 * 
 */
package com.zoj.bp.sysmgr.usermgr.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.EncryptUtil;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/sysMgr/userMgr")
public class UserMgrCtrl
{
	@Autowired
	private IUserService userSvc;
	
	@RequestMapping(value = "/toUserMgrView")
	public String toUserMgrView() throws BusinessException
	{
		return "sysMgr/userMgr/index";
	}
	
	@RequestMapping(value = "/getAllUsers")
	@ResponseBody
	public DatagridVo<User> getAllUsers(Pagination pagination,
			@RequestParam(required=false) String userName, @RequestParam(required=false) String alias)
	{
		return userSvc.getAllUser(pagination, userName, alias);
	}
	
	@RequestMapping(value = "/addUser")
	@ResponseBody
	public Map<String, ?> addUser(@Valid User user, @RequestParam("confirmPwd") String confirmPwd, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		if(!StringUtils.equals(user.getPwd(), confirmPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("两次密码输入不一致，请重新输入。")));
		user.setPwd(EncryptUtil.encoderByMd5(user.getPwd()));
		userSvc.addUser(user);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/deleteUsers")
	@ResponseBody
	public Map<String, ?> deleteUsers(@RequestParam("userIds[]") Integer[] userIds) throws Exception
	{
		if(ArrayUtils.isEmpty(userIds))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("没有可以删除的用户。")));
		userSvc.deleteUsers(userIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/editUser")
	@ResponseBody
	public Map<String, ?> editUser(@Valid User user, @RequestParam("confirmPwd") String confirmPwd, Errors errors) throws BusinessException, Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		if(!StringUtils.equals(user.getPwd(), confirmPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("两次密码输入不一致，请重新输入。")));
		//if(StringUtils.equals(user.getPwd(), "******"))		//不修改密码
		if(StringUtils.isEmpty(user.getPwd()))		//不修改密码
			userSvc.updateUser(user, false);
		else
		{
			user.setPwd(EncryptUtil.encoderByMd5(user.getPwd()));
			userSvc.updateUser(user, true);
		}
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/deleteUserByIds")
	@ResponseBody
	public Map<String, ?> deleteUserByIds(@RequestParam("delIds[]") Integer[] userIds, HttpSession session) throws Exception
	{
		if(ArrayUtils.isEmpty(userIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可删除的用户"));
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.contains(userIds, loginUser.getId()))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("不能删除你自己。"));
		userSvc.setUserToDimission(userIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/revertUserByIds")
	@ResponseBody
	public Map<String, ?> revertUserByIds(@RequestParam("revertIds[]") Integer[] userIds, HttpSession session) throws Exception
	{
		if(ArrayUtils.isEmpty(userIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可恢复的用户"));
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.contains(userIds, loginUser.getId()))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("不能恢复你自己。"));
		userSvc.revertUserByIds(userIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/getUserById")
	@ResponseBody
	public Map<String, Object> getUserById(@RequestParam("userId") Integer userId) throws Exception
	{
		User user = userSvc.getUserById(userId);
		user.setPwd(null);
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(user);
		map.put("confirmPwd", null);
		return map;
	}
	
	@RequestMapping(value = "/changePwd")
	@ResponseBody
	public Map<String, Object> changePwd(HttpSession session, @RequestParam("originalPwd") String originalPwd,
			@RequestParam("newPwd") String newPwd, @RequestParam("confirmNewPwd") String confirmNewPwd) throws Exception
	{
		if(StringUtils.isBlank(originalPwd) || StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmNewPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL, "验证失败，请检查输入内容。"));
		if(!StringUtils.equals(newPwd, confirmNewPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL, "两次密码输入不一致，请重新输入。"));
		User loginUser = (User)session.getAttribute("loginUser");
		User dbUser = userSvc.getUserById(loginUser.getId());
		if(!StringUtils.equals(EncryptUtil.encoderByMd5(originalPwd), dbUser.getPwd()))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL, "原密码错误，请重新输入。"));
		String newPwdForMD5 = EncryptUtil.encoderByMd5(newPwd);
		loginUser.setPwd(newPwdForMD5);
		userSvc.changPwd(loginUser.getId(), newPwdForMD5);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS.setMsg("密码修改成功。"));
	}
	
	@RequestMapping("/showAssignUnderlingWindow")
	public ModelAndView showAssignUnderlingWindow(@RequestParam Integer leaderId)
	{
		ModelAndView mv = new ModelAndView("sysMgr/userMgr/assignUnderling", "isLeader", true);
		User user = userSvc.getUserById(leaderId);
		if(!user.isMarketingLeader() && !user.isDesignLeader())	//非主管
			mv.addObject("isLeader", false);
		return mv;
	}
	
	@RequestMapping(value = "/getAssignedUnderlingByUser")
	@ResponseBody
	public DatagridVo<User> getAssignedUnderlingByUser(@RequestParam("userId") Integer userId, Pagination pagination) throws BusinessException
	{
		return userSvc.getAssignedUnderling(userId, pagination);
	}
	
	@RequestMapping(value = "/getNotAssignUnderlingByUser")
	@ResponseBody
	public DatagridVo<User> getNotAssignUnderlingByUser(@RequestParam("userId") Integer userId, Pagination pagination) throws BusinessException
	{
		return userSvc.getNotAssignUnderling(userId, pagination);
	}
	
	@RequestMapping(value = "/addUnderlingToUser")
	@ResponseBody
	public Map<String, Object> addUnderlingToUser(HttpSession session,
			@RequestParam("userId") Integer userId, @RequestParam("underlingIds[]") Integer[] underlingIds) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("您没有修改用户下属的权限。"));
		if(ArrayUtils.isEmpty(underlingIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可分配的下属。"));
		userSvc.addUnderlingToUser(userId, underlingIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/removeUnderlingFromUser")
	@ResponseBody
	public Map<String, Object> removeUnderlingFromUser(HttpSession session,
			@RequestParam("userId") Integer userId, @RequestParam("underlingIds[]") Integer[] underlingIds) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("您没有修改用户下属的权限。"));
		if(ArrayUtils.isEmpty(underlingIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可移除的下属。"));
		userSvc.removeUnderlingFromLeader(userId, underlingIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}