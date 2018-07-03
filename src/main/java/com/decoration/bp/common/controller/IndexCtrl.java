package com.decoration.bp.common.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.msg.MsgManager;

/**
 * @author MatchstickShi
 */
@Controller
public class IndexCtrl
{
	@RequestMapping(value = "/toIndexView")
	public ModelAndView toIndexView(HttpSession session) throws BusinessException, Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return new ModelAndView("index").addObject("loginUserMenus", JSON.toJSONString(loginUser.getMenus()));
	}
	
	@RequestMapping("/getLastMsg")
	@ResponseBody
	public DeferredResult<Map<String, ?>> getLastMsg(HttpSession session) throws Exception
	{
		DeferredResult<Map<String, ?>> result = new DeferredResult<>(0L);
		User loginUser = (User) session.getAttribute("loginUser");
		MsgManager.instance().addMonitor(loginUser.getId(), result);
		return result;
	}
}