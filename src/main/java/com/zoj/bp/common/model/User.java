/**  */
package com.zoj.bp.common.model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

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
	private Long tel;
	
	@NotNull
	private Integer role = 1;
	
	private Integer leaderId;
	
	private Integer status = 1;
	
	private String leaderName;
	
	private Integer groupId;
	
	private String groupName;
	
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
	
	/**是否属于商务部人员*/
	@Transient
	public boolean isBelongMarketing()
	{
		return this.isSuperAdmin() || this.isMarketingLeader() || this.isMarketingSalesman() || this.isMarketingManager();
	}
	
	/**是否属于主案部人员*/
	@Transient
	public boolean isBelongDesign()
	{
		return this.isSuperAdmin() || this.isDesignLeader() || this.isDesignDesigner() || this.isDesignManager();
	}

	@Transient
	public boolean isMarketingManager()
	{
		return this.role == Role.marketingManager.value();
	}
	
	/**
	 * @return
	 */
	@Transient
	public boolean isSuperAdmin()
	{
		return this.role == Role.superAdmin.value();
	}

	/**
	 * @return
	 */
	@Transient
	public boolean isAdmin()
	{
		return this.role == Role.admin.value() || this.role == Role.superAdmin.value();
	}
	
	/**
	 * 是否是商务部业务员
	 * @return
	 */
	@Transient
	public boolean isMarketingSalesman()
	{
		return this.role == Role.marketingSalesman.value();
	}
	
	/**
	 * 是否是商务部主管
	 * @return
	 */
	@Transient
	public boolean isMarketingLeader()
	{
		return this.role == Role.marketingLeader.value();
	}
	
	/**
	 * 是否是主管
	 * @return
	 */
	@Transient
	public boolean isLeader()
	{
		return this.isMarketingLeader() || this.isDesignLeader();
	}
	
	/**
	 * 是否是员工（业务员或设计师）
	 * @return
	 */
	@Transient
	public boolean isStaff()
	{
		return this.isMarketingSalesman() || this.isDesignDesigner();
	}
	
	/**
	 * 是否是主案部设计师
	 * @return
	 */
	@Transient
	public boolean isDesignDesigner()
	{
		return this.role == Role.designDesigner.value();
	}
	
	/**
	 * 是否是主案部主管
	 * @return
	 */
	@Transient
	public boolean isDesignLeader()
	{
		return this.role == Role.designLeader.value();
	}
	
	/**
	 * 是否是主案部经理
	 * @return
	 */
	@Transient
	public boolean isDesignManager()
	{
		return this.role == Role.designManager.value();
	}
	
	public enum Role
	{
		/**超级管理员*/
		superAdmin(-1, "超级管理员"),
		/**管理员*/
		admin(0, "管理员"),
		/**商务部-业务员*/
		marketingSalesman(1, "业务员"),
		/**商务部-主管*/
		marketingLeader(2, "商务部主管"),
		/**商务部-经理*/
		marketingManager(3, "商务部经理"),
		/**主案部-设计师*/
		designDesigner(4, "设计师"),
		/**主案部-主管*/
		designLeader(5, "主案部主管"),
		/**主案部-经理*/
		designManager(6, "主案部经理");

		private Role(Integer value, String zhName)
		{
			this.value = value;
			this.zhName = zhName;
		}
		
		private Integer value;
		
		private String zhName;
		
		public Integer value()
		{
			return this.value;
		}
		
		public static Role valueOf(Integer role)
		{
			return Stream.of(Role.values()).filter(r -> r.value() == role).findFirst().orElse(null);
		}

		public String getZhName()
		{
			return zhName;
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

	public Long getTel()
	{
		return tel;
	}

	public void setTel(Long tel)
	{
		this.tel = tel;
	}

	public String getLeaderName()
	{
		return leaderName;
	}

	public void setLeaderName(String leaderName)
	{
		this.leaderName = leaderName;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public Integer getGroupId()
	{
		return groupId;
	}

	public void setGroupId(Integer groupId)
	{
		this.groupId = groupId;
	}
	
	public String getRoleName()
	{
		return Role.valueOf(this.role).getZhName();
	}
}