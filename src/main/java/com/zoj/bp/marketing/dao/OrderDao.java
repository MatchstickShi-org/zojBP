package com.zoj.bp.marketing.dao;

import java.util.HashMap;
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
import com.zoj.bp.common.model.Order;
import com.zoj.bp.common.model.Order.Status;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class OrderDao extends BaseDao implements IOrderDao
{
	@Override
	public Order getOrderById(Integer id) {
		try
		{
			return jdbcOps.queryForObject(
				"SELECT O.*, C.`NAME`, C.ORG_ADDR, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.TEL5, I.`NAME` AS infoerName, "
				+ " 	U.ALIAS as salesmanName,U2.ALIAS AS designerName "+
				" FROM `ORDER` O "+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "+
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID "+
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID "+
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID "+
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
		String sql = "UPDATE `ORDER` SET STATUS = :status ";
		if(order.getDesignerId() != null && order.getDesignerId() > 0)
			sql +=",DESIGNER_ID = :designerId ";
		sql += "WHERE ID = :id";
		return jdbcOps.update(sql, new BeanPropertySqlParameterSource(order));
		
	}

	@Override
	public DatagridVo<Order> getOrdersByInfoer(Pagination pagination, Integer infoerId,Integer[] status)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*,C.`NAME`,C.ORG_ADDR, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.TEL5, C.IS_KEY, I.`NAME` AS infoerName,U.ALIAS as salesmanName,U2.ALIAS AS designerName "+ 
				" FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID"+
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID"+
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID"+
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID"+
				" WHERE 1 = 1 ";
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
	public DatagridVo<Order> getOrdersByUser(Pagination pagination, User user,
			String clientName, String tel, String infoerName, Integer salesmanOrDesignerId, Integer isKey, Integer... statuses)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*, "+ 
				" CASE O.ID WHEN NULL THEN NULL "
				+ " ELSE " 
				+ " 	CASE WHEN O.STATUS IN (0, 64, 90) THEN -100 "
				+ " 	ELSE "+ 
				" 			CASE WHEN MAX(OV.DATE) IS NULL THEN DATEDIFF(NOW(),O.INSERT_TIME) "+ 
				"			ELSE DATEDIFF(NOW(),MAX(OV.DATE)) "
				+ " 		END "
				+ " 	END "+ 
				" END notVisitDays, "
				+ " A.STATUS VISIT_APPLY_STATUS, "+ 
				" 	C.NAME, C.ORG_ADDR, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.TEL5, C.IS_KEY, I.`NAME` infoerName, U.ALIAS salesmanName, "+ 
				" 	U.STATUS salesmanStatus, U2.ALIAS AS designerName, U2.STATUS designerStatus "+
				" FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID " +
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID " +
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID " +
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID "
				+ " LEFT JOIN DESIGNER_VISIT_APPLY A ON A.ORDER_ID = O.ID AND TO_DAYS(A.CREATE_TIME) = TO_DAYS(CURRENT_DATE) ";
		if(user.isBelongMarketing())
		{
			sql +=" LEFT JOIN ORDER_VISIT OV ON O.ID = OV.ORDER_ID AND O.SALESMAN_ID = OV.VISITOR_ID ";
			if(user.isMarketingSalesman())
				sql += " WHERE U.ID = :userId ";
			else if(user.isMarketingLeader())
				sql += " WHERE U.GROUP_ID = (SELECT U.GROUP_ID FROM USER U WHERE U.ID = :userId) ";
			else
				sql += " WHERE 1=1 ";
			if(salesmanOrDesignerId != null)
			{
				sql += " AND O.SALESMAN_ID = :salesmanId";
				paramMap.put("salesmanId", salesmanOrDesignerId);
			}
		}
		else if(user.isBelongDesign())
		{
			sql +=" LEFT JOIN ORDER_VISIT OV ON O.ID = OV.ORDER_ID AND O.DESIGNER_ID = OV.VISITOR_ID ";
			if(user.isDesignDesigner())
				sql += " WHERE U2.ID = :userId ";
			else if(user.isDesignLeader())
				sql += " WHERE U2.GROUP_ID = (SELECT U.GROUP_ID FROM USER U WHERE U.ID = :userId) ";
			else
				sql += " WHERE 1=1 ";
			if(salesmanOrDesignerId != null)
			{
				sql += " AND O.DESIGNER_ID = :designerId";
				paramMap.put("designerId", salesmanOrDesignerId);
			}
		}
		else
			sql += " WHERE 1=1 ";
		
		paramMap.put("userId", user.getId());
		if(StringUtils.isNotEmpty(clientName))
		{
			sql += " AND C.NAME LIKE :name";
			paramMap.put("name", '%' + clientName + '%');
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
		if(isKey != null)
		{
			sql += " AND C.IS_KEY = :isKey";
			paramMap.put("isKey", isKey);
		}
		if(ArrayUtils.isNotEmpty(statuses))
			sql +=" AND O.`STATUS` IN(" + StringUtils.join(statuses, ',') + ")";
		sql +=" GROUP BY O.ID, C.ID, I.ID, U.ID, U2.ID, A.ID ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY C.IS_KEY DESC, ";
		sql += pagination.buildOrderBySqlPart(" notVisitDays DESC ");
		sql +=" , O.INSERT_TIME DESC ";
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Order.class)), count);
	}

	@Override
	public DatagridVo<Order> getOrdersBySalesman(Pagination pagination,
			User salesman, String name, String tel, String infoerName, Integer salesmanId, Integer isKey, Integer... statuses)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*, "+ 
				" CASE O.ID WHEN NULL THEN NULL "
				+ " ELSE "
				+ " 	CASE WHEN O.STATUS IN (0, 64, 90) THEN -100 " +
				" 		ELSE "+ 
				" 			CASE WHEN MAX(OV.DATE) IS NULL THEN DATEDIFF(NOW(),O.INSERT_TIME) "+ 
				"			ELSE DATEDIFF(NOW(),MAX(OV.DATE))"
				+ " 		END "
				+ " 	END "
				+ " END AS notVisitDays, "+ 
				" C.NAME, C.ORG_ADDR, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.TEL5, C.IS_KEY, I.`NAME` infoerName, U.ALIAS salesmanName, "+ 
				" U.STATUS salesmanStatus, U2.ALIAS AS designerName, U2.STATUS designerStatus "+
				" FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID " +
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID " +
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID " +
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID " +
				" LEFT JOIN ORDER_VISIT OV ON O.ID = OV.ORDER_ID AND O.SALESMAN_ID = OV.VISITOR_ID "+
				" WHERE U.ID = :userId ";
		paramMap.put("userId", salesman.getId());
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
		if(salesmanId != null)
		{
			sql += " AND I.SALESMAN_ID = :salesmanId";
			paramMap.put("salesmanId", salesmanId);
		}
		if(isKey != null)
		{
			sql += " AND C.IS_KEY = :isKey";
			paramMap.put("isKey", isKey);
		}
		if(ArrayUtils.isNotEmpty(statuses))
			sql +=" AND O.`STATUS` IN(" + StringUtils.join(statuses, ',') + ")";
		sql +=" GROUP BY O.ID, C.ID, U.ID, U2.ID, I.ID ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY C.IS_KEY DESC, ";
		sql += pagination.buildOrderBySqlPart(" notVisitDays DESC ");
		sql +=" , O.INSERT_TIME DESC";
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Order.class)), count);
	}
	
	@Override
	public DatagridVo<Order> getOrdersByDesigner(Pagination pagination,
			User designer, String name, String tel, String infoerName, Integer designerId, Integer isKey, Integer... statuses)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT O.*, "+ 
				" CASE O.ID WHEN NULL THEN NULL "+ 
				" ELSE " 
				+ " 	CASE WHEN O.STATUS IN (0, 64, 90) THEN -100 "
				+ " 	ELSE "
				+ " 		CASE WHEN MAX(OV.DATE) IS NULL THEN DATEDIFF(NOW(),O.INSERT_TIME) " 
				+ "			ELSE DATEDIFF(NOW(),MAX(OV.DATE)) "
				+ " 		END "
				+ " 	END "+ 
				" END notVisitDays, "
				+ " A.STATUS VISIT_APPLY_STATUS, "+ 
				" 	C.NAME, C.ORG_ADDR, C.TEL1, C.TEL2, C.TEL3, C.TEL4, C.TEL5, C.IS_KEY, I.NAME INFOER_NAME, I.`NAME` infoerName, U.ID salesmanId, U.ALIAS salesmanName, "+ 
				" 	U.STATUS salesmanStatus, U2.ID designerId, U2.ALIAS designerName, U2.STATUS designerStatus "+
				" FROM `ORDER` O"+
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID " +
				" LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID " +
				" LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID " +
				" LEFT JOIN INFOER I ON I.ID = O.INFOER_ID " +
				" LEFT JOIN ORDER_VISIT OV ON O.ID = OV.ORDER_ID AND O.SALESMAN_ID = OV.VISITOR_ID "
				+ " LEFT JOIN DESIGNER_VISIT_APPLY A ON A.ORDER_ID = O.ID AND TO_DAYS(A.CREATE_TIME) = TO_DAYS(CURRENT_DATE) "+
				" WHERE U2.ID = :userId ";
		paramMap.put("userId", designer.getId());
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
			sql += " AND I.NAME like :infoerName ";
			paramMap.put("infoerName", '%' + infoerName + '%');
		}
		if(designerId != null)
		{
			sql += " AND O.DESIGNER_ID = :designerId ";
			paramMap.put("designerId", designerId);
		}
		if(isKey != null)
		{
			sql += " AND C.IS_KEY = :isKey";
			paramMap.put("isKey", isKey);
		}
		if(ArrayUtils.isNotEmpty(statuses))
			sql +=" AND O.`STATUS` IN(" + StringUtils.join(statuses, ',') + ")";
		sql +=" GROUP BY O.ID, C.ID, I.ID, U.ID, U2.ID, I.ID, A.ID ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY C.IS_KEY DESC, ";
		sql += pagination.buildOrderBySqlPart(" notVisitDays DESC ");
		sql +=" , O.INSERT_TIME DESC";
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Order.class)), count);
	}

	@Override
	public DatagridVo<Order> getOrdersByStatus(
			User loginUser, String clientName, Integer orderId, Pagination pagination, Status... status) throws Exception
	{
		if(ArrayUtils.isEmpty(status))
			throw new Exception("找不到要查找的客户，请检查请求参数。");
		MapSqlParameterSource params = new MapSqlParameterSource("start", pagination.getStartRow()).addValue("rows", pagination.getRows());
		String sql = "SELECT O.*, C.NAME NAME, C.ORG_ADDR ORG_ADDR, C.TEL1 TEL1, C.TEL2 TEL2, C.TEL3 TEL3, "
				+ " 	C.TEL4 TEL4, C.TEL5 TEL5, I.ID INFOER_NAME, I.`NAME` infoerName, "
				+ " 	U.ID salesmanId, U.ALIAS salesmanName, U2.ID designerId, U2.ALIAS designerName "
				+ " FROM `ORDER` O "
				+ " LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "
				+ " LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID "
				+ " LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID "
				+ " LEFT JOIN INFOER I ON I.ID = O.INFOER_ID ";
		if(loginUser.isMarketingSalesman())
			sql += " WHERE O.SALESMAN_ID = :userId ";
		else if(loginUser.isMarketingLeader())
		{
			sql += " WHERE O.SALESMAN_ID IN "
				+ " ("
				+ "		SELECT G.ID FROM `GROUP` G LEFT JOIN USER U ON U.GROUP_ID = G.ID "
				+ "		WHERE U.ID = :userId "
				+ "	)";
		}
		else
			sql += " WHERE 1=1 ";
		
		if(StringUtils.isNotEmpty(clientName))
		{
			sql += " AND C.NAME LIKE :clientName ";
			params.addValue("clientName", "%" + clientName + "%");
		}
		if(orderId != null)
		{
			sql += " AND O.ID LIKE :orderId";
			params.addValue("orderId", "%" + orderId + "%");
		}
		sql += " AND O.STATUS IN (" + StringUtils.join(status, ',') + ") ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY O.INSERT_TIME LIMIT :start, :rows";
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, 
				params, 
				BeanPropertyRowMapper.newInstance(Order.class)), count);
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
	public Integer updateOrderStatus(Status status, Integer... orderIds)
	{
		return jdbcOps.update("UPDATE `ORDER` SET STATUS = :status "
				+ " WHERE ID IN(" + StringUtils.join(orderIds, ',') + ")", new MapSqlParameterSource("status", status.value()));
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

	@Override
	public Integer deleteBySalesmans(Integer... salesmanIds)
	{
		return jdbcOps.update("DELETE FROM `ORDER` WHERE SALESMAN_ID IN (" + StringUtils.join(salesmanIds, ',') + ")", 
				EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer deleteByDesigners(Integer... designerIds)
	{
		return jdbcOps.update("DELETE FROM `ORDER` WHERE DESIGNER_ID IN (" + StringUtils.join(designerIds, ',') + ")", 
				EmptySqlParameterSource.INSTANCE);
	}
}