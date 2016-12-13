/**  */
package com.zoj.bp.common.msg;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.util.DateTimeUtils;
import com.zoj.bp.common.util.ResponseUtils;

/**
 * 消息广播处理器（只保留最近24小时的广播消息）
 * @author MatchstickShi
 */
public class BroadcastMsgManager
{
	private static Logger logger = LoggerFactory.getLogger(BroadcastMsgManager.class);
	
	private static BroadcastMsgManager instance = null;

	public static BroadcastMsgManager instance()
	{
		if(instance == null)
		{
			synchronized (BroadcastMsgManager.class)
			{
				if(instance == null)
					instance = new BroadcastMsgManager();
			}
		}
		return instance;
	}
	
	private BroadcastMsgManager(){}
	
	private final Lock lock = new ReentrantLock();
	
	private BlockingQueue<MsgLog> broadcastMsgQ = new ArrayBlockingQueue<>(50);
	
	private Map<String, DeferredResult<Map<String, ?>>> monitorMap = new ConcurrentHashMap<>();

	/**
	 * @param monitorId
	 * @param result
	 */
	public void addMonitor(String monitorId, DeferredResult<Map<String, ?>> result)
	{
		lock.lock();
		try
		{
			if (monitorMap.containsKey(monitorId))
				monitorMap.get(monitorId).setResult(ResponseUtils.buildRespMap(ReturnCode.LOGIN_USER_REPEATED));
			result.onCompletion(new Runnable()
			{
				@Override
				public void run()
				{
					monitorMap.remove(monitorId);
				}
			});
			monitorMap.put(monitorId, result);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * 获取系统缓存的所有广播消息
	 */
	public List<MsgLog> getAllMsgs()
	{
		return broadcastMsgQ.stream().collect(Collectors.toList());
	}
	
	public void addMsg(MsgLog msg) throws Exception
	{
		lock.lock();
		try
		{
			monitorMap.forEach((k, v) -> monitorMap.get(k).setResult(ResponseUtils.buildRespMap(ReturnCode.SUCCESS, "newestMsg", msg)));
			MsgLog firstMsg = broadcastMsgQ.peek();
			if(broadcastMsgQ.size() == 50 || (firstMsg != null 
					&& DateTimeUtils.instance().toDateTime(firstMsg.getSendTime()).isBefore(LocalDateTime.now().plusHours(-24))))
				broadcastMsgQ.take();
			broadcastMsgQ.put(msg);
		}
		finally
		{
			lock.unlock();
		}
	}

	public void setMsgs(MsgLog... msgs)
	{
		if(msgs != null && msgs.length > 0)
		{
			Stream.of(msgs).forEach(t ->
			{
				try
				{
					broadcastMsgQ.put(t);
				}
				catch (InterruptedException e)
				{
					logger.error(e.getMessage(), e);
				}
			});
		}
	}
}