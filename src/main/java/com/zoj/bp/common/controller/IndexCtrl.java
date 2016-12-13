package com.zoj.bp.common.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.msg.BroadcastMsgManager;

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
	
	@RequestMapping("/getLastBroadcastMsg")
	@ResponseBody
	public DeferredResult<Map<String, ?>> getLastBroadcastMsg(HttpSession session) throws Exception
	{
		DeferredResult<Map<String, ?>> result = new DeferredResult<>(0L);
		BroadcastMsgManager.instance().addMonitor(session.getId(), result);
		return result;
	}
}