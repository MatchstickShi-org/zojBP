package com.decoration.bp.sysmgr.groupmgr.dao;

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
import com.decoration.bp.common.model.Group;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.model.Group.Type;
import com.decoration.bp.common.model.User.Role;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

@Repository
public class GroupDao extends BaseDao implements IGroupDao
{
	@Override
	public DatagridVo<Group> getAllGroups(Pagination pagination, Integer type)
	{
		String sql = "SELECT G.*, U.ID LEADER_ID, U.ALIAS LEADER_NAME FROM `GROUP` G "
				+ " LEFT JOIN USER U ON G.ID = U.GROUP_ID AND (U.ROLE = :marketLeader OR U.ROLE = :designLeader) "
				+ " WHERE 1=1 ";
		MapSqlParameterSource params = 
				new MapSqlParameterSource("marketLeader", Role.marketingLeader.value())
					.addValue("designLeader", Role.designLeader.value());
		if(type != null)
		{
			sql += " AND G.TYPE = :type";
			params.addValue("type", type);
		}
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY G.NAME LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(Group.class)), count);
	}
	
	@Override
	public Integer addGroup(String name, boolean isMarketingGroup)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update("INSERT INTO `GROUP`(NAME, TYPE) VALUES(:name, :type)",
				new MapSqlParameterSource("name", name)
					.addValue("type", isMarketingGroup ? 0 : 1), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Group getGroupById(Integer groupId)
	{
		try
		{
			return jdbcOps.queryForObject(
					"SELECT G.*, U.ID LEADER_ID, U.ALIAS LEADER_NAME FROM `GROUP` G "
					+ " LEFT JOIN USER U ON G.ID = U.GROUP_ID AND (U.ROLE = :marketLeader OR U.ROLE = :designLeader) "
					+ " WHERE G.ID = :id ",
					new MapSqlParameterSource("id", groupId).addValue("marketLeader", Role.marketingLeader.value())
						.addValue("designLeader", Role.designLeader.value()), BeanPropertyRowMapper.newInstance(Group.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public Group getGroupByName(String name)
	{
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM `GROUP` WHERE NAME = :name",
					new MapSqlParameterSource("name", name), BeanPropertyRowMapper.newInstance(Group.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public Integer updateGroup(Group group)
	{
		String sql = "UPDATE `GROUP` SET NAME = :name WHERE ID = :id";
		return jdbcOps.update(sql, new BeanPropertySqlParameterSource(group));
	}

	@Override
	public Integer deleteGroupByIds(Integer[] grpIds)
	{
		return jdbcOps.update("DELETE FROM `GROUP` "
				+ " WHERE ID IN(" + StringUtils.join(grpIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public DatagridVo<User> getAssignedUnderling(Integer groupId, Pagination pagination)
	{
		String sql = "SELECT U.*, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON G.ID = U.GROUP_ID "
				+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID "
				+ " AND (LU.ROLE = CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END) "
				+ " WHERE U.GROUP_ID = :groupId AND U.STATUS = 1 "
				+ " AND (U.ROLE = CASE G.TYPE WHEN :marketGroup THEN :salesman ELSE :designer END) ";
		MapSqlParameterSource params = new MapSqlParameterSource("groupId", groupId)
				.addValue("marketGroup", Type.marketingGroup.value())
				.addValue("salesman", Role.marketingSalesman.value()).addValue("designer", Role.designDesigner.value())
				.addValue("marketingLeader", Role.marketingLeader.value()).addValue("designLeader", Role.designLeader.value());
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public DatagridVo<User> getNotAssignUnderling(Integer groupId, Pagination pagination)
	{
		Group g = getGroupById(groupId);
		String sql = "SELECT U.*, G.NAME GROUP_NAME, LU.ID LEADER_ID, LU.ALIAS LEADER_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON G.ID = U.GROUP_ID "
				+ " LEFT JOIN USER LU ON G.ID = LU.GROUP_ID AND LU.ROLE = :leaderRole "
				+ " WHERE U.ROLE = :role AND (U.GROUP_ID <> :groupId OR U.GROUP_ID IS NULL) ";
		MapSqlParameterSource params = new MapSqlParameterSource("groupId", groupId)
				.addValue("leaderRole", Type.valueOf(g.getType()) 
						== Type.marketingGroup ? Role.marketingLeader.value() : Role.designLeader.value())
				.addValue("role", Type.valueOf(g.getType()) 
						== Type.marketingGroup ? Role.marketingSalesman.value() : Role.designDesigner.value());
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public Integer addUnderling2Group(Integer groupId, Integer... underlingIds)
	{
		StringBuffer sql = new StringBuffer(" UPDATE USER SET GROUP_ID = :groupId WHERE 1=1 ");
		if(!ArrayUtils.isEmpty(underlingIds))
			sql.append(" AND ID IN (" + StringUtils.join(underlingIds, ',') + ")");
		return jdbcOps.update(sql.toString(), new MapSqlParameterSource("groupId", groupId));
	}

	@Override
	public Integer removeUnderlingFromGroup(Integer groupId, Integer... underlingIds)
	{
		StringBuffer sql = new StringBuffer(" UPDATE USER SET GROUP_ID = NULL WHERE GROUP_ID = :groupId ");
		if(!ArrayUtils.isEmpty(underlingIds))
			sql.append(" AND ID IN (" + StringUtils.join(underlingIds, ',') + ")");
		return jdbcOps.update(sql.toString(), new MapSqlParameterSource("groupId", groupId));
	}

	@Override
	public Integer removeUnderlingFromGroups(Integer... grpIds)
	{
		if(ArrayUtils.isEmpty(grpIds))
			return 0;
		StringBuffer sql = new StringBuffer(
				" UPDATE USER SET GROUP_ID = NULL WHERE GROUP_ID IN (" + StringUtils.join(grpIds, ',') + ")");
		return jdbcOps.update(sql.toString(), EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public DatagridVo<User> getCanAssignLeadersByGroup(Integer groupId, Pagination pagination)
	{
		String sql = "SELECT U.*, G.NAME GROUP_NAME FROM USER U "
				+ " LEFT JOIN `GROUP` G ON G.ID = U.GROUP_ID "
				+ " WHERE 1=1 ";
		Integer role = Role.marketingLeader.value();
		if(groupId == null)
			sql += " AND (U.ROLE = :marketingLeader OR U.ROLE = :designLeader) ";
		else
		{
			Group g = getGroupById(groupId);
			if(g.getType() == 1)
				role =  Role.designLeader.value();
			sql += " AND U.ROLE = :role ";
		}
		sql += "AND (U.GROUP_ID <> :groupId OR U.GROUP_ID IS NULL) ";

		MapSqlParameterSource params = new MapSqlParameterSource("marketingLeader", Role.marketingLeader.value())
			.addValue("designLeader", Role.designLeader.value()).addValue("role", role)
			.addValue("marketGroup", Type.marketingGroup.value()).addValue("groupId", groupId);
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " LIMIT :start, :rows";
		params.addValue("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params, BeanPropertyRowMapper.newInstance(User.class)), count);
	}

	@Override
	public Integer removeLeaderFromGroup(Integer groupId)
	{
		String sql = "UPDATE USER U LEFT JOIN `GROUP` G ON A.GROUP_ID = G.ID SET A.GROUP_ID = NULL "
				+ " WHERE G.ID = :groupId "
				+ " AND U.ROLE = (CASE G.TYPE WHEN :marketGroup THEN :marketingLeader ELSE :designLeader END )";
		MapSqlParameterSource params = new MapSqlParameterSource("marketingLeader", Role.marketingLeader.value())
				.addValue("designLeader", Role.designLeader.value())
				.addValue("marketGroup", Type.marketingGroup.value()).addValue("groupId", groupId);
		return jdbcOps.update(sql.toString(), params);
	}
}