/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.ICommissionCostService;
import com.zoj.bp.marketing.service.IInfoCostService;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IInfoerVisitService;
import com.zoj.bp.marketing.service.IOrderService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/marketing/infoerMgr")
public class InfoerCtrl
{
	@Autowired
	private IInfoerService infoerSvc;
	
	@Autowired
	private IOrderService orderSvc;
	
	@Autowired
	private IInfoerVisitService infoerVisitSvc;
	
	@Autowired
	private IInfoCostService infoCostSvc;
	
	@Autowired
	private ICommissionCostService commissionCostSvc;
	
	@RequestMapping(value = "/toInfoSrcMgrView")
	public String toInfoSrcMgrView() throws BusinessException
	{
		return "/marketing/infoerMgr/index";
	}
	
	@RequestMapping(value = "/getAllInfoers")
	@ResponseBody
	public DatagridVo<Infoer> getAllInfoers(Pagination pagination,@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,@RequestParam(required=false) String level, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		String[] levelArr = null;
		if (StringUtils.isNotEmpty(level))
			levelArr = level.split(",");
		return infoerSvc.getAllInfoer(pagination, loginUser,name,tel,levelArr);
	}
	
	@RequestMapping(value = "/getInfoerById")
	@ResponseBody
	public Map<String, Object> getInfoerById(@RequestParam("infoerId") Integer infoerId) throws Exception
	{
		Infoer infoer = infoerSvc.getInfoerById(infoerId);
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(infoer);
		return map;
	}
	

	@RequestMapping(value = "/addInfoer")
	@ResponseBody
	public Map<String, ?> addInfoer(@Valid Infoer infoer,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		infoer.setSalesmanId(loginUser.getId());
		infoerSvc.addInfoer(infoer);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/addClient")
	@ResponseBody
	public Map<String, ?> addClient(@Valid Order order,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		order.setSalesmanId(loginUser.getId());
		order.setStatus(10);//状态为正跟踪
		orderSvc.addOrderAndClient(order);
		Infoer infoer = infoerSvc.getInfoerById(order.getInfoerId());
		/**
		 * 如果当前信息员等级为铁牌，则新增客户的时候更新等级为铜牌
		 */
		if (infoer.getLevel() == 4) {
			infoer.setLevel(3);
			infoerSvc.updateInfoer(infoer);
		}
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/addInfoerVisit")
	@ResponseBody
	public Map<String, ?> addInfoerVisit(@Valid InfoerVisit infoerVisit,Errors errors,HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		infoerVisit.setSalesmanId(loginUser.getId());
		infoerVisitSvc.addInfoerVisit(infoerVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/editInfoer")
	@ResponseBody
	public Map<String, ?> editInfoer(@Valid Infoer infoer, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL));
		infoerSvc.updateInfoer(infoer);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/checkInfoerTel")
	@ResponseBody
	public Map<String, ?> checkInfoerTel(@RequestParam String tel) throws Exception
	{
		Infoer infoer = null;
		if(StringUtils.isNotEmpty(tel)){
			infoer = infoerSvc.findByTel(tel);
		}
		if(infoer != null)
			return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg("重复！该号码于 "+infoer.getInsertTime()+" 被业务员 "+infoer.getSalesmanName()+" 录入！"));
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/checkClientTel")
	@ResponseBody
	public Map<String, ?> checkClientTel(@RequestParam String tel) throws Exception
	{
		Order order = null;
		if(StringUtils.isNotEmpty(tel)){
			order = orderSvc.findByTel(tel);
		}
		if(order != null)
			return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg("重复！该号码于 "+order.getInsertTime()+"被业务员 "+order.getSalesmanName()+" 录入！"));
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/getInfoerVisitByInfoer")
	@ResponseBody
	public DatagridVo<InfoerVisit> getInfoerVisitByInfoer(@RequestParam("infoerId") Integer infoerId, Pagination pagination) throws BusinessException
	{
		return infoerVisitSvc.getAllInfoerVisit(pagination,infoerId);
	}
	
	@RequestMapping(value = "/getClientByInfoer")
	@ResponseBody
	public DatagridVo<Order> getClientByInfoer(@RequestParam("infoerId") Integer infoerId,HttpSession session,Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return orderSvc.getAllOrder(pagination, loginUser, infoerId, null);
	}
	
	@RequestMapping(value = "/getOrderByInfoer")
	@ResponseBody
	public DatagridVo<Order> getOrderByInfoer(@RequestParam("infoerId") Integer infoerId,HttpSession session,Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		Integer[] status = {14,30,32,34};  //在谈单状态：14：在谈单-设计师已打回；30：在谈单-市场部经理审核中；32：在谈单-设计部经理审核中；34：在谈单-设计师跟踪中
		return orderSvc.getAllOrder(pagination, loginUser, infoerId, status);
	}
	
	@RequestMapping(value = "/getInfoCostByInfoer")
	@ResponseBody
	public DatagridVo<InfoCost> getInfoCostByInfoer(@RequestParam("infoerId") Integer infoerId,Pagination pagination) throws BusinessException
	{
		return infoCostSvc.getAllInfoCost(pagination, infoerId);
	}
	
	@RequestMapping(value = "/getCommissionCostByInfoer")
	@ResponseBody
	public DatagridVo<CommissionCost> getCommissionCostByInfoer(@RequestParam("infoerId") Integer infoerId,Pagination pagination) throws BusinessException
	{
		return commissionCostSvc.getAllCommissionCost(pagination, infoerId);
	}
}