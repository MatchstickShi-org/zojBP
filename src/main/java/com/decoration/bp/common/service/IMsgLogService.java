/**  */
package com.decoration.bp.common.service;

import java.util.List;

import com.decoration.bp.common.model.MsgLog;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

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