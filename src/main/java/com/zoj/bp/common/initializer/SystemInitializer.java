/**  */
package com.zoj.bp.common.initializer;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.msg.BroadcastMsgManager;
import com.zoj.bp.common.service.IMsgLogService;

/**
 * @author MatchstickShi
 */
@Component
public class SystemInitializer implements ApplicationContextAware
{
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		IMsgLogService msgSvc = applicationContext.getBean(IMsgLogService.class);
		List<MsgLog> ms = msgSvc.getLast24hoursBroadcastMsgs();
		BroadcastMsgManager.instance().setMsgs(ms.toArray(new MsgLog[ms.size()]));
	}
}