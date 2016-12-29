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
			return jdbcOps.queryForObject("SELECT O.*,C.`NAME`,C.ORG_ADDR,C.TEL1,C.TEL2,C.TEL3,C.TEL4,C.TEL5,I.`NAME` AS infoerName,U.ALIAS as salesmanName,U2.ALIAS AS designerName FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID"+
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID"+
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID"+
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID"+
				" WHERE O.ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(Order.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateOrder(Order order) {
		String sql = "UPDATE `ORDER` SET INFOER_ID = :infoerId, SALESMAN_ID = :salesmanId, DESIGNER_ID = :designerId, PROJECT_NAME = :projectName,"
				+ " PROJECT_ADDR = :projectAddr, STATUS = :status";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(order));

	}
	
	@Override
	public Integer updateOrderStatus(Order order) {
		String sql = "UPDATE `ORDER` SET STATUS = :status";
		sql += " WHERE ID = :id";
		return jdbcOps.update(sql, new BeanPropertySqlParameterSource(order));
		
	}

	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser,Integer infoerId,Integer[] status) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*,C.`NAME`,C.ORG_ADDR,C.TEL1,C.TEL2,C.TEL3,C.TEL4,C.TEL5,I.`NAME` AS infoerName,U.ALIAS as salesmanName,U2.ALIAS AS designerName FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID"+
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID"+
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID"+
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID"+
				" WHERE 1 = 1 ";
		if(loginUser != null)
			sql +=" AND O.SALESMAN_ID="+loginUser.getId();
		if(infoerId != null && infoerId > 0)
			sql +=" AND O.INFOER_ID="+infoerId;
		if(status != null && status.length > 0)
			sql +=" AND O.`STATUS` IN(" + StringUtils.join(status, ',') + ")";
		sql +=" ORDER BY O.INSERT_TIME DESC";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Order.class)), count);
	}
	
	@Override
	public DatagridVo<Order> getAllOrder(Pagination pagination, User loginUser, String name, String tel,
			String infoerName, String[] status) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*,C.`NAME`,C.ORG_ADDR,C.TEL1,C.TEL2,C.TEL3,C.TEL4,C.TEL5,I.`NAME` AS infoerName,U.ALIAS as salesmanName,U2.ALIAS AS designerName FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID"+
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID"+
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID"+
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID"+
				" WHERE 1 = 1 ";
		if(loginUser != null)
			sql +=" AND O.SALESMAN_ID="+loginUser.getId();
		if(StringUtils.isNotEmpty(name))
		{
			sql += " AND C.NAME LIKE :name";
			paramMap.put("name", '%' + name + '%');
		}
		if (StringUtils.isNotEmpty(tel))
		{
			sql += " AND (C.TEL1 LIKE :tel1 OR C.TEL2 LIKE :tel2 OR C.TEL3 LIKE :tel3 OR C.TEL4 LIKE :tel4 OR C.TEL5 LIKE :tel5)";
			paramMap.put("tel1", '%' + tel + '%');
			paramMap.put("tel2", '%' + tel + '%');
			paramMap.put("tel3", '%' + tel + '%');
			paramMap.put("tel4", '%' + tel + '%');
			paramMap.put("tel5", '%' + tel + '%');
		}
		if(StringUtils.isNotEmpty(infoerName))
		{
			sql += " AND I.NAME like :infoerName";
			paramMap.put("infoerName", '%' + infoerName + '%');
		}
		if(status != null)
			sql +=" AND O.`STATUS` IN(" + StringUtils.join(status, ',') + ")";
		sql +=" ORDER BY O.INSERT_TIME DESC";
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
				"INSERT INTO `ORDER`(INFOER_ID,SALESMAN_ID,DESIGNER_ID,PROJECT_NAME,PROJECT_ADDR,INSERT_TIME,STATUS) "
				+ "VALUES(:infoerId,:salesmanId,:designerId,:projectName,:projectAddr,now(),:status)",
				new BeanPropertySqlParameterSource(order), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer updateOrderByIds(Integer[] orderIds) {
		return jdbcOps.update("UPDATE `ORDER` SET STATUS = 12 "
				+ " WHERE ID IN(" + StringUtils.join(orderIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer updateOrderSalesmanId(Integer[] orderIds, Integer salesmanId) {
		return jdbcOps.update("UPDATE `ORDER` SET SALESMAN_ID = :salesmanId "
				+ " WHERE ID IN(" + StringUtils.join(orderIds, ',') + ")", new MapSqlParameterSource("salesmanId",salesmanId));
	}
	@Override
	public Integer updateOrderDesigerId(Integer[] orderIds, Integer designerId) {
		return jdbcOps.update("UPDATE `ORDER` SET DESIGNER_ID = :designerId "
				+ " WHERE ID IN(" + StringUtils.join(orderIds, ',') + ")", new MapSqlParameterSource("designerId",designerId));
	}
	@Override
	public Integer updateOrderSalesmanIdByInfoers(Integer[] infoerIds, Integer salesmanId) {
		return jdbcOps.update("UPDATE `ORDER` SET SALESMAN_ID = :salesmanId "
				+ " WHERE INFOER_ID IN(" + StringUtils.join(infoerIds, ',') + ")", new MapSqlParameterSource("salesmanId",salesmanId));
	}

}
