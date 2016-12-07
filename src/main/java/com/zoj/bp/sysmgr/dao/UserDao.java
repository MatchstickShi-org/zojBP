package com.zoj.bp.sysmgr.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class UserDao extends BaseDao implements IUserDao
{
	@Override
	public User getUserByName(String userName)
	{
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM USER WHERE NAME = :name",
					new MapSqlParameterSource("name", userName), BeanPropertyRowMapper.newInstance(User.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public User getUserById(Integer id)
	{
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM USER WHERE ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(User.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateUser(User user, boolean changePwd)
	{
		String sql = "UPDATE USER SET ALIAS = :alias, NAME = :name, ROLE = :role";
		if(changePwd)
			sql += ", PWD = :pwd";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public DatagridVo<User> getAllUser(Pagination pagination)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM USER";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public Integer addUser(User user)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO USER(NAME, ALIAS, PWD, ROLE) VALUES(:name, :alias, :pwd, :role)",
				new BeanPropertySqlParameterSource(user), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteUserByIds(Integer[] userIds)
	{
		return jdbcOps.update("DELETE FROM USER WHERE ID IN(" + StringUtils.join(userIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public void changePwd(Integer userId, String newPwdForMD5)
	{
		jdbcOps.update("UPDATE USER SET PWD = :pwd WHERE ID = :id", 
				new MapSqlParameterSource("pwd", newPwdForMD5).addValue("id", userId));
	}

	@Override
	public Integer removeBrandsFromUser(Integer userId, Integer[] brandIds)
	{
		return jdbcOps.update("DELETE FROM USER_PRODUCT_TYPE_RELATION"
				+ " WHERE USER_ID = :userId AND PRODUCT_TYPE_ID IN (" + StringUtils.join(brandIds, ',') + ")", 
				new MapSqlParameterSource("userId", userId));
	}

	@Override
	public Integer addBrandsToUser(Integer userId, Integer[] brandIds)
	{
		String sql = "INSERT INTO USER_PRODUCT_TYPE_RELATION(USER_ID, PRODUCT_TYPE_ID) VALUES";
		String part = "";
		for(Integer brandId : brandIds)
		{
			if(StringUtils.isNotEmpty(part))
				part += ",";
			part += "(:userId, " + brandId + ")";
		}
		sql += part + ";";
		return jdbcOps.update(sql, new MapSqlParameterSource("userId", userId));
	}

	@Override
	public Integer removeOperatorsFromBrand(Integer brandId, Integer[] userIds)
	{
		return jdbcOps.update("DELETE FROM USER_PRODUCT_TYPE_RELATION"
				+ " WHERE PRODUCT_TYPE_ID = :brandId AND USER_ID IN (" + StringUtils.join(userIds, ',') + ")", 
				new MapSqlParameterSource("brandId", brandId));
	}

	@Override
	public Integer addOperatorsToBrand(Integer brandId, Integer[] userIds)
	{
		String sql = "INSERT INTO USER_PRODUCT_TYPE_RELATION(USER_ID, PRODUCT_TYPE_ID) VALUES";
		String part = "";
		for(Integer userId : userIds)
		{
			if(StringUtils.isNotEmpty(part))
				part += ",";
			part += "(" + userId + ", :brandId)";
		}
		sql += part + ";";
		return jdbcOps.update(sql, new MapSqlParameterSource("brandId", brandId));
	}
}