package com.zoj.bp.costmgr.commissionmgr.controller;

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
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.service.ICommissionCostMgrService;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;
import com.zoj.bp.marketing.service.IOrderService;

/**
 * @author MatchstickShi
 *
 */
@Controller
@RequestMapping("/costMgr/commissionCostMgr")
public class CommissionCostMgrCtrl
{
	@Autowired
	private ICommissionCostMgrService commissionCostSvc;
	
	@Autowired
	private IOrderService orderSvc;
	
	@RequestMapping(value = "/toIndexView")
	public ModelAndView toIndexView() throws BusinessException
	{
		return new ModelAndView("costMgr/commissionCostMgr/index");
	}
	
	@RequestMapping(value="/getDealOrders")
	@ResponseBody
	public DatagridVo<Order> getDealOrders(HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespDatagridVo(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是商务部经理，无法查询。"));
		return orderSvc.getOrdersByStatus(Status.deal);
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
	
	@RequestMapping(value = "/addCommissionCost")
	@ResponseBody
	public Map<String, ?> addCommissionCost(HttpSession session, CommissionCost commissionCost, Errors errors)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是商务部经理，无法新增提成。"));
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("输入参数有误，请检查后重新输入。"));
		commissionCostSvc.addCommissionCostRecord(commissionCost);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}