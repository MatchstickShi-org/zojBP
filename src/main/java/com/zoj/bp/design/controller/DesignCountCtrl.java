/**
 * 
 */
package com.zoj.bp.design.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
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
	
	@RequestMapping(value = "/toDesignCoutView")
	public String toDesignCoutView() throws BusinessException
	{
		return "/design/count/index";
	}
	/**
	 * 获取当天的主案部统计记录
	 * @param session
	 * @param pagination
	 * @param designerName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/getTodayDesignCout")
	@ResponseBody
	public DatagridVo<DesignCount> getTodayDesignCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String designerName,
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate)
	{
		if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate))
			return designCountService.getTodayDesignerCount(pagination,designerName);
		else
			return designCountService.getDesignerCountByDate(pagination,designerName,startDate,endDate);
	}
}