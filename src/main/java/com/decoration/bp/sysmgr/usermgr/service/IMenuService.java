/**
 * 
 */
package com.decoration.bp.sysmgr.usermgr.service;

import java.util.List;

import com.decoration.bp.common.model.Menu;
import com.decoration.bp.common.model.User;


/**
 * @author Administrator
 *
 */
public interface IMenuService
{
	List<Menu> getMenusByUser(User user);
}