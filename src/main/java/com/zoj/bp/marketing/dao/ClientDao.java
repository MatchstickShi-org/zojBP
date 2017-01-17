package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.Client;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class ClientDao extends BaseDao implements IClientDao
{
	@Override
	public Client getClientByName(String name)
	{
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM CLIENT WHERE NAME = :name",
					new MapSqlParameterSource("name", name), BeanPropertyRowMapper.newInstance(Client.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public Client getClientById(Integer id) {
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM CLIENT WHERE ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(Client.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}
	
	@Override
	public Client getClientByOrderId(Integer orderId) {
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM CLIENT WHERE ORDER_ID = :orderId",
					new MapSqlParameterSource("orderId", orderId), BeanPropertyRowMapper.newInstance(Client.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateClient(Client client) {
		String sql = "UPDATE CLIENT SET ORDER_ID = :orderId, NAME = :name,ORG_ADDR = :orgAddr,"
				+ " TEL1 = :tel1, TEL2 = :tel2, TEL3 = :tel3, TEL4 = :tel4, TEL5 = :tel5";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(client));
	}

	@Override
	public DatagridVo<Client> getAllClient(Pagination pagination,User loginUser) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM CLIENT WHERE ORDER_ID IN (SELECT ID FROM ORDER WHERE SALESMAN_ID = "+loginUser.getId()+")";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Client.class)), count);
	}
	
	@Override
	public Integer addClient(Client client)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO CLIENT(ORDER_ID, NAME, ORG_ADDR, TEL1, TEL2, TEL3, TEL4, TEL5, IS_KEY) VALUES(:orderId, :name, :orgAddr, :tel1, :tel2, :tel3, :tel4, :tel5, :isKey)",
				new BeanPropertySqlParameterSource(client), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<Order> getClientByTels(String... tels)
	{
		try
		{
			String sql ="SELECT O.*,U.ALIAS AS salesmanName FROM CLIENT C "+
					"LEFT JOIN `order` O ON O.ID = C.ORDER_ID "+
					"LEFT JOIN `user` U ON U.ID = O.SALESMAN_ID "+
					"WHERE 1=1 ";
			if(ArrayUtils.isNotEmpty(tels))
			{
				sql += "AND ( tel1 IN(" + StringUtils.join(tels, ',') + ") ";
				sql += "OR tel2 IN(" + StringUtils.join(tels, ',') + ") ";
				sql += "OR tel3 IN(" + StringUtils.join(tels, ',') + ") ";
				sql += "OR tel4 IN(" + StringUtils.join(tels, ',') + ") ";
				sql += "OR tel5 IN(" + StringUtils.join(tels, ',') + "))";
			}
			return jdbcOps.query(sql, EmptySqlParameterSource.INSTANCE, BeanPropertyRowMapper.newInstance(Order.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public Integer deleteBySalesmans(Integer... userIds)
	{
		return jdbcOps.update("DELETE C FROM CLIENT C LEFT JOIN `ORDER` O ON C.ORDER_ID = O.ID "
				+ " WHERE O.SALESMAN_ID IN (" + StringUtils.join(userIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer deleteByDesigners(Integer... userIds)
	{
		return jdbcOps.update("DELETE C FROM CLIENT C LEFT JOIN `ORDER` O ON C.ORDER_ID = O.ID"
				+ " WHERE O.DESIGNER_ID IN (" + StringUtils.join(userIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
}