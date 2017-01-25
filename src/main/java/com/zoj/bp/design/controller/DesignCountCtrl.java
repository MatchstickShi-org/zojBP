/**
 * 
 */
package com.zoj.bp.design.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.BusinessException;
import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.service.IDesignCountService;
import com.zoj.bp.marketing.service.IOrderVisitService;

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
	
	@Autowired
	private IOrderVisitService orderVisitService;
	
	@RequestMapping(value = "/toDesignCoutView")
	public String toDesignCoutView() throws BusinessException
	{
		return "/design/count/index";
	}
	
	@RequestMapping(value = "/toDesignHistoryCountView")
	public String toDesignHistoryCountView() throws BusinessException
	{
		return "/design/historyCount/index";
	}
	/**
	 * 跳转业务员回访记录页面
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/toShowOrderVisitView")
	public String toShowOrderVisitView() throws BusinessException
	{
		return "/design/count/showOrderVisit";
	}
	/**
	 * 获取当天的在谈单回访记录
	 * @param session
	 * @param pagination
	 * @param designerId
	 * @return
	 */
	@RequestMapping(value = "/getTodayTalkingOrderVisitByUserId")
	@ResponseBody
	public DatagridVo<OrderVisit> getTodayTalkingOrderVisitByUserId(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String designerId)
	{
		return orderVisitService.getTodayTalkingOrderVisitByUserId(pagination, Integer.valueOf(designerId));
	}
	/**
	 * 获取当天的主案部统计记录
	 * @param session
	 * @param pagination
	 * @param designerName
	 * @return
	 */
	@RequestMapping(value = "/getTodayDesignCout")
	@ResponseBody
	public DatagridVo<DesignCount> getTodayDesignCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String designerName)
	{
		return designCountService.getTodayDesignerCount(pagination,designerName);
	}
	/**
	 * 获取主案部历史统计记录
	 * @param session
	 * @param pagination
	 * @param designerName
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/getHistoryDesignCout")
	@ResponseBody
	public DatagridVo<DesignCount> getHistoryDesignCout(HttpSession session,Pagination pagination,
			@RequestParam(required=false) String designerName,
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
		return designCountService.getDesignerCountByDate(pagination,designerName,startDate,endDate);
	}
}