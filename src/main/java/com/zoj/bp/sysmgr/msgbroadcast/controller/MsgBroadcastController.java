/**  */
package com.zoj.bp.sysmgr.msgbroadcast.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.excption.ReturnCode;
import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.service.IMsgLogService;
import com.zoj.bp.common.util.ResponseUtils;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

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