package com.decoration.bp.sysmgr.usermgr.dao;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.decoration.bp.common.dao.BaseDao;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.model.Group.Type;
import com.decoration.bp.common.model.User.Role;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

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
			MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
					.addValue("marketingLeader", Role.marketingLeader.value())
					.addValue("designLeader", Role.designLeader.value()).addValue("id", id);
			return jdbcOps.queryForObject(
					"SELECT U.*, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
					+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
					+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID "
					+ " AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
					+ " WHERE U.ID = :id ",
					params, BeanPropertyRowMapper.newInstance(User.class));
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
		String sql = "SELECT U.*, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID "
				+ " AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
				+ " WHERE 1=1 ";
		MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
				.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value());
		if(StringUtils.isNotEmpty(userName))
		{
			sql += " AND U.NAME LIKE :name";
			params.addValue("name", '%' + userName + '%');
		}
		if (StringUtils.isNotEmpty(alias))
		{
			sql += " AND U.ALIAS LIKE :alias";
			params.addValue("alias", '%' + alias + '%');
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow());
		params.addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public DatagridVo<User> getAllUserByRole(Pagination pagination, String userName, String alias,Integer... roles)
	{
		String sql = "SELECT U.*, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID "
				/* LEFT JOIN LU 需要加上条件LU.ROLE = 主管，否则会出现多个重复user（根据groupId会查出多个user） */
				+ " AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
				+ " WHERE 1=1 ";
		MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
				.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value());
		if(StringUtils.isNotEmpty(userName))
		{
			sql += " AND U.NAME LIKE :name";
			params.addValue("name", '%' + userName + '%');
		}
		if (StringUtils.isNotEmpty(alias))
		{
			sql += " AND U.ALIAS LIKE :alias";
			params.addValue("alias", '%' + alias + '%');
		}
		if(ArrayUtils.isNotEmpty(roles))
			sql +=" AND U.ROLE IN(" + StringUtils.join(roles, ',') + ")";
		sql +=" AND U.STATUS = 1";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow());
		params.addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}
	
	@Override
	public List<User> getUsersByRole(Integer role)
	{
		return jdbcOps.query("SELECT U.* FROM USER U WHERE U.ROLE = :role",
				new MapSqlParameterSource("role", role), BeanPropertyRowMapper.newInstance(User.class));
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
	public Integer setUserToDimission(Integer[] userIds)
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
	public List<User> getUnderlingByLeader(Integer userId)
	{
		String sql = "SELECT U.* FROM USER U "
			+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
			+ " LEFT JOIN USER LU ON LU.GROUP_ID = G.ID "
			+ " 	AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
			+ " WHERE LU.ID = :userId AND U.STATUS = 1 "
			+ " 	AND (U.ROLE = CASE G.TYPE WHEN :marketGroup THEN :salesman ELSE :designer END) ";
		MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
			.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value())
			.addValue("salesman", Role.marketingSalesman.value()).addValue("designer", Role.designDesigner.value()).addValue("userId", userId);
		return jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class));
	}

	@Override
	public DatagridVo<User> getAssignedUnderling(Integer userId, Pagination pagination)
	{
		String sql = "SELECT U.*, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME, G.NAME GROUP_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON LU.GROUP_ID = G.ID "
				+ " 	AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
				+ " WHERE LU.ID = :userId AND U.STATUS = 1 "
				+ " 	AND (U.ROLE = CASE G.TYPE WHEN :marketGroup THEN :salesman ELSE :designer END) ";
		MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
				.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value()) 
				.addValue("salesman", Role.marketingSalesman.value()).addValue("designer", Role.designDesigner.value()) 
				.addValue("userId", userId);

		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY U.ALIAS LIMIT :start, :rows ";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public DatagridVo<User> getNotAssignUnderling(User leader, Pagination pagination)
	{
		String sql = "SELECT U.*, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME, G.NAME GROUP_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON U.GROUP_ID = G.ID "
				+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID "
				+ " AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
				+ " WHERE U.STATUS = 1 AND U.ROLE = :role AND (U.GROUP_ID <> :groupId OR U.GROUP_ID IS NULL)";
		MapSqlParameterSource params = new MapSqlParameterSource("marketGroup", Type.marketingGroup.value())
				.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value())
				.addValue("groupId", leader.getGroupId())
				.addValue("role", leader.isMarketingLeader() ? Role.marketingSalesman.value() : Role.designDesigner.value());

		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY U.ALIAS LIMIT :start, :rows ";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public Integer removeUnderling(Integer[] underlingIds)
	{
		return jdbcOps.update("UPDATE USER SET GROUP_ID = NULL "
				+ " WHERE ID IN (" + StringUtils.join(underlingIds, ',') + ")", 
				EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer removeUnderlingFromLeader(Integer userId, Integer[] underlingIds)
	{
		return jdbcOps.update("UPDATE USER SET GROUP_ID = NULL "
				+ " WHERE GROUP_ID = (SELECT A.GROUP_ID FROM (SELECT GROUP_ID FROM `USER` WHERE ID = :leaderId) A) "
				+ " AND ID IN (" + StringUtils.join(underlingIds, ',') + ")", 
				new MapSqlParameterSource("leaderId", userId));
	}

	@Override
	public Integer addUnderlingToLeader(Integer userId, Integer... underlingIds)
	{
		StringBuffer sql = new StringBuffer(
				" UPDATE USER SET GROUP_ID = "
				+ " (SELECT A.GROUP_ID FROM (SELECT U.GROUP_ID FROM `USER` U WHERE U.ID = :leaderId) A) "
				+ " WHERE ID IN (" + StringUtils.join(underlingIds, ',') + ")");
		return jdbcOps.update(sql.toString(), new MapSqlParameterSource("leaderId", userId));
	}

	@Override
	public Integer deleteByUsers(Integer... userIds)
	{
		return jdbcOps.update("DELETE FROM USER WHERE ID IN (" + StringUtils.join(userIds, ',') + ")",
				EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public List<User> getMarketingUsersByStatus(Integer status)
	{
		return jdbcOps.query("SELECT U.* FROM USER U WHERE (U.ROLE = 1 OR U.ROLE = 2 OR U.ROLE = 3) AND STATUS = :status",
				new MapSqlParameterSource("status", status), BeanPropertyRowMapper.newInstance(User.class));
	}
	
	@Override
	public List<User> getDesignUsersByStatus(Integer status)
	{
		return jdbcOps.query("SELECT U.* FROM USER U WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND STATUS = :status",
				new MapSqlParameterSource("status", status), BeanPropertyRowMapper.newInstance(User.class));
	}
}