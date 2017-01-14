package com.zoj.bp.task.job;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务统计任务
 * 
 * @author wangw
 */
public class BusinessCountJob
{
	private static Logger logger = LoggerFactory.getLogger(BusinessCountJob.class);

	public void doCount()
	{
		try
		{
			System.out.println("统计结束");
			logger.info("业务统计成功!");
		} catch (Exception e)
		{
			logger.error(MessageFormat.format("业务统计定时任务出错：", e.getMessage()));
		}
	}
}