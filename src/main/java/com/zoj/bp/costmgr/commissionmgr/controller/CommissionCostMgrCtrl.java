package com.zoj.bp.costmgr.commissionmgr.controller;

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
import com.zoj.bp.costmgr.commissionmgr.service.ICommissionMgrService;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/costMgr/commissionCostMgr")
public class CommissionCostMgrCtrl
{
	@Autowired
	private ICommissionMgrService commissionCostSvc;
	
	@RequestMapping(value = "/toIndexView")
	public ModelAndView toIndexView() throws BusinessException
	{
		return new ModelAndView("costMgr/commissionCostMgr/index");
	}
	
	@RequestMapping(value = "/getAllcommissionCosts")
	@ResponseBody
	public DatagridVo<CommissionCost> getAllcommissionCosts(HttpSession session, 
			@RequestParam(value="clientName", required=false) String clientName, 
			@RequestParam(value="orderId", required=false) String orderId, 
			Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return commissionCostSvc.getAllCommissionCosts(loginUser, clientName, orderId, pagination);
	}
	
	/*@RequestMapping(value = "/showAddInfoCostWindow")
	public ModelAndView showAddInfoCostWindow(HttpSession session, @RequestParam(value="orderId") Integer orderId)
	{
		ModelAndView mv = new ModelAndView("costMgr/infoCostMgr/addInfoCost", "errorMsg", null);
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isDesignManager() && !loginUser.isSuperAdmin())
			mv.addObject("errorMsg", "对不起，你不是市场部经理，无法新增信息费。");
		InfoCost infoCost = infoCostSvc.getInfoCostByOrder(orderId);
		if(infoCost.getCost() != null)		//已打款
			mv.addObject("errorMsg", MessageFormat.format("客户[{0}]已打款，无法再次打款，请刷新后重试。", infoCost.getClientName()));
		mv.addObject("infoCost", infoCost);
		return mv;
	}
	
	@RequestMapping(value = "/addInfoCost")
	@ResponseBody
	public Map<String, ?> addInfoCost(HttpSession session, InfoCost infoCost, Errors errors)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isDesignManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是市场部经理，无法新增信息费。"));
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("输入参数有误，请检查后重新输入。"));
		infoCostSvc.addInfoCostRecord(infoCost);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}*/
}