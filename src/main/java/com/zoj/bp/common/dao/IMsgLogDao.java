/**  */
package com.zoj.bp.common.dao;

import java.util.List;

import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 */
public interface IMsgLogDao
{
	DatagridVo<MsgLog> getAllBroadcastMsgs(Pagination pagination);
	
	Integer addBroadcastMsg(String content);

	/**
	 * @param msgId 
	 * @return
	 */
	MsgLog getMgsById(Integer msgId);

	/**
	 * @return
	 */
	List<MsgLog> getLast24hoursBroadcastMsgs();

	/**
	 * @param userId
	 * @param pagination
	 * @return
	 */
	DatagridVo<MsgLog> getMsgsByUser(Integer userId, Pagination pagination);

	/**
	 * @param msg
	 * @return
	 */
	Integer addMsgLog(MsgLog msg);
}