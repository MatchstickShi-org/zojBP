package com.zoj.bp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zoj.bp.excption.BusinessException;
import com.zoj.bp.model.User;

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
}