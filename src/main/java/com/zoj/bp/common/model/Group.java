package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

/**
 * @author MatchstickShi
 */
public class Group implements Serializable
{
	private static final long serialVersionUID = -496281401422277455L;
	
	private Integer id;
	
	@NotNull
	private String name;

	@NotNull
	private Integer type;
	
	private Integer leaderId;
	
	private String leaderName;
	
	public enum Type
	{
		/**市场组*/
		marketingGroup(0),
		/**设计组*/
		designGroup(1);
		
		private Integer value;
		
		private Type(Integer value)
		{
			this.value = value;
		}
		
		public Integer value()
		{
			return value;
		}
		
		public static Type valueOf(Integer type)
		{
			return Stream.of(Type.values()).filter(r -> r.value() == type).findFirst().orElse(null);
		}
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getLeaderId()
	{
		return leaderId;
	}

	public void setLeaderId(Integer leaderId)
	{
		this.leaderId = leaderId;
	}

	public String getLeaderName()
	{
		return leaderName;
	}

	public void setLeaderName(String leaderName)
	{
		this.leaderName = leaderName;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}
}