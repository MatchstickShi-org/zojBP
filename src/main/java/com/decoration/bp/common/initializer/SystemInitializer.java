/**  */
package com.decoration.bp.common.initializer;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.decoration.bp.common.model.MsgLog;
import com.decoration.bp.common.msg.MsgManager;
import com.decoration.bp.common.service.IMsgLogService;

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
		MsgManager.instance().setMsgs(ms.toArray(new MsgLog[ms.size()]));
	}
}