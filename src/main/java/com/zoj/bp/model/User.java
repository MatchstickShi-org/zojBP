/**  */
package com.zoj.bp.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

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
	private String role = "1";
	
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

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
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
		return StringUtils.equals(this.role, Role.admin.getValue());
	}
	
	public enum Role
	{
		/**管理员*/
		admin("0"),
		/**产品维护员*/
		product_operator("1"),
		/**车型维护员*/
		car_operator("2");
		
		private Role(String value)
		{
			this.value = value;
		}
		
		private String value;
		
		public String getValue()
		{
			return this.value;
		}
	}
}