/**
 * 
 */
package com.zoj.bp.design.controller;

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
import com.zoj.bp.design.service.IDesignCountService;

/**
 * @author wangw
 *
 */
@Controller
@RequestMapping("/design/countMgr")
public class DesignCountCtrl
{
	@Autowired
	private IDesignCountService designCountService;
	
	@RequestMapping(value = "/toDesignCout")
	public String toDesignCout() throws BusinessException
	{
		return "/design/count/index";
	}
	/**
	 * 获取当天的主案部统计记录
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getTodayDesignCout")
	@ResponseBody
	public Map<String, ?> getTodayDesignCout(HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		if(!loginUser.isBelongDesign() || !loginUser.isSuperAdmin())
			return ResponseUtils.buildRespMap(ReturnCode.VALIDATE_FAIL.setMsg("你不是主案部人员，无法操作。"));
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("list", designCountService.getTodayDesignerCount());
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS,returnMap);
	}
}