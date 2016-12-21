package com.zoj.bp.sysmgr.groupmgr.dao;

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
import com.zoj.bp.common.model.Group;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class GroupDao extends BaseDao implements IGroupDao
{
	@Override
	public DatagridVo<Group> getAllGroups(Pagination pagination, Integer type)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT G.*, U.ALIAS LEADER_NAME FROM `GROUP` G "
				+ " LEFT JOIN USER U ON G.LEADER_ID = U.ID "
				+ " WHERE 1=1 ";
		if(type != null)
		{
			sql += " AND G.TYPE = :type";
			paramMap.put("type", type);
		}
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Group.class)), count);
	}
	
	@Override
	public Integer addGroup(String name, boolean isMarketingGroup, Integer leaderId)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update("INSERT INTO `GROUP`(NAME, TYPE, LEADER_ID) VALUES(:name, :type, :leaderId)",
				new MapSqlParameterSource("name", name)
					.addValue("type", isMarketingGroup ? 0 : 1).addValue("leaderId", leaderId), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Group getGroupById(Integer groupId)
	{
		try
		{
			return jdbcOps.queryForObject(
					"SELECT G.*, U.ALIAS LEADER_NAME FROM `GROUP` G LEFT JOIN USER U ON G.LEADER_ID = U.ID WHERE ID = :id",
					new MapSqlParameterSource("id", groupId), BeanPropertyRowMapper.newInstance(Group.class));
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
		String sql = "UPDATE `GROUP` SET NAME = :name, TYPE = :type, LEADER_ID = :leaderId WHERE ID = :id";
		return jdbcOps.update(sql, new BeanPropertySqlParameterSource(group));
	}

	@Override
	public Integer deleteGroupByIds(Integer[] grpIds)
	{
		return jdbcOps.update("DELETE FROM `GROUP` "
				+ " WHERE ID IN(" + StringUtils.join(grpIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}

	@Override
	public DatagridVo<Group> getAssignedUnderling(Integer groupId, Pagination pagination)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT G.*, U.ALIAS LEADER_NAME FROM `GROUP` G "
				+ " LEFT JOIN USER U ON G.LEADER_ID = U.ID "
				+ " WHERE U.GROUP_ID = :groupId";
		paramMap.put("groupId", groupId);
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Group.class)), count);
	}

	@Override
	public DatagridVo<Group> getNotAssignUnderling(Integer groupId, Pagination pagination)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT G.*, U.ALIAS LEADER_NAME FROM `GROUP` G "
				+ " LEFT JOIN USER U ON G.LEADER_ID = U.ID "
				+ " WHERE U.GROUP_ID <> :groupId OR U.GROUP_ID IS NULL ";
		paramMap.put("groupId", groupId);
		
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Group.class)), count);
	}

	@Override
	public Integer addUnderlingToGroup(Integer groupId, Integer[] underlingIds)
	{
		StringBuffer sql = new StringBuffer(
				" UPDATE USER SET GROUP_ID = :groupId WHERE ID IN (" + StringUtils.join(underlingIds, ',') + ")");
		return jdbcOps.update(sql.toString(), new MapSqlParameterSource("groupId", groupId));
	}

	@Override
	public Integer removeUnderlingFromGroup(Integer groupId, Integer[] underlingIds)
	{
		return null;
	}
}