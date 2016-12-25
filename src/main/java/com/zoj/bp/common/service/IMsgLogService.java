/**  */
package com.zoj.bp.common.service;

import java.util.List;

import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 */
public interface IMsgLogService
{
	DatagridVo<MsgLog> getAllBroadcastMsgs(Pagination pagination);
	
	Integer addBroadcastMsg(String content) throws Exception;

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
}