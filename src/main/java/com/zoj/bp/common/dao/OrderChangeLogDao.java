package com.zoj.bp.common.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.model.OrderChangeLog;

@Repository
public class OrderChangeLogDao extends BaseDao implements IOrderChangeLogDao
{
	@Override
	public List<OrderChangeLog> getOrderChangeLogByOrderId(Integer orderId) {
		return jdbcOps.query("SELECT * FROM ORDER_CHANGE_LOG WHERE ORDER_ID = :orderId", new MapSqlParameterSource("orderId", orderId), BeanPropertyRowMapper.newInstance(OrderChangeLog.class));
	}
	
	@Override
	public Integer addOrderChangeLog(OrderChangeLog orderChangeLog) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO ORDER_CHANGE_LOG(ORDER_ID,STATUS) VALUES(:orderId,:status)",
				new BeanPropertySqlParameterSource(orderChangeLog), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteBySalesmans(Integer... salesmanIds)
	{
		return jdbcOps.update("DELETE A FROM ORDER_CHANGE_LOG A LEFT JOIN `ORDER` O ON A.ORDER_ID = O.ID"
				+ " WHERE O.SALESMAN_ID IN (" + StringUtils.join(salesmanIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer deleteByDesigners(Integer... designerIds)
	{
		return jdbcOps.update("DELETE A FROM ORDER_CHANGE_LOG A LEFT JOIN `ORDER` O ON A.ORDER_ID = O.ID"
				+ " WHERE O.DESIGNER_ID IN (" + StringUtils.join(designerIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
}
