/**
 * 
 */
package com.zoj.bp.controller;

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

import com.zoj.bp.excption.BusinessException;
import com.zoj.bp.excption.ReturnCode;
import com.zoj.bp.model.User;
import com.zoj.bp.service.IUserService;
import com.zoj.bp.util.EncryptUtil;
import com.zoj.bp.util.ResponseUtils;
import com.zoj.bp.vo.DatagridVo;
import com.zoj.bp.vo.Pagination;

/**
 * @author MatchstickShi
 *
 */
@Controller
public class UserMgrCtrl
{
	@Autowired
	private IUserService userSvc;
	
	@RequestMapping(value = "/toUserMgrView")
	public ModelAndView toUserMgrView() throws BusinessException
	{
		return new ModelAndView("userMgr/index");
	}
	
	@RequestMapping(value = "/userMgr/getAllUsers")
	@ResponseBody
	public DatagridVo<User> getAllUsers(Pagination pagination)
	{
		return userSvc.getAllUser(pagination);
	}
	
	@RequestMapping(value = "/userMgr/addUser")
	@ResponseBody
	public Map<String, ?> addUser(@Valid User user, @RequestParam("confirmPwd") String confirmPwd, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		if(!StringUtils.equals(user.getPwd(), confirmPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("两次密码输入不一致，请重新输入。")));
		user.setPwd(EncryptUtil.encoderByMd5(user.getPwd()));
		userSvc.addUser(user);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/userMgr/editUser")
	@ResponseBody
	public Map<String, ?> editUser(@Valid User user, @RequestParam("confirmPwd") String confirmPwd, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		if(!StringUtils.equals(user.getPwd(), confirmPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("两次密码输入不一致，请重新输入。")));
		if(StringUtils.equals(user.getPwd(), "******"))		//不修改密码
			userSvc.updateUser(user, false);
		else
		{
			user.setPwd(EncryptUtil.encoderByMd5(user.getPwd()));
			userSvc.updateUser(user, true);
		}
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/userMgr/deleteUserByIds")
	@ResponseBody
	public Map<String, ?> deleteUserByIds(@RequestParam("delIds[]") Integer[] userIds, HttpSession session) throws Exception
	{
		if(ArrayUtils.isEmpty(userIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可删除的用户"));
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.contains(userIds, loginUser.getId()))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("不能删除你自己。"));
		userSvc.deleteUserByIds(userIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/userMgr/getUserById")
	@ResponseBody
	public Map<String, Object> getUserById(@RequestParam("userId") Integer userId) throws Exception
	{
		User user = userSvc.getUserById(userId);
		user.setPwd("******");
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(user);
		map.put("confirmPwd", user.getPwd());
		return map;
	}
	
	@RequestMapping(value = "/userMgr/changePwd")
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
}