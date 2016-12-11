package com.zoj.bp.sysmgr.usermgr.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.Menu;

@Repository
public class MenuDao extends BaseDao implements IMenuDao
{
	@Override
	public List<Menu> getMenusByRole(Integer role)
	{
		return jdbcOps.query("SELECT M.* FROM MENU M "
				+ "LEFT JOIN ROLE_MENU_RELATION R ON M.ID = R.MENU_ID "
				+ "WHERE M.IS_COMMON = 1 OR R.ROLE = :ROLE "
				+ "ORDER BY M.PARENT_ID, M.IDX",
				new MapSqlParameterSource("ROLE", role), BeanPropertyRowMapper.newInstance(Menu.class));
	}

	@Override
	public List<Menu> getAllMenus()
	{
		return jdbcOps.query("SELECT M.* FROM MENU M ORDER BY M.PARENT_ID, M.IDX", BeanPropertyRowMapper.newInstance(Menu.class));
	}
}