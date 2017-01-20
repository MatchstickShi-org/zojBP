/**
 * 
 */
package com.zoj.bp.design.controller;

import java.math.BigDecimal;
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
import com.zoj.bp.common.model.OrderApprove.Operate;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.model.User.Role;
import com.zoj.bp.common.service.IOrderApproveService;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.service.IDesignerVisitApplyService;
import com.zoj.bp.design.vo.DesignerVisitApply;
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
@RequestMapping("/design/clientMgr")
public class DesignClientCtrl
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
	private IDesignerVisitApplyService visitApplySvc;
	
	@Autowired
	private ICommissionCostService commissionCostSvc;
	
	@Autowired
	private IUserService userSvc;
	
	@Autowired
	private IOrderApproveService orderApproveSvc;
	
	@RequestMapping(value = "/toClientNegotiationView")
	public ModelAndView toClientNegotiationView(HttpSession session) throws BusinessException
	{
		return new ModelAndView("/design/clientNegotiation/index",
				"underling", userSvc.getDesignUnderlingByUser((User) session.getAttribute("loginUser")));
	}
	
	/**
	 * 获取客户洽谈记录
	 * @param pagination
	 * @param clientName
	 * @param tel
	 * @param infoerName
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllClientNegotiation")
	@ResponseBody
	public DatagridVo<Order> getAllClientNegotiation(Pagination pagination,
			@RequestParam(required=false) String clientName,
			@RequestParam(required=false) Integer orderId,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) Integer designerId,
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
		{
			status = new Integer[]
			{
				Status.designerRejected.value(),
				Status.talkingDesignManagerAuditing.value(),
				Status.talkingDesignerTracing.value(),
				Status.deal.value(),
				Status.dead.value(),
				Status.disagree.value(),
				Status.disagreeDesignManagerAuditing.value(),
				Status.disagreeMarketingManagerAuditing.value()
			};
		}
		if (filter == null || filter == 1)
			return orderSvc.getOrdersByDesigner(pagination, loginUser, clientName, orderId, tel, StringUtils.EMPTY, designerId, status);
		else
			return orderSvc.getOrdersByUser(loginUser, pagination, clientName, orderId, tel, StringUtils.EMPTY, designerId, null, status);
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
	 * 获取当前订单的审批流程
	 * @param pagination
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/getOrderApproveByOrderId")
	@ResponseBody
	public DatagridVo<OrderApprove> getOrderApproveByOrderId(Pagination pagination,@RequestParam(required=false) Integer orderId,HttpSession session)
	{
		return orderApproveSvc.getAllOrderApprove(pagination, null, orderId);
	}
	
	/**
	 * 新增设计师的回访记录
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
		if(!loginUser.isSuperAdmin())
			orderVisit.setVisitorId(loginUser.getId());
		orderVisitSvc.addOrderVisit(orderVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	/**
	 * 新增设计师回访记录批示
	 * @param orderVisit
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addVisitComment")
	@ResponseBody
	public Map<String, ?> addVisitComment(@RequestParam Integer id,@RequestParam String comment,HttpSession session) throws Exception
	{
		OrderVisit orderVisit = orderVisitSvc.getOrderVisitById(id);
		orderVisit.setComment(comment);
		orderVisitSvc.updateOrderVisit(orderVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 已签单
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dealOrder")
	@ResponseBody
	public Map<String, ?> dealOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		if(orderApprove.getDealAmount().compareTo(BigDecimal.ZERO) <= 0)
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg("请输入正确的签单金额！")));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(Operate.permit.value());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	/**
	 * 死单
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deadOrder")
	@ResponseBody
	public Map<String, ?> deadOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(Operate.reject.value());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	/**
	 * 不准单
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/disagreeOrder")
	@ResponseBody
	public Map<String, ?> disagreeOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setClaimerName(loginUser.getAlias());
		orderApprove.setOperate(Operate.apply.value());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 打回正跟踪
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/repulseOrder")
	@ResponseBody
	public Map<String, ?> repulseOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(Operate.repulse.value());
		orderApprove.setDesignerName(loginUser.getAlias());
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
		orderSvc.updateOrder(order, null);
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
		Order order = orderSvc.getOrderById(orderId, loginUser);
		return orderVisitSvc.getAllOrderVisit(pagination,order.getSalesmanId(), orderId);
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
		orderSvc.giveUpOrders(orderIds);
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
	public Map<String, ?> findInfoerBySalesmanId(HttpSession session,Pagination pagination) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null)
			return ResponseUtils.buildRespMap(ReturnCode.SESSION_TIME_OUT);
		return ResponseUtils.buildRespMapByBean(infoerSvc.findBySalesmanId(loginUser.getId(),pagination, null));
	}
	
	@RequestMapping(value = "/getInfoCostByOrder")
	@ResponseBody
	public DatagridVo<InfoCost> getInfoCostByOrder(@RequestParam("orderId") Integer orderId,Pagination pagination) throws BusinessException
	{
		return infoCostSvc.getAllInfoCost(pagination,null,orderId);
	}
	
	/**
	 * 获取提成打款记录
	 * @param orderId
	 * @param pagination
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getCommissionCostByOrder")
	@ResponseBody
	public DatagridVo<CommissionCost> getCommissionCostByOrder(@RequestParam("orderId") Integer orderId,Pagination pagination) throws BusinessException
	{
		return commissionCostSvc.getAllCommissionCost(pagination,null,orderId);
	}
	
	@RequestMapping("/showDesignerForTransfer")
	public String showDesignerForTransfer()
	{
		return "design/clientNegotiation/selectDesignerForTransfer";
	}
	
	/**
	 * 获取在职的设计师
	 * @param pagination
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getAllDesigner")
	@ResponseBody
	public DatagridVo<User> getAllDesigner(Pagination pagination) throws BusinessException
	{
		Integer[] roles = {Role.designDesigner.value(),Role.designLeader.value(),Role.designManager.value()};//4：设计部设计师；5：设计部主管；6：设计部经理
		return userSvc.getAllUserByRole(pagination, "", "", roles);
	}
	
	/**
	 * 设计部业务转移
	 * @param orderIds
	 * @param designerId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transferOrder")
	@ResponseBody
	public Map<String, ?> transferOrder(HttpSession session,
			@RequestParam("orderIds[]") Integer[] orderIds,@RequestParam("designerId") Integer designerId) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isDesignManager() && !loginUser.isSuperAdmin())
			ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是主案部经理，无法进行业务转移。"));
		orderSvc.updateOrderDesigerId(orderIds,designerId);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 设计师回访申请
	 * @param session
	 * @param designerVisitApply
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/visitApply")
	@ResponseBody
	public Map<String, ?> visitApply(HttpSession session,@Valid DesignerVisitApply designerVisitApply, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongDesign())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是主案部人员，无法进行回访申请操作。"));
		designerVisitApply.setDesigner(loginUser.getId());
		designerVisitApply.setStatus(0);//未审核
		visitApplySvc.addDesignerVisitApply(designerVisitApply);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 审核回访申请
	 * @param session
	 * @param designerVisitApply
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkVisitApply")
	@ResponseBody
	public Map<String, ?> checkVisitApply(HttpSession session,@RequestParam("id") Integer id) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongDesign())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是主案部经理，无法进行此操作。"));
		DesignerVisitApply designerVisitApply = visitApplySvc.getDesignerVisitApplyById(id);
		if(designerVisitApply.getStatus() == 1)
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，该客户回访申请已同意，无需再次操作。"));
		designerVisitApply.setApprover(loginUser.getId());
		designerVisitApply.setStatus(1);//已审核
		visitApplySvc.updateVisitApply(designerVisitApply);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/toDesignerVisitApplyView")
	public String toDesignerVisitApplyView() throws BusinessException
	{
		return "/design/applyVisit/index";
	}
	
	/**
	 * 获取所有设计师回访申请
	 * @param orderId
	 * @param pagination
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getAllDesignerVisitApply")
	@ResponseBody
	public DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,HttpSession session,
			@RequestParam(required=false) Integer orderId,
			@RequestParam(required=false) String designerName,
			@RequestParam(value = "status[]",required=false) Integer[] status) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isDesignManager() && !loginUser.isSuperAdmin())
			ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是主案部经理，无法查看回访申请。"));
		if(ArrayUtils.isNotEmpty(status))
		{
			while(status[0] == null)
				status = ArrayUtils.remove(status, 0);
		}
		else
			status = new Integer[]{0,1};
		return visitApplySvc.getTodayDesignerVisitApplys(pagination, designerName, orderId, status);
	}
	
}