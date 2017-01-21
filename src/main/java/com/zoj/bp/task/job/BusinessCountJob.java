package com.zoj.bp.task.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.model.User;
import com.zoj.bp.design.service.IDesignCountService;
import com.zoj.bp.marketing.service.IMarketingCountService;
import com.zoj.bp.sysmgr.usermgr.service.IUserService;

/**
 * 业务统计任务
 * @author wangw
 */
public class BusinessCountJob
{
	private static Logger logger = LoggerFactory.getLogger(BusinessCountJob.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IMarketingCountService marketringCountService;
	
	@Autowired
	private IDesignCountService designCountService;

	public void businessCount()
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<User> salesmans = userService.getInServiceMarketingUsers();
			if(CollectionUtils.isNotEmpty(salesmans)){
				for(User user:salesmans){
					List<MarketingCount> lastMarketingCout = marketringCountService.getLastMarketingCountByUsetrId(user.getId());
					String countDate = sdf.format(new Date());
					//如果有统计记录，则取这条记录的更新时间
					if(CollectionUtils.isNotEmpty(lastMarketingCout)){
						Calendar cal = Calendar.getInstance();
						Calendar aCalendar = Calendar.getInstance();
						//最近一次统计时间
						Date lastMarketingCoutUpdateTime = lastMarketingCout.get(0).getUpdateTime();
						//当前日期
					    aCalendar.setTime(new Date());
					    int nowDay = aCalendar.get(Calendar.DAY_OF_YEAR);
					    //最近一次统计日期
					    aCalendar.setTime(lastMarketingCoutUpdateTime);
					    int lastUpdateDay = aCalendar.get(Calendar.DAY_OF_YEAR);
					    int days = nowDay-lastUpdateDay;
					    if(days >1){//如果相差日期大于一天，则开始日期需要加一天（sql中会减去一天）
					    	for(int i = 0;i<days; i++){
					    		cal.setTime(lastMarketingCoutUpdateTime);
					    		cal.add(Calendar.DATE, i);
					    		countDate = sdf.format(cal.getTime());
					    		MarketingCount marketingCount = marketringCountService.getTodayMarketingCountByUserId(user.getId(),countDate);
					    		marketingCount.setCountDate(sdf.parse(countDate));
								marketringCountService.addMarketingCount(marketingCount);
					    	}
					    }else{
					    	countDate = sdf.format(new Date().getTime()-24*60*60*1000);
					    	MarketingCount marketingCount = marketringCountService.getTodayMarketingCountByUserId(user.getId(),countDate);
					    	marketingCount.setCountDate(sdf.parse(countDate));
							marketringCountService.addMarketingCount(marketingCount);
					    }
					}else{
						countDate = sdf.format(new Date().getTime()-24*60*60*1000);
						MarketingCount marketingCount = marketringCountService.getTodayMarketingCountByUserId(user.getId(),countDate);
						marketingCount.setCountDate(sdf.parse(countDate));
						marketringCountService.addMarketingCount(marketingCount);
					}
				}
			}
			logger.info("商务部业务统计完成。");
			
			List<User> designers = userService.getInServiceDesignUsers();
			if(CollectionUtils.isNotEmpty(designers)){
				for(User user:designers){
					List<DesignCount> lastDesignCount = designCountService.getLastDesignCountByUsetrId(user.getId());
					String countDate = sdf.format(new Date());
					//如果有统计记录，则取这条记录的更新时间
					if(CollectionUtils.isNotEmpty(lastDesignCount)){
						Calendar cal = Calendar.getInstance();
						Calendar aCalendar = Calendar.getInstance();
						//最近一次统计时间
						Date lastDesignCoutUpdateTime = lastDesignCount.get(0).getUpdateTime();
						//当前日期
					    aCalendar.setTime(new Date());
					    int nowDay = aCalendar.get(Calendar.DAY_OF_YEAR);
					    //最近一次统计日期
					    aCalendar.setTime(lastDesignCoutUpdateTime);
					    int lastUpdateDay = aCalendar.get(Calendar.DAY_OF_YEAR);
					    int days = nowDay-lastUpdateDay;
					    if(days >1){//如果相差日期大于一天，则开始日期需要加一天（sql中会减去一天）
					    	for(int i = 0;i<days; i++){
					    		cal.setTime(lastDesignCoutUpdateTime);
					    		cal.add(Calendar.DATE, i);
					    		countDate = sdf.format(cal.getTime());
					    		DesignCount designCount = designCountService.getTodayDesignCountByUserId(user.getId(),countDate);
					    		designCount.setCountDate(sdf.parse(countDate));
								designCountService.addDesignCount(designCount);
					    	}
					    }else{
					    	countDate = sdf.format(new Date().getTime()-24*60*60*1000);
					    	DesignCount designCount = designCountService.getTodayDesignCountByUserId(user.getId(),countDate);
					    	designCount.setCountDate(sdf.parse(countDate));
							designCountService.addDesignCount(designCount);
					    }
					}else{
						countDate = sdf.format(new Date().getTime()-24*60*60*1000);
						DesignCount designCount = designCountService.getTodayDesignCountByUserId(user.getId(),countDate);
						designCount.setCountDate(sdf.parse(countDate));
						designCountService.addDesignCount(designCount);
					}
				}
			}
			logger.info("主案部业务统计完成。");
		}
		catch (Exception e)
		{
			logger.error("业务统计定时任务出错", e);
		}
	}
}