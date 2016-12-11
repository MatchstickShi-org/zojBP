/**  */
package com.zoj.bp.common.msg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.util.DateTimeUtils;

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
	
	private final Condition writeCondition = lock.newCondition();
	
	private final Condition readCondition = lock.newCondition();
	
	private BlockingQueue<MsgLog> broadcastMsgQ = new ArrayBlockingQueue<>(50);
	
	private List<MsgLog> newestMsgs = new ArrayList<>(50);
	
	private Map<String, List<MsgLog>> sessionMsgIdsMap = new HashMap<>();
	
	private Map<MsgLog, Set<String>> msgReceiveSessionMap = new HashMap<>();
	
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
			if(newestMsgs.size() == 50)
				writeCondition.await();		//阻塞写线程
			newestMsgs.add(msg);
			msgReceiveSessionMap.put(msg, new HashSet<>());
			readCondition.signalAll();		//唤醒所有等待中的读线程
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * 获取最新消息
	 * @return
	 * @throws Exception 
	 */
	public List<MsgLog> getNewestMsg(String sessionId) throws Exception
	{
		if (!sessionMsgIdsMap.containsKey(sessionId))
			sessionMsgIdsMap.put(sessionId, new ArrayList<>());
		lock.lock();		//锁定当前线程，不允许其他线程访问
		if (newestMsgs.size() == 0)
			readCondition.await();		//所有执行本方法的线程等待
		
		try
		{
			List<MsgLog> sessionMsgs = sessionMsgIdsMap.get(sessionId);
			List<MsgLog> newMsgs = newestMsgs.stream().filter(m -> !sessionMsgs.contains(m)).collect(Collectors.toList());
			newMsgs.forEach(m -> 
			{
				msgReceiveSessionMap.get(m).add(sessionId);
				sessionMsgs.add(m);
			});
			updateNewestMsgs();
			return newMsgs;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	private void updateNewestMsgs()
	{
		List<MsgLog> removeMsgs = new ArrayList<>();
		for(MsgLog m : newestMsgs)
		{
			if(msgReceiveSessionMap.get(m).equals(sessionMsgIdsMap.keySet()))	//消息已被所有会话接受
				removeMsgs.add(m);
		}
		
		if(!removeMsgs.isEmpty())		//从newestMsgs中移到broadcastMsgQ中
		{
			newestMsgs.removeAll(removeMsgs);
			putAll(removeMsgs);
			writeCondition.signalAll();
		}
	}

	/**
	 * @param msgs
	 * @throws InterruptedException 
	 */
	private void putAll(List<MsgLog> msgs)
	{
		msgs.stream().forEach(t ->
		{
			try
			{
				/*根据时间和broadcastMsgQ的size判断是否take;*/
				MsgLog firstMsg = broadcastMsgQ.peek();
				while(broadcastMsgQ.size() == 50 || (firstMsg != null 
						&& DateTimeUtils.instance().toDateTime(firstMsg.getSendTime()).isBefore(LocalDateTime.now().plusHours(-24))))
				{
					broadcastMsgQ.take();
					firstMsg = broadcastMsgQ.peek();
				}
				broadcastMsgQ.put(t);
			}
			catch (InterruptedException e)
			{
				logger.error(e.getMessage(), e);
			}
		});
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