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
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IMarketingCountService;
import com.zoj.bp.marketing.service.IOrderVisitService;

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
	
	@Autowired
	private IOrderVisitService orderVisitService;
	
	/**
	 * 跳转商务部即时统计首页
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/toMarketingCoutView")
	public String toMarketingCoutView() throws BusinessException
	{
		return "/marketing/count/index";
	}
	/**
	 * 跳转商务部历史统计首页
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/toMarketingHistoryCountView")
	public String toMarketingHistoryCountView() throws BusinessException
	{
		return "/marketing/historyCount/index";
	}
	/**
	 * 跳转业务员回访记录页面
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/toShowOrderVisitView")
	public String toShowOrderVisitView() throws BusinessException
	{
		return "/marketing/count/showOrderVisit";
	}
	/**
	 * 获取当天的在谈单回访记录
	 * @param session
	 * @param pagination
	 * @param salesmanName
	 * @return
	 */
	@RequestMapping(value = "/getTodayTalkingOrderVisitByUserId")
	@ResponseBody
	public DatagridVo<OrderVisit> getTodayTalkingOrderVisitByUserId(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String salesmanId)
	{
		return orderVisitService.getTodayTalkingOrderVisitByUserId(pagination, Integer.valueOf(salesmanId));
	}
	/**
	 * 获取商务部即时统计记录
	 * @param session
	 * @param pagination
	 * @param salesmanName 业务员名称
	 * @return
	 */
	@RequestMapping(value = "/getTodayMarketingCout")
	@ResponseBody
	public DatagridVo<MarketingCount> getTodayMarketingCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String salesmanName)
	{
		return marketingCountService.getTodayMarketingCount(pagination,salesmanName);
	}
	/**
	 * 获取指定日期的商务部历史统计记录
	 * @param session
	 * @param pagination
	 * @param salesmanName 业务员名称
	 * @param startDate	开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	@RequestMapping(value = "/getHistoryMarketingCout")
	@ResponseBody
	public DatagridVo<MarketingCount> getHistoryMarketingCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String salesmanName,
			@RequestParam(required=false) String startDate,
			@RequestParam(required=false) String endDate)
	{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		if(StringUtils.isEmpty(startDate)){
//			Calendar calendar = Calendar.getInstance();  
//			calendar.setTime(new Date());  
//			calendar.add(Calendar.DAY_OF_MONTH, -1);  
//			startDate = sdf.format(calendar.getTime());  
//		}
//		if (StringUtils.isEmpty(endDate)) {
//			Calendar calendar = Calendar.getInstance();  
//			calendar.setTime(new Date());  
//			calendar.add(Calendar.DAY_OF_MONTH, -1);  
//			endDate = sdf.format(calendar.getTime());  
//		}
		return marketingCountService.getMarketingCountByDate(pagination,salesmanName,startDate,endDate);
	}
}