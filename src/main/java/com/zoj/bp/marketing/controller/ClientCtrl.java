/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.model.User.Role;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.infocostmgr.service.IInfoCostMgrService;
import com.zoj.bp.marketing.service.IClientService;
import com.zoj.bp.marketing.service.ICommissionCostService;
import com.zoj.bp.marketing.service.IInfoCostService;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IOrderService;
import com.zoj.bp.marketing.service.IOrderVisitService;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/marketing/clientMgr")
public class ClientCtrl
{
	@Autowired
	private IOrderService orderSvc;
	
	@Autowired
	private IOrderVisitService orderVisitSvc;
	
	@Autowired
	private IClientService clientSvc;
	
	@Autowired
	private IInfoerService infoerSvc;
	
	@Autowired
	private IInfoCostService infoCostSvc;
	
	@Autowired
	private IInfoCostMgrService infoCostMgrSvc;
	
	@Autowired
	private ICommissionCostService commissionCostSvc;
	
	@Autowired
	private IUserService userSvc;
	
	@RequestMapping(value = "/toClientTraceView")
	public String toClientTraceView() throws BusinessException
	{
		return "/marketing/clientTrace/index";
	}
	
	
	@RequestMapping(value = "/toClientNegotiationView")
	public String toClientNegotiationView() throws BusinessException
	{
		return "/marketing/clientNegotiation/index";
	}
	
