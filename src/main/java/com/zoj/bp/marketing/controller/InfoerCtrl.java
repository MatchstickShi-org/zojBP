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
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IInfoerVisitService;

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
	private IInfoerVisitService infoerVisitSvc;
	
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
		return infoerSvc.getAllInfoer(pagination, loginUser,name,tel,level);
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
	
	@RequestMapping(value = "/getInfoerVisitByInfoer")
	@ResponseBody
	public DatagridVo<InfoerVisit> getInfoerVisitByInfoer(@RequestParam("infoerId") Integer infoerId, Pagination pagination) throws BusinessException
	{
		return infoerVisitSvc.getAllInfoerVisit(pagination,infoerId);
	}
}