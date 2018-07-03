/**
 * 
 */
package com.decoration.bp.sysmgr.usermgr.dao;

import java.util.List;

import com.decoration.bp.common.model.Menu;

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