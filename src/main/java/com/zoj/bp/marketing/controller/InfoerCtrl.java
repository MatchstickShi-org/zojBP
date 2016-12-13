/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.Infoer;
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
	private IInfoerService infoerService;
	
	@Autowired
	private IInfoerVisitService infoerVisitService;
	
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
		return infoerService.getAllInfoer(pagination, loginUser,name,tel,level);
	}
	
	@RequestMapping(value = "/getInfoerById")
	@ResponseBody
	public Map<String, Object> getInfoerById(@RequestParam("infoerId") Integer infoerId) throws Exception
	{
		Infoer infoer = infoerService.getInfoerById(infoerId);
		Map<String, Object> map = ResponseUtils.buildRespMapByBean(infoer);
		return map;
	}
	
}