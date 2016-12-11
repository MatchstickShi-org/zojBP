package com.zoj.bp.common.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.msg.BroadcastMsgManager;
import com.zoj.bp.common.util.EncryptUtil;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.sysmgr.usermgr.service.IMenuService;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * @author MatchstickShi
 */
@Controller
public class LoginCtrl
{
	@Autowired
	private IUserService userSvc;
	
	@Autowired
	private IMenuService menuSvc;
	
	@RequestMapping(value = {"/", "/toLoginView"})
	public ModelAndView index() throws BusinessException, Exception
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/login", produces="application/json")
	@ResponseBody
	public Map<String, ?> login(HttpSession session, User loginUser) throws BusinessException, Exception
	{
		if(loginUser == null || StringUtils.isEmpty(loginUser.getName()) || StringUtils.isEmpty(loginUser.getPwd()))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("无效的用户名或密码。"));
		
		User u = userSvc.getUserByName(loginUser.getName());
		if(u == null)
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("无效的用户名或密码。"));
		if(!StringUtils.equals(EncryptUtil.encoderByMd5(loginUser.getPwd()), u.getPwd()))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("无效的用户名或密码。"));
		
		u.setMenus(menuSvc.getMenusByUser(u));
		
		session.setAttribute("broadcastMsgs", BroadcastMsgManager.instance().getAllMsgs());
		session.setAttribute("loginUser", u);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session)
	{
		if(session.getAttribute("loginUser") != null)
			session.removeAttribute("loginUser");
		return "redirect:/toLoginView";
	}
}