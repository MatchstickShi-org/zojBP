package com.zoj.bp.sysmgr.usermgr.dao;

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
import com.zoj.bp.common.model.User.Role;
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
			return jdbcOps.queryForObject(
					"SELECT U.*, G.ID GROUP_ID, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
					+ " LEFT JOIN USER_GROUP_MEMBER M ON U.ID = M.MEMBER_ID "
					+ " LEFT JOIN USER_GROUP G ON M.GROUP_ID = G.ID "
					+ " LEFT JOIN USER LU ON G.LEADER_ID = LU.ID "
					+ " WHERE U.ID = :id ",
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
		String sql = "UPDATE USER SET ALIAS = :alias, NAME = :name, ROLE = :role, TEL = :tel";
		if(changePwd)
			sql += ", PWD = :pwd";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public DatagridVo<User> getAllUser(Pagination pagination, String userName, String alias)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, G.ID GROUP_ID, G.NAME GROUP_NAME, G.LEADER_ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U"
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON G.LEADER_ID = LU.ID "
				+ " WHERE 1=1 ";
		if(StringUtils.isNotEmpty(userName))
		{
			sql += " AND U.NAME LIKE :name";
			paramMap.put("name", '%' + userName + '%');
		}
		if (StringUtils.isNotEmpty(alias))
		{
			sql += " AND U.ALIAS LIKE :alias";
			paramMap.put("alias", '%' + alias + '%');
		}
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
				"INSERT INTO USER(NAME, ALIAS, PWD, ROLE, STATUS, TEL) VALUES(:name, :alias, :pwd, :role, :status, :tel)",
				new BeanPropertySqlParameterSource(user), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteUserByIds(Integer[] userIds)
	{
		return jdbcOps.update("UPDATE USER SET STATUS = 0 "
				+ " WHERE ID IN(" + StringUtils.join(userIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer revertUserByIds(Integer[] userIds)
	{
		return jdbcOps.update("UPDATE USER SET STATUS = 1 WHERE ID IN(" + StringUtils.join(userIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public void changePwd(Integer userId, String newPwdForMD5)
	{
		jdbcOps.update("UPDATE USER SET PWD = :pwd WHERE ID = :id", 
				new MapSqlParameterSource("pwd", newPwdForMD5).addValue("id", userId));
	}

	@Override
	public DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME, G.ID GROUP_ID, G.NAME GROUP_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON LU.ID = G.LEADER_ID "
				+ " WHERE M.PARENT_ID = :userId AND U.STATUS = 1 AND M.LEADER_ID <> U.ID ";
		paramMap.put("userId", userId);

		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY U.ALIAS LIMIT :start, :rows ";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public DatagridVo<User> getNotAssignUnderling(User leader, Pagination pagination)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME, G.ID GROUP_ID, G.NAME GROUP_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON LU.ID = G.LEADER_ID "
				+ " WHERE U.STATUS = 1 AND U.ROLE = :role AND (G.LEADER_ID <> :userId OR G.LEADER_ID IS NULL)";
		paramMap.put("userId", leader.getId());
		paramMap.put("role", leader.isMarketingLeader() ? Role.marketingSalesman.value() : Role.designDesigner.value());

		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY U.ALIAS LIMIT :start, :rows ";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public Integer removeUnderling(Integer[] underlingIds)
	{
		return jdbcOps.update("UPDATE USER SET GROUP_ID = NULL "
				+ " WHERE USER_ID IN (" + StringUtils.join(underlingIds, ',') + ")", 
				EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer removeUnderlingFromLeader(Integer userId, Integer[] underlingIds)
	{
		return jdbcOps.update("UPDATE USER SET GROUP_ID = NULL "
				+ " WHERE GROUP_ID = (SELECT ID FROM `GROUP` WHERE LEADER_ID = :leaderId) "
				+ " AND USER_ID IN (" + StringUtils.join(underlingIds, ',') + ")", 
				new MapSqlParameterSource("leaderId", userId));
	}

	@Override
	public Integer addUnderlingToLeader(Integer userId, Integer... underlingIds)
	{
		StringBuffer sql = new StringBuffer(
				" UPDATE USER SET GROUP_ID = (SELECT ID FROM `GROUP` WHERE LEADER_ID = :leaderId) "
				+ " WHERE ID IN (" + StringUtils.join(underlingIds, ',') + ")");
		return jdbcOps.update(sql.toString(), new MapSqlParameterSource("leaderId", userId));
	}
	
	@Override
	public void setLeaderToEmployee(Integer leaderId, Integer newGroupId)
	{
		jdbcOps.update("UPDATE `GROUP` SET LEADER_ID = NULL WHERE LEADER_ID = :leaderId",
				new MapSqlParameterSource("leaderId", leaderId));
		if (newGroupId != null)
		{
			jdbcOps.update("UPDATE USER SET GROUP_ID = :newGroupId WHERE ID = :leaderId",
					new MapSqlParameterSource("newGroupId", newGroupId).addValue("leaderId", leaderId));
		}
	}

	@Override
	public void setUserToLeader(Integer userId, Integer leadGroupId)
	{
		if(leadGroupId != null)
			jdbcOps.update("UPDATE `GROUP` SET LEADER_ID = :userId WHERE ID = :groupId",
					new MapSqlParameterSource("userId", userId).addValue("groupId", leadGroupId));
	}
}