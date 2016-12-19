/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IClientService;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IInfoerVisitService;
import com.zoj.bp.marketing.service.IOrderService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/marketing/clientMgr")
public class ClientCtrl
{
	@Autowired
	private IClientService clientSvc;
	
	@Autowired
	private IOrderService orderSvc;
	
	@Autowired
	private IInfoerVisitService infoerVisitSvc;
	
	@RequestMapping(value = "/toClientTraceView")
	public String toClientTraceView() throws BusinessException
	{
		return "/marketing/clientTrace/index";
	}
	
	@RequestMapping(value = "/getAllClientTrace")
	@ResponseBody
	public DatagridVo<Order> getAllClientTrace(Pagination pagination,@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,@RequestParam(required=false) String infoerName,@RequestParam(required=false) String status, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return orderSvc.getAllOrder(pagination,loginUser,name,tel,infoerName,status);
	}
	
	@RequestMapping(value = "/getOrderById")
	@ResponseBody
	public Map<String, Object> getOrderById(@RequestParam("orderId") Integer orderId) throws Exception
	{
		Order order = orderSvc.getOrderById(orderId);
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(order);
		return map;
	}
	
	@RequestMapping(value = "/addOrderVisit")
	@ResponseBody
	public Map<String, ?> addOrderVisit(@Valid OrderVisit orderVisit,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		//infoerVisit.setSalesmanId(loginUser.getId());
		//infoerVisitSvc.addInfoerVisit(infoerVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/editOrder")
	@ResponseBody
	public Map<String, ?> editOrder(@Valid Order order, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		orderSvc.updateOrder(order);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	/*@RequestMapping(value = "/getInfoerVisitByInfoer")
	@ResponseBody
	public DatagridVo<InfoerVisit> getInfoerVisitByInfoer(@RequestParam("infoerId") Integer infoerId, Pagination pagination) throws BusinessException
	{
		return infoerVisitSvc.getAllInfoerVisit(pagination,infoerId);
	}*/
}