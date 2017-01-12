/**
 * 
 */
package com.zoj.bp.marketing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.marketing.service.IMarketingCountService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/marketing/countMgr")
public class CountCtrl
{
	@Autowired
	private IMarketingCountService marketingCountService;
	
	@RequestMapping(value = "/toMarketingCout")
	public String toMarketingCout() throws BusinessException
	{
		return "/marketing/count/index";
	}
	/**
	 * 获取当天的商务部统计记录
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getTodayMarketingCout")
	@ResponseBody
	public Map<String, ?> getTodayMarketingCout(HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongMarketing() || !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是商务部人员，无法操作。"));
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("list", marketingCountService.getTodayMarketingCount());
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS,returnMap);
	}
}