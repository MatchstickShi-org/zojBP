package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class OrderDao extends BaseDao implements IOrderDao {

	@Override
	public Order getOrderById(Integer id) {
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM ORDER WHERE ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(Order.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateOrder(Order order) {
		String sql = "UPDATE ORDER SET INFOER_ID = :infoerId, SALESMAN_ID = :salesmanId, STYLIST_ID = :stylistId, PROJECT_NAME = :projectName,"
				+ " PROJECT_ADDR = :projectAddr, STATUS = :status";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(order));

	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser, String name, String tel,
			String infoerName, String status) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM ORDER WHERE SALESMAN_ID="+loginUser.getId();
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Order.class)), count);
	}

	@Override
	public Integer addOrder(Order order) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO ORDER(INFOER_ID,SALESMAN_ID,STYLIST_ID,PROJECT_NAME,PROJECT_ADDR,INSERT_TIME,STATUS) "
				+ "VALUES(:infoerId,:salesmanId,:stylistId,:projectName,:projectAddr,now(),:status)",
				new BeanPropertySqlParameterSource(order), keyHolder);
		return keyHolder.getKey().intValue();
	}

}
