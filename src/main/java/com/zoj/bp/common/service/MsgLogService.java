/**  */
package com.zoj.bp.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.dao.IMsgLogDao;
import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.msg.BroadcastMsgManager;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 */
@Service
public class MsgLogService implements IMsgLogService
{
	private static Logger logger = LoggerFactory.getLogger(MsgLogService.class);
	
	@Autowired
	private IMsgLogDao msgLogDao;
	
	@Override
	public DatagridVo<MsgLog> getAllBroadcastMsgs(Pagination pagination)
	{
		return msgLogDao.getAllBroadcastMsgs(pagination);
	}

	@Override
	public Integer addBroadcastMsg(String content) throws Exception
	{
		Integer msgId = msgLogDao.addBroadcastMsg(content);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					BroadcastMsgManager.instance().addMsg(msgLogDao.getMgsById(msgId));
				}
				catch (Exception e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}).start();
		return msgId;
	}

	@Override
	public List<MsgLog> getLast24hoursBroadcastMsgs()
	{
		return msgLogDao.getLast24hoursBroadcastMsgs();
	}
}