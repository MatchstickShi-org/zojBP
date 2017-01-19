package com.zoj.bp.task.job;

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
			List<User> salesmans = userService.getInServiceMarketingUsers();
			if(CollectionUtils.isNotEmpty(salesmans)){
				for(User user:salesmans){
					MarketingCount marketingCount = marketringCountService.getTodayMarketingCountByUserId(user.getId());
					marketringCountService.addMarketingCount(marketingCount);
				}
			}
			logger.info("商务部业务统计完成。");
			
			List<User> designers = userService.getInServiceDesignUsers();
			if(CollectionUtils.isNotEmpty(designers)){
				for(User user:designers){
					DesignCount designCount = designCountService.getTodayDesignCountByUserId(user.getId());
					designCountService.addDesignCount(designCount);
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