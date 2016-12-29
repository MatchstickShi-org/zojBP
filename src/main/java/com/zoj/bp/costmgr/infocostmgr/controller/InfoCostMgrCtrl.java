package com.zoj.bp.costmgr.infocostmgr.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.infocostmgr.service.IInfoCostMgrService;
import com.zoj.bp.costmgr.infocostmgr.vo.InfoCost;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/costMgr/infoCostMgr")
public class InfoCostMgrCtrl
{
	@Autowired
	private IInfoCostMgrService infoCostSvc;
	
	@RequestMapping(value = "/toIndexView")
	public ModelAndView toIndexView() throws BusinessException
	{
		return new ModelAndView("costMgr/infoCostMgr/index");
	}
	
	@RequestMapping(value = "/getAllInfoCosts")
	@ResponseBody
	public DatagridVo<InfoCost> getAllInfoCosts(HttpSession session, 
			@RequestParam(value="status", required=false) Integer status, 
			@RequestParam(value="clientName", required=false) String clientName, 
			@RequestParam(value="orderId", required=false) String orderId, 
			Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return infoCostSvc.getAllInfoCosts(loginUser, status, clientName, orderId, pagination);
	}
}