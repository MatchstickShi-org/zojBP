/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.service.IOrderApproveService;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IClientService;
import com.zoj.bp.marketing.service.IOrderService;
import com.zoj.bp.marketing.service.IOrderVisitService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/marketing/clientCheckMgr")
public class ClientCheckCtrl
{
	@Autowired
	private IOrderService orderSvc;
	
	@Autowired
	private IOrderApproveService orderApproveSvc;
	
	@Autowired
	private IOrderVisitService orderVisitSvc;
	
	@Autowired
	private IClientService clientSvc;
	
	@RequestMapping(value = "/toClientCheckView")
	public String toClientCheckView() throws BusinessException
	{
		return "/marketing/clientCheck/index";
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
	 * 获取客户跟踪记录
	 * @param pagination
	 * @param name
	 * @param tel
	 * @param infoerName
	 * @param status
	 * @param session
	 * @return
	 */
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
		{
			status = new Integer[]
			{
				Status.talkingMarketingManagerAuditing.value(),
				Status.disagreeMarketingManagerAuditing.value()
			};
		}
		return orderSvc.getOrdersByUser(loginUser, pagination, name, tel, infoerName, status);
	}
	
	/**
	 * 根据客户Id查询客户详情
	 * @param orderId
	 * @param session
	 * @return
	 * @throws Exception
	 */
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
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage()));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部人员，无法操作。"));
		if(!loginUser.isSuperAdmin())
			orderVisit.setVisitorId(loginUser.getId());
		orderVisitSvc.addOrderVisit(orderVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 审核通过
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/permitOrder")
	@ResponseBody
	public Map<String, ?> permitOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage()));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!(loginUser.isMarketingManager() || loginUser.isDesignManager()) && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是商务经理，无法执行此操作。"));
		orderApprove.setOperate(1);
		orderApprove.setApprover(loginUser.getId());
		orderSvc.addOrderApprove(orderApprove);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/**
	 * 审核驳回
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/rejectOrder")
	@ResponseBody
	public Map<String, ?> rejectOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage()));
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
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL);
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
		Order order = orderSvc.getOrderById(orderId, (User) session.getAttribute("loginUser"));
		return orderVisitSvc.getAllOrderVisit(pagination, order.getSalesmanId(), orderId);
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
}