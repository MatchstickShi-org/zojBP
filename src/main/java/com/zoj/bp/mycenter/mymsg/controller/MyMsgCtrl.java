package com.zoj.bp.mycenter.mymsg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.service.IMsgLogService;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/myCenter/myMsg")
public class MyMsgCtrl
{
	@Autowired
	private IMsgLogService msgSvc;
	
	@RequestMapping(value = "/toIndexView")
	public ModelAndView toIndexView() throws BusinessException
	{
		return new ModelAndView("myCenter/myMsg/index");
	}
	
	@RequestMapping(value = "/getAllMsgs")
	@ResponseBody
	public DatagridVo<MsgLog> getAllMsgs(HttpSession session, Pagination pagination) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return msgSvc.getMsgsByUser(loginUser.getId(), pagination);
	}
}