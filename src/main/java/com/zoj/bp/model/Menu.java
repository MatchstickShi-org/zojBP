package com.zoj.bp.model;

import java.io.Serializable;

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
}