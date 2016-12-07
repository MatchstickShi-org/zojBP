/**
 * 
 */
package com.zoj.bp.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Administrator
 *
 */
public class TreeVo implements Serializable
{
	public TreeVo()
	{
		super();
	}

	/**
	 * @param id
	 * @param text
	 */
	public TreeVo(Integer id, String text)
	{
		super();
		this.id = id;
		this.text = text;
	}

	private static final long serialVersionUID = 7820278168285255980L;
	
	private Integer id;
	
	private String text;
	
	private String state;
	
	private boolean checked;
	
	private Map<String, Object> attributes = new TreeMap<>();
	
	private List<TreeVo> children;
	
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String attrName, Class<T> cls)
	{
		return (T) this.attributes.get(attrName);
	}
	
	public void setAttr(String attrName, Object attrValue)
	{
		this.attributes.put(attrName, attrValue);
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public List<TreeVo> addChildren(TreeVo child)
	{
		if(children == null)
			children = new ArrayList<>();
		if(child != null)
			children.add(child);
		return children;
	}

	public List<TreeVo> getChildren()
	{
		return children;
	}

	public void setChildren(List<TreeVo> children)
	{
		this.children = children;
	}

	public Integer getParentId()
	{
		return attributes.containsKey("parentId") ? Integer.valueOf((String) attributes.get("parentId")) : null;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public Map<String, Object> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes)
	{
		this.attributes = attributes;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
}