package com.decoration.bp.costmgr.commissionmgr.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.decoration.bp.common.excption.BusinessException;
import com.decoration.bp.common.excption.ReturnCode;
import com.decoration.bp.common.model.Order;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.model.Order.Status;
import com.decoration.bp.common.util.ResponseUtils;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.commissionmgr.service.ICommissionCostMgrService;
import com.decoration.bp.costmgr.commissionmgr.vo.CommissionCost;
import com.decoration.bp.marketing.service.IOrderService;

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
	public DatagridVo<Order> getDealOrders(HttpSession session,
			@RequestParam(value="clientName", required=false) String clientName,
			@RequestParam(value="orderId", required=false) Integer orderId, Pagination pagination) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespDatagridVo(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是商务部经理，无法查询。"));
		return orderSvc.getOrdersByStatus(loginUser, clientName, orderId, pagination, Status.deal);
	}
	
	@RequestMapping(value = "/getCommissionCostsByOrder")
	@ResponseBody
	public DatagridVo<CommissionCost> getCommissionCostsByOrder(HttpSession session,
			@RequestParam(value="orderId") Integer orderId, Pagination pagination) throws BusinessException
	{
		//User loginUser = (User) session.getAttribute("loginUser");
		return commissionCostSvc.getCommissionCostsByOrder(orderId, pagination);
	}
	
	@RequestMapping(value = "/showAddCostWindow")
	public ModelAndView showAddCostWindow(HttpSession session, @RequestParam(value="orderId") Integer orderId)
	{
		ModelAndView mv = new ModelAndView("costMgr/commissionCostMgr/addCost", "errorMsg", null);
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			mv.addObject("errorMsg", "对不起，您不是商务部经理，无法新增提成。");
		Order order = orderSvc.getOrderById(orderId, loginUser);
		mv.addObject("order", order);
		return mv;
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