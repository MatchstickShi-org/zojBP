/**
 * 
 */
package com.zoj.bp.sysmgr.usermgr.service;

import java.util.List;

import com.zoj.bp.common.model.Menu;
import com.zoj.bp.common.model.User;


/**
 * @author Administrator
 *
 */
public interface IMenuService
{
	List<Menu> getMenusByUser(User user);
}