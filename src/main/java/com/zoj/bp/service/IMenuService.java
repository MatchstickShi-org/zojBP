/**
 * 
 */
package com.zoj.bp.service;

import java.util.List;

import com.zoj.bp.model.Menu;
import com.zoj.bp.model.User;


/**
 * @author Administrator
 *
 */
public interface IMenuService
{
	List<Menu> getMenusByUser(User user);
}