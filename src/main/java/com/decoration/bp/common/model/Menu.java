package com.decoration.bp.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MatchstickShi
 */
public class Menu implements Serializable
{
	private static final long serialVersionUID = -1848376457107580025L;
	
	/**id INT(20) not null AUTO_INCREMENT*/
	private Integer id;
	/**name*/
	private String name;
	
	private String url;
	
	private Integer parentId;
	
	private Integer idx;
	
	private List<Menu> childMenus = new ArrayList<>();

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

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public List<Menu> getChildMenus()
	{
		return childMenus;
	}

	public void setChildMenus(List<Menu> childMenus)
	{
		this.childMenus = childMenus;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	/**
	 * @param m
	 */
	public void addChildMenu(Menu m)
	{
		this.childMenus.add(m);
	}

	public Integer getIdx()
	{
		return idx;
	}

	public void setIdx(Integer idx)
	{
		this.idx = idx;
	}
}