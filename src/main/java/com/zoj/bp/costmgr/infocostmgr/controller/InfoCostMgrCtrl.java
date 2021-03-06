package com.zoj.bp.costmgr.infocostmgr.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.zoj.bp.common.util.ResponseUtils;
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
	
	@RequestMapping(value = "/showAddInfoCostWindow")
	public ModelAndView showAddInfoCostWindow(HttpSession session, @RequestParam(value="orderId") Integer orderId)
	{
		ModelAndView mv = new ModelAndView("costMgr/infoCostMgr/addInfoCost", "errorMsg", null);
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			mv.addObject("errorMsg", "对不起，您不是商务部经理，无法新增信息费。");
		InfoCost infoCost = infoCostSvc.getInfoCostByOrder(orderId);
		if(infoCost.getCost() != null)		//已打款
			mv.addObject("errorMsg", MessageFormat.format("客户[{0}]已打款，无法再次打款，请刷新后重试。", infoCost.getClientName()));
		mv.addObject("infoCost", infoCost);
		return mv;
	}
	
	@RequestMapping(value = "/addInfoCost")
	@ResponseBody
	public Map<String, ?> addInfoCost(HttpSession session, InfoCost infoCost, Errors errors) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是商务部经理，无法新增信息费。"));
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("输入参数有误，请检查后重新输入。"));
		InfoCost dbCost = infoCostSvc.getInfoCostByOrder(infoCost.getOrderId());
		if(dbCost.getCost() != null)		//已打款
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(
					MessageFormat.format("客户[{0}]已打款，无法再次打款，请刷新后重试。", infoCost.getClientName())));
		infoCostSvc.addInfoCostRecord(infoCost);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}