	/**
	 * 获取客户跟踪记录
	 * @param pagination
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllClientTrace")
	@ResponseBody
	public DatagridVo<Order> getAllClientTrace(Pagination pagination,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) String infoerName,
			@RequestParam(required=false) Integer filter,
			@RequestParam(value = "status[]",required=false) Integer[] status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.isNotEmpty(status))
		{
			while(status[0] == null)
				status = ArrayUtils.remove(status, 0);
		}
		else
			status = new Integer[]
					{
					Status.tracing.value(),
					Status.abandoned.value(),
					Status.designerRejected.value(),
					Status.talkingMarketingManagerAuditing.value(),
					Status.talkingDesignManagerAuditing.value()
					};
		if (filter == null || filter == 0)
			return orderSvc.getOrdersByUser(loginUser, pagination, null, name, tel, infoerName, StringUtils.EMPTY, status);
		else
			return orderSvc.getOrdersBySalesman(loginUser, pagination, name, tel, infoerName, StringUtils.EMPTY, status);
	}
	
	@RequestMapping(value = "/getAllClientCheck")
	@ResponseBody
	public DatagridVo<Order> getAllClientCheck(Pagination pagination,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) String infoerName,
			@RequestParam(value = "status[]",required=false) Integer[] status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.isEmpty(status))
			status = new Integer[]
					{
					Status.talkingMarketingManagerAuditing.value(),
					Status.disagreeMarketingManagerAuditing.value()
					};
		return orderSvc.getOrdersByUser(loginUser, pagination, null, name, tel, infoerName, StringUtils.EMPTY, status);
	}
	
	/**
	 * 获取客户洽谈记录
	 * @param pagination
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllClientNegotiation")
	@ResponseBody
	public DatagridVo<Order> getAllClientNegotiation(Pagination pagination,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) String infoerName,
			@RequestParam(value = "status[]",required=false) Integer[] status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.isNotEmpty(status))
		{
			while(status[0] == null)
				status = ArrayUtils.remove(status, 0);
		}else
			status = new Integer[]
					{
					Status.talkingDesignerTracing.value(),
					Status.deal.value(),
					Status.dead.value(),
					Status.disagree.value(),
					Status.disagreeDesignManagerAuditing.value(),
					Status.disagreeMarketingManagerAuditing.value()
					};
		return orderSvc.getOrdersByUser(loginUser, pagination, null, name, tel, infoerName, StringUtils.EMPTY, status);
	}
	
	@RequestMapping(value = "/getOrderById")
	@ResponseBody
	public Map<String, Object> getOrderById(@RequestParam("orderId") Integer orderId, HttpSession session) throws Exception
	{
		Order order = orderSvc.getOrderById(orderId, (User) session.getAttribute("loginUser"));
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(order);
		return map;
	}
	
	/**
	 * 新增客户的回访记录
	 * @param orderVisit
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addOrderVisit")
	@ResponseBody
	public Map<String, ?> addOrderVisit(@Valid OrderVisit orderVisit,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部人员，无法操作。"));
		orderVisit.setVisitorId(loginUser.getId());
		orderVisitSvc.addOrderVisit(orderVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 申请在谈单
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/applyOrder")
	@ResponseBody
	public Map<String, ?> applyOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部人员，无法操作。"));
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(2);
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/permitOrder")
	@ResponseBody
	public Map<String, ?> permitOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!(loginUser.isMarketingManager() || loginUser.isDesignManager()) && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是商务经理，无法执行此操作。"));
		orderApprove.setOperate(1);
		orderApprove.setApprover(loginUser.getId());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/rejectOrder")
	@ResponseBody
	public Map<String, ?> rejectOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!(loginUser.isMarketingManager() || loginUser.isDesignManager()) && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是商务经理，无法执行此操作。"));
		orderApprove.setOperate(0);
		orderApprove.setApprover(loginUser.getId());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 编辑订单
	 * @param orderForm
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editOrder")
	@ResponseBody
	public Map<String, ?> editOrder(@Valid Order orderForm, Errors errors, HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		Client client = clientSvc.getClientByOrderId(orderForm.getId());
		client.setName(orderForm.getName());
		client.setOrgAddr(orderForm.getOrgAddr());
		Order order = orderSvc.getOrderById(orderForm.getId(), (User) session.getAttribute("loginUser"));
		order.setProjectName(orderForm.getProjectName());
		order.setProjectAddr(orderForm.getProjectAddr());
		orderSvc.updateOrder(order,client);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 获取业务员回访记录
	 * @param orderId
	 * @param pagination
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getOrderVisitByOrder")
	@ResponseBody
	public DatagridVo<OrderVisit> getOrderVisitByOrder(@RequestParam("orderId") Integer orderId, Pagination pagination,HttpSession session) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return orderVisitSvc.getAllOrderVisit(pagination, loginUser.getId(), orderId);
	}
	
	/**
	 * 获取设计师回访记录
	 * @param orderId
	 * @param pagination
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getStylistOrderVisitByOrder")
	@ResponseBody
	public DatagridVo<OrderVisit> getStylistOrderVisitByOrder(
			@RequestParam("orderId") Integer orderId, Pagination pagination,HttpSession session) throws BusinessException
	{
		Order order = orderSvc.getOrderById(orderId, (User) session.getAttribute("loginUser"));
		return orderVisitSvc.getAllOrderVisit(pagination, order.getDesignerId(), orderId);
	}
	
	/**
	 * 放弃客户
	 * @param orderIds
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteOrderByIds")
	@ResponseBody
	public Map<String, ?> deleteOrderByIds(@RequestParam("delIds[]") Integer[] orderIds, HttpSession session) throws Exception
	{
		if(ArrayUtils.isEmpty(orderIds))
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("没有可放弃的客户"));
		orderSvc.deleteOrderByIds(orderIds);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 根据业务员查询信息员
	 * @param session
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findInfoerBySalesmanId")
	@ResponseBody
	public Map<String, ?> findInfoerBySalesmanId(HttpSession session,Pagination pagination)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return ResponseUtils.buildRespMapByBean(infoerSvc.findBySalesmanId(loginUser.getId(),pagination, null));
	}
	
	@RequestMapping(value = "/getInfoCostByOrder")
	@ResponseBody
	public DatagridVo<InfoCost> getInfoCostByOrder(
			@RequestParam("orderId") Integer orderId,Pagination pagination)
	{
		return infoCostSvc.getAllInfoCost(pagination,null,orderId);
	}
	
	@RequestMapping(value = "/getCommissionCostByOrder")
	@ResponseBody
	public DatagridVo<CommissionCost> getCommissionCostByOrder(
			@RequestParam("orderId") Integer orderId,Pagination pagination)
	{
		return commissionCostSvc.getAllCommissionCost(pagination,null,orderId);
	}
	
	@RequestMapping("/showSelectInfoerWindow")
	public String showSelectInfoerWindow()
	{
		return "marketing/clientTrace/selectInfoer";
	}
	
	@RequestMapping("/showAllSalesman")
	public String showAllSalesman()
	{
		return "marketing/clientTrace/selectSalesman";
	}
	
	@RequestMapping(value = "/getAllSalesman")
	@ResponseBody
	public DatagridVo<User> getAllSalesman(Pagination pagination) throws BusinessException
	{
		Integer[] roles = {Role.marketingSalesman.value(),Role.marketingLeader.value(),Role.marketingManager.value()};//1：市场部业务员；2：市场部主管；3：市场部经理
		return userSvc.getAllUserByRole(pagination, StringUtils.EMPTY, StringUtils.EMPTY, roles);
	}
	
	@RequestMapping(value = "/transferOrder")
	@ResponseBody
	public Map<String, ?> transferOrder(HttpSession session,
			@RequestParam("orderIds[]") Integer[] orderIds,@RequestParam("salesmanId") Integer salesmanId) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，你不是商务部经理，无法进行业务转移。"));
		orderSvc.updateOrderSalesmanId(orderIds,salesmanId);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/showAddInfoCostWindow")
	public ModelAndView showAddInfoCostWindow(HttpSession session, @RequestParam(value="orderId") Integer orderId)
	{
		ModelAndView mv = new ModelAndView("marketing/clientNegotiation/addInfoCost", "errorMsg", null);
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			mv.addObject("errorMsg", "对不起，你不是商务部经理，无法新增信息费。");
		com.zoj.bp.costmgr.infocostmgr.vo.InfoCost infoCost = infoCostMgrSvc.getInfoCostByOrder(orderId);
		if(infoCost.getCost() != null)		//已打款
			mv.addObject("errorMsg", MessageFormat.format("客户[{0}]已打款，无法再次打款，请刷新后重试。", infoCost.getClientName()));
		mv.addObject("infoCost", infoCost);
		return mv;
	}
}