/**  */
package com.zoj.bp.common.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @author MatchstickShi
 */
public class User implements Serializable
{
	private static final long serialVersionUID = -6475342050918755832L;
	
	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	
	/**name*/
	@NotNull
	private String name;
	
	@NotNull
	private String alias;
	
	@NotNull
	private String pwd;
	
	@NotNull
	private Integer role = 1;
	
	private Integer leaderId;
	
	private Integer status;
	
	private List<Menu> menus;
	
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

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public Integer getRole()
	{
		return role;
	}

	public void setRole(Integer role)
	{
		this.role = role;
	}

	public List<Menu> getMenus()
	{
		return menus;
	}

	public void setMenus(List<Menu> menus)
	{
		this.menus = menus;
	}

	/**
	 * @return
	 */
	public boolean isAdmin()
	{
		return this.role == Role.admin.getValue();
	}
	
	public enum Role
	{
		/**管理员*/
		admin(0),
		/**市场部-业务员*/
		marketingSalesman(1),
		/**市场部-主管*/
		marketingLeader(2),
		/**市场部-经理*/
		marketingManager(3),
		/**设计部-设计师*/
		designDesigner(4),
		/**设计部-主管*/
		designLeader(5),
		/**设计部-经理*/
		designManager(6);
		
		private Role(Integer value)
		{
			this.value = value;
		}
		
		private Integer value;
		
		public Integer getValue()
		{
			return this.value;
		}
	}

	public Integer getLeaderId()
	{
		return leaderId;
	}

	public void setLeaderId(Integer leaderId)
	{
		this.leaderId = leaderId;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
}