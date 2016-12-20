/**
 * 
 */
package com.zoj.bp.mycenter.myinfo.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/myCenter/myInfo")
public class MyInfoCtrl
{
	@Autowired
	private IUserService userSvc;
	
	@RequestMapping(value = "/toMyInfoView")
	public ModelAndView toMyInfoView(HttpSession session) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return new ModelAndView("myCenter/myInfo/index", "userInfo", userSvc.getUserById(loginUser.getId()));
	}
	
	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(HttpSession session) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		User user = userSvc.getUserById(loginUser.getId());
		user.setPwd(null);
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(user);
		map.put("confirmPwd", null);
		return map;
	}
	
	@RequestMapping(value = "/updateUser")
	@ResponseBody
	public Map<String, ?> updateUser(@Valid User user, @RequestParam("confirmPwd") String confirmPwd, Errors errors) throws BusinessException, Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		if(!StringUtils.equals(user.getPwd(), confirmPwd))
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("两次密码输入不一致，请重新输入。")));
		if(StringUtils.isEmpty(user.getPwd()))		//不修改密码
			userSvc.updateUser(user, false);
		else
		{
			user.setPwd(EncryptUtil.encoderByMd5(user.getPwd()));
			userSvc.updateUser(user, true);
		}
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}