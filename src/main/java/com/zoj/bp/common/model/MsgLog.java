/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author MatchstickShi
 */
public class MsgLog implements Serializable
{
	private static final long serialVersionUID = 8288607008180366038L;

	private Integer id;
	
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue)
	private Integer targetUser;
	
	private String content;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date sendTime;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getTargetUser()
	{
		return targetUser;
	}

	public void setTargetUser(Integer targetUser)
	{
		this.targetUser = targetUser;
	}

	public Date getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}