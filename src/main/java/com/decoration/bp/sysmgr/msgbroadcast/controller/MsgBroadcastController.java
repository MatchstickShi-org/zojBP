/**  */
package com.decoration.bp.sysmgr.msgbroadcast.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.decoration.bp.common.excption.ReturnCode;
import com.decoration.bp.common.model.MsgLog;
import com.decoration.bp.common.service.IMsgLogService;
import com.decoration.bp.common.util.ResponseUtils;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 */
@Controller
@RequestMapping("/sysMgr/msgBroadcast")
public class MsgBroadcastController
{
	@Autowired
	private IMsgLogService msgLogSvc;
	
	@RequestMapping("/toMsgBroadcastView")
	public String toMsgBroadcastView()
	{
		return "sysMgr/msgBroadcast/index";
	}
	
	@RequestMapping("/getAllBroadcastMsgs")
	@ResponseBody
	public DatagridVo<MsgLog> getAllBroadcastMsgs(Pagination pagination)
	{
		return msgLogSvc.getAllBroadcastMsgs(pagination);
	}
	
	@RequestMapping("/addBroadcastMsg")
	@ResponseBody
	public Map<String, ?> addBroadcastMsg(@RequestParam String context) throws Exception
	{
		msgLogSvc.addBroadcastMsg(context);
		return ResponseUtils.buildRespMap(ReturnCode.SUCCESS);
	}
}