/**
 * 
 */
package com.zoj.bp.design.controller;

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

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Client;
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
import com.zoj.bp.marketing.service.IClientService;
import com.zoj.bp.marketing.service.IOrderService;
import com.zoj.bp.marketing.service.IOrderVisitService;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/design/clientCheckMgr")
public class DesignClientCheckCtrl
{
	@Autowired
	private IOrderService orderSvc;
	
	@Autowired
	private IOrderVisitService orderVisitSvc;
	
	@Autowired
	private IClientService clientSvc;
	
	@Autowired
	private IUserService userSvc;
	
	@Autowired
	private IOrderApproveService orderApproveSvc;
	
	@RequestMapping(value = "/toClientCheckView")
	public String toClientCheckView() throws BusinessException
	{
		return "/design/clientCheck/index";
	}
	
	/**
	 * 获取待审核的客户洽谈记录
	 * @param pagination
	 * @param name
	 * @param tel
	 * @param designerName
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllClientCheck")
	@ResponseBody
	public DatagridVo<Order> getAllClientCheck(Pagination pagination,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) String designerName,
			@RequestParam(value = "status[]",required=false) Integer[] status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.isEmpty(status))
		{
			status = new Integer[]
			{
				Status.talkingDesignManagerAuditing.value(),
				Status.disagreeDesignManagerAuditing.value()
			};
		}
		return orderSvc.getOrdersByUser(loginUser, pagination, name, tel, StringUtils.EMPTY ,status);
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
	 * 审核时申请死单
	 * @param orderApprove
	 * @param errors
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkDeadOrder")
	@ResponseBody
	public Map<String, ?> checkDeadOrder(@Valid OrderApprove orderApprove,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(Operate.apply.value());
		orderSvc.addOrderApprove(orderApprove);
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
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		orderApprove.setOperate(Operate.permit.value());
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
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!(loginUser.isMarketingManager() || loginUser.isDesignManager()) && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("对不起，您不是经理职务，无法执行此操作。"));
		orderApprove.setOperate(Operate.reject.value());
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
	
	@RequestMapping("/showDesignerForPermit")
	public String showDesignerForPermit()
	{
		return "design/clientCheck/selectDesignerForPermit";
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
}