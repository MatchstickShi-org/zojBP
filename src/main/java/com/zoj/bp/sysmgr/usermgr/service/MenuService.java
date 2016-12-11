package com.zoj.bp.sysmgr.usermgr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoj.bp.common.model.Menu;
import com.zoj.bp.common.model.User;
import com.zoj.bp.sysmgr.usermgr.dao.IMenuDao;

/**
 * @author MatchstickShi
 */
@Service
public class MenuService implements IMenuService
{
	@Autowired
	private IMenuDao menuDao;

	@Override
	public List<Menu> getMenusByUser(User user)
	{
		return this.buildMenus(user.isAdmin() ? menuDao.getAllMenus() : menuDao.getMenusByRole(user.getRole()));
	}

	/**
	 * @param ms
	 * @return
	 */
	private List<Menu> buildMenus(List<Menu> menus)
	{
		if(CollectionUtils.isEmpty(menus))
			return Collections.<Menu>emptyList();
		
		Map<Integer, Menu> parentIdMenus = new LinkedHashMap<>();
		menus.stream().forEach(m -> 
		{
			if(m.getParentId() == null)
			{
				if(!parentIdMenus.containsKey(m.getId()))
					parentIdMenus.put(m.getId(), m);
			}
			else
				parentIdMenus.get(m.getParentId()).addChildMenu(m);
		});
		return new ArrayList<>(parentIdMenus.values());
	}
}