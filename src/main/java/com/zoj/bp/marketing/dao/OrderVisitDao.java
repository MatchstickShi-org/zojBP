package com.zoj.bp.marketing.dao;

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
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class OrderVisitDao extends BaseDao implements IOrderVisitDao
{
	@Override
	public OrderVisit getOrderVisitById(Integer id) {
		try
		{
			return jdbcOps.queryForObject("SELECT OV.* FROM ORDER_VISIT OV "+
				" WHERE OV.ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(OrderVisit.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateOrderVisit(OrderVisit orderVisit) {
		String sql = "UPDATE ORDER_VISIT SET COMMENT = :comment WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(orderVisit));

	}

	@Override
	public DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,Integer visitorId,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT OV.* "
				+ "FROM ORDER_VISIT OV "
				+ "WHERE OV.VISITOR_ID= :visitorId ";
		paramMap.put("visitorId", visitorId);
		if(orderId != null && orderId > 0)
		{
			sql += " AND ORDER_ID = :orderId";
			paramMap.put("orderId", orderId);
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY DATE DESC";
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(OrderVisit.class)), count);
	}
	
	@Override
	public Integer addOrderVisit(OrderVisit orderVisit) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO ORDER_VISIT(ORDER_ID,VISITOR_ID,DATE,CONTENT) VALUES(:orderId,:visitorId,now(),:content)",
				new BeanPropertySqlParameterSource(orderVisit), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteBySalesmanId(Integer... salesmanIds)
	{
		return jdbcOps.update("DELETE V FROM ORDER_VISIT V LEFT JOIN `ORDER` O ON V.VISITOR_ID = O.ID "
				+ " WHERE O.SALESMAN_ID IN (" + StringUtils.join(salesmanIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer deleteByDesignerId(Integer... designerIds)
	{
		return jdbcOps.update("DELETE V FROM ORDER_VISIT V LEFT JOIN `ORDER` O ON V.VISITOR_ID = O.ID"
				+ " WHERE O.DESIGNER_ID IN (" + StringUtils.join(designerIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
}