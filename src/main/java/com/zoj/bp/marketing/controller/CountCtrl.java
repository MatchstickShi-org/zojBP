/**
 * 
 */
package com.zoj.bp.marketing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
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
	
	/**
	 * 跳转商务部统计首页
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/toMarketingCoutView")
	public String toMarketingCoutView() throws BusinessException
	{
		return "/marketing/count/index";
	}
	/**
	 * 获取指定日期的商务部统计记录
	 * @param session
	 * @param pagination
	 * @param salesmanName 业务员名称
	 * @param startDate	开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	@RequestMapping(value = "/getTodayMarketingCout")
	@ResponseBody
	public DatagridVo<MarketingCount> getTodayMarketingCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String salesmanName,
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate)
	{
		return marketingCountService.getTodayMarketingCount(pagination,salesmanName,startDate,endDate);
	}
}