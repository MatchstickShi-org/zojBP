/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.List;
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
import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.model.InfoCost;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.model.User.Role;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.ICommissionCostService;
import com.zoj.bp.marketing.service.IInfoCostService;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IInfoerVisitService;
import com.zoj.bp.marketing.service.IOrderService;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

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
	
	@Autowired
	private IUserService userSvc;
	
	@RequestMapping(value = "/toInfoSrcMgrView")
	public ModelAndView toInfoSrcMgrView(HttpSession session) throws BusinessException
	{
		return new ModelAndView("/marketing/infoerMgr/index",
				"underling", userSvc.getMarketUnderlingByUser((User) session.getAttribute("loginUser")));
	}
	
	@RequestMapping(value = "/getAllInfoers")
	@ResponseBody
	public DatagridVo<Infoer> getAllInfoers(Pagination pagination,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String tel,
			@RequestParam(required=false) Integer filter,
			@RequestParam(required=false) Integer salesmanId,
			@RequestParam(value = "level[]", required=false) Integer[] levels, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(ArrayUtils.isNotEmpty(levels))
		{
			while(levels[0] == null)
				levels = ArrayUtils.remove(levels, 0);
		}
		try
		{
			return infoerSvc.getAllInfoer(pagination,
					loginUser, name, tel, salesmanId, (filter == null || filter == 1) ? false : true, levels);
		}
		catch (BusinessException e)
		{
			return DatagridVo.<Infoer>emptyVo(e.getReturnCode());
		}
	}
	
	@RequestMapping(value = "/getInfoerById")
	@ResponseBody
	public Map<String, Object> getInfoerById(@RequestParam("infoerId") Integer infoerId, HttpSession session) throws Exception
	{
		Infoer infoer = infoerSvc.getInfoerById(infoerId, (User) session.getAttribute("loginUser"));
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(infoer);
		return map;
	}
	

	@RequestMapping(value = "/addInfoer")
	@ResponseBody
	public Map<String, ?> addInfoer(@Valid Infoer infoer, Errors errors, HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage()));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("您不是商务部人员，无法新增信息员。"));
		Infoer infoerTel = null;
		if(infoer != null)
		{
			infoerTel = infoerSvc.findByTel(infoer, loginUser);
			if(infoerTel != null)
				return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg("重复！该信息员于 "+infoerTel.getInsertTime()+" 被业务员 "+infoerTel.getSalesmanName()+" 录入！"));
		}
		infoer.setSalesmanId(loginUser.getId());
		infoerSvc.addInfoer(infoer);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/addClient")
	@ResponseBody
	public Map<String, ?> addClient(@Valid Order order, Errors errors, HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		Order orderTel = null;
		if(order != null)
		{
			List<String> tels = order.getTels();
			orderTel = orderSvc.findByTels(loginUser, tels.toArray(new String[tels.size()]));
			if(orderTel != null)
				return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg(
						"重复！该客户[" + order.getId() + "]于 "+orderTel.getInsertTime()+" 被业务员["+orderTel.getSalesmanName()+"]录入。"));
		}
		if(!loginUser.isSuperAdmin())
			order.setSalesmanId(loginUser.getId());
		order.setStatus(Status.tracing.value());//状态为正跟踪
		orderSvc.addOrderAndClient(order);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/addInfoerVisit")
	@ResponseBody
	public Map<String, ?> addInfoerVisit(@Valid InfoerVisit infoerVisit, Errors errors, HttpSession session) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(new BusinessException(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage())));
		User loginUser = (User) session.getAttribute("loginUser");
		if(loginUser.getId() != infoerVisit.getSalesmanId() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是该信息员的业务员，无法新增。"));
		infoerVisitSvc.addInfoerVisit(infoerVisit);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/editInfoer")
	@ResponseBody
	public Map<String, ?> editInfoer(HttpSession session, @Valid Infoer infoer, Errors errors) throws Exception
	{
		if(errors.hasErrors())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg(errors.getFieldError().getDefaultMessage()));
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部人员，无法新增信息员。"));
		infoerSvc.updateInfoer(infoer);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/transferInfoer")
	@ResponseBody
	public Map<String, ?> transferInfoer(HttpSession session,
			@RequestParam("infoerIds[]") Integer[] infoerIds, @RequestParam("salesmanId") Integer salesmanId) throws Exception
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isMarketingManager() && !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部经理，无法操作。"));
		infoerSvc.updateInfoerSalesmanId(infoerIds, salesmanId);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/checkInfoerTel")
	@ResponseBody
	public Map<String, ?> checkInfoerTel(@RequestParam String tel, HttpSession session) throws Exception
	{
		if(StringUtils.isNotEmpty(tel)){
			Infoer infoer = new Infoer();
			infoer.setTel1(tel);
			infoer = infoerSvc.findByTel(infoer, (User) session.getAttribute("loginUser"));
			if(infoer != null && infoer.getId() >0)
			return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg(
					"重复！信息员["+infoer.getName()+"]电话["+tel+"]于"+infoer.getInsertTime()+" 被业务员["+infoer.getSalesmanName()+"]录入。"));
		}
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
	
	@RequestMapping(value = "/checkClientTel")
	@ResponseBody
	public Map<String, ?> checkClientTel(@RequestParam String tel, HttpSession session) throws Exception
	{
		Order order = null;
		if(StringUtils.isNotEmpty(tel))
			order = orderSvc.findByTels((User) session.getAttribute("loginUser"), tel);
		if(order != null)
			return ResponseUtils.buildRespMap(ReturnCode.TEL_EXISTS.setMsg(
					"重复！客户[" + order.getId() + "]电话["+tel+"]于 "+order.getInsertTime()+" 被业务员["+order.getSalesmanName()+"]录入。"));
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
	public DatagridVo<Order> getClientByInfoer(@RequestParam("infoerId") Integer infoerId, HttpSession session, Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return orderSvc.getOrdersByInfoer(pagination, loginUser, infoerId);
	}
	
	@RequestMapping(value = "/getOrderByInfoer")
	@ResponseBody
	public DatagridVo<Order> getOrderByInfoer(@RequestParam("infoerId") Integer infoerId, HttpSession session, Pagination pagination) throws BusinessException
	{
		User loginUser = (User) session.getAttribute("loginUser");
		Integer[] status =
		{
			Status.designerRejected.value(),
			Status.talkingMarketingManagerAuditing.value(),
			Status.talkingDesignManagerAuditing.value(),
			Status.talkingDesignerTracing.value()
		};  //在谈单状态：14：在谈单-设计师已打回；30：在谈单-市场部经理审核中；32：在谈单-设计部经理审核中；34：在谈单-设计师跟踪中
		return orderSvc.getOrdersByInfoer(pagination, loginUser, infoerId, status);
	}
	
	@RequestMapping(value = "/getInfoCostByInfoer")
	@ResponseBody
	public DatagridVo<InfoCost> getInfoCostByInfoer(@RequestParam("infoerId") Integer infoerId,Pagination pagination) throws BusinessException
	{
		return infoCostSvc.getAllInfoCost(pagination, infoerId, null);
	}
	
	@RequestMapping(value = "/getCommissionCostByInfoer")
	@ResponseBody
	public DatagridVo<CommissionCost> getCommissionCostByInfoer(@RequestParam("infoerId") Integer infoerId,Pagination pagination) throws BusinessException
	{
		return commissionCostSvc.getAllCommissionCost(pagination, infoerId, null);
	}
	
	@RequestMapping("/showAllSalesman")
	public String showAllSalesman()
	{
		return "marketing/infoerMgr/selectSalesman";
	}
	
	@RequestMapping(value = "/getAllSalesman")
	@ResponseBody
	public DatagridVo<User> getAllSalesman(Pagination pagination) throws BusinessException
	{
		Integer[] roles = {Role.marketingSalesman.value(),Role.marketingLeader.value(),Role.marketingManager.value()};//1：市场部业务员；2：市场部主管；3：市场部经理
		return userSvc.getAllUserByRole(pagination, "", "", roles);
	}
}