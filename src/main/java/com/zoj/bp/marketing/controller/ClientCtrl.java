/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.Arrays;
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
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IClientService;
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
	public DatagridVo<Order> getAllClientTrace(Pagination pagination,@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,@RequestParam(required=false) String infoerName,@RequestParam(required=false) String status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		String[] statusArr = null;
		if (StringUtils.isNotEmpty(status)){
			statusArr = status.split(",");
			if(Arrays.asList(statusArr).contains("-1"))
				statusArr = new String[]{"10","12","30","32","14"};
		}else
			statusArr = new String[]{"10","12","30","32","14"};
		return orderSvc.getAllOrder(pagination,loginUser,name,tel,infoerName,statusArr);
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
	public DatagridVo<Order> getAllClientNegotiation(Pagination pagination,@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,@RequestParam(required=false) String infoerName,@RequestParam(required=false) String status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		String[] statusArr = null;
		if (StringUtils.isNotEmpty(status)){
			statusArr = status.split(",");
			if(Arrays.asList(statusArr).contains("-1"))
				statusArr = new String[]{"34","90","0","62","64","60"};
		}else
			statusArr = new String[]{"34","90","0","62","64","60"};
		return orderSvc.getAllOrder(pagination,loginUser,name,tel,infoerName,statusArr);
	}
	
	@RequestMapping(value = "/getOrderById")
	@ResponseBody
	public Map<String, Object> getOrderById(@RequestParam("orderId") Integer orderId) throws Exception
	{
		Order order = orderSvc.getOrderById(orderId);
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
		orderApprove.setClaimer(loginUser.getId());
		orderApprove.setOperate(2);
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
	public Map<String, ?> editOrder(@Valid Order orderForm, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		Client client = clientSvc.getClientByOrderId(orderForm.getId());
		client.setName(orderForm.getName());
		client.setOrgAddr(orderForm.getOrgAddr());
		Order order = orderSvc.getOrderById(orderForm.getId());
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
	public DatagridVo<OrderVisit> getStylistOrderVisitByOrder(@RequestParam("orderId") Integer orderId, Pagination pagination,HttpSession session) throws BusinessException
	{
		Order order = orderSvc.getOrderById(orderId);
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
	public Map<String, ?> findInfoerBySalesmanId(HttpSession session,Pagination pagination) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null)
			return ResponseUtils.buildRespMap(ReturnCode.SESSION_TIME_OUT);
		return ResponseUtils.buildRespMapByBean(infoerSvc.findBySalesmanId(loginUser.getId(),pagination));
	}
	
	@RequestMapping("/showSelectInfoerWindow")
	public String showSelectInfoerWindow()
	{
		return "marketing/clientTrace/selectInfoer";
	}
	
	@RequestMapping(value = "/getAllDesigner")
	@ResponseBody
	public DatagridVo<User> getAllDesigner(@RequestParam("userName") String userName,@RequestParam("alias") String alias,Pagination pagination) throws BusinessException
	{
		String[] roles = {"4","5","6"};//4：设计部设计师；5：设计部主管；6：设计部经理
		return userSvc.getAllUserByRole(pagination, userName, alias, roles);
	}
}