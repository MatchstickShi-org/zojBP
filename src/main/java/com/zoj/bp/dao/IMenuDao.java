/**
 * 
 */
package com.zoj.bp.dao;

import java.util.List;

import com.zoj.bp.model.Menu;

/**
 * @author Administrator
 *
 */
public interface IMenuDao
{
	List<Menu> getMenusByRole(Integer role);

	/**
	 * @return
	 */
	List<Menu> getAllMenus();
}