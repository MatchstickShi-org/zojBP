package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class MarkertingCountDao extends BaseDao implements IMarketingCountDao
{

	@Override
	public DatagridVo<MarketingCount> getTodayMarketingCount(Pagination pagination,String salesmanName) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, "+
			"COUNT(DISTINCT O2.ID) TODAY_CLIENT_ADD_COUNT, COUNT(DISTINCT O3.ID) CONTACTING_CLIENT_COUNT, "+
			"COUNT(DISTINCT O4.ID) DEAL_ORDER_COUNT, COUNT(DISTINCT O5.ID) MONTH_TALKING_ORDER_COUNT "+
		"FROM "+
		"( "+
			"SELECT U.ID,U.ID SALESMAN_ID,U.ALIAS SALESMAN_NAME,LU.ID LEADER_ID,"+
				"COUNT(DISTINCT IV.ID) TODAY_INFOER_VISIT_COUNT, COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT, "+
				"COUNT(DISTINCT OV1.ID) TODAY_CLIENT_VISIT_COUNT,COUNT(DISTINCT O.ID) TALKING_ORDER_COUNT, "+
				"COUNT(DISTINCT I.ID) TODAY_INFOER_ADD_COUNT, COUNT(DISTINCT I2.ID) TRACING_INFOER_COUNT "+
			"FROM USER U "+
			"LEFT JOIN INFOER_VISIT IV ON U.ID = IV.SALESMAN_ID AND IV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+ 
			"LEFT JOIN `ORDER` O ON U.ID = O.SALESMAN_ID AND O.`STATUS` = 34 "+
			"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_STATUS =34 AND OV.VISITOR_ID = U.ID AND OV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+ 
			"LEFT JOIN ORDER_VISIT OV1 ON OV1.ORDER_STATUS IN(10,12,14,20,22,30,32) AND OV1.VISITOR_ID = U.ID AND OV1.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59')  "+ 
			"LEFT JOIN INFOER I ON I.SALESMAN_ID = U.ID AND I.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+ 
			"LEFT JOIN INFOER I2 ON I2.SALESMAN_ID = U.ID "+
			"LEFT JOIN `GROUP` G ON G.ID = U.GROUP_ID "+
			"LEFT JOIN USER LU ON LU.GROUP_ID = G.ID AND LU.ROLE = 2 AND LU.ID != U.ID "+
			"WHERE (U.ROLE = 1 OR U.ROLE = 2 OR U.ROLE = 3) AND U.`STATUS` = 1 "+
			"GROUP BY U.ID "+
		") U "+
		"LEFT JOIN `ORDER` O2 ON U.ID = O2.SALESMAN_ID AND O2.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
		"LEFT JOIN `ORDER` O3 ON U.ID = O3.SALESMAN_ID AND O3.`STATUS` IN(10,30,32) "+
		"LEFT JOIN `ORDER` O4 ON U.ID = O4.SALESMAN_ID AND O4.`STATUS` = 90 "+
		"LEFT JOIN `ORDER` O5 ON U.ID = O5.SALESMAN_ID AND O5.`STATUS` IN(20,22,34,90,0,60,62,64) AND O5.INSERT_TIME BETWEEN CONCAT(DATE_FORMAT(CURRENT_DATE,'%Y-%m'),'-01 00:00:00') AND CONCAT(LAST_DAY(CURRENT_DATE),' 23:59:59') "+
		"WHERE 1 = 1 ";
		if(StringUtils.isNotEmpty(salesmanName)){
			sql +="AND U.SALESMAN_NAME LIKE :salesmanName ";
			paramMap.put("salesmanName", '%' + salesmanName + '%');
		}
		sql+="GROUP BY U.ID ";
		sql+="ORDER BY U.SALESMAN_NAME ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(MarketingCount.class)), count);
	}
	
	@Override
	public DatagridVo<MarketingCount> getMarketingCountByDate(Pagination pagination,String salesmanName,String startDate,String endDate) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT "+
				"U.ID, "+
				"U.ID SALESMAN_ID, "+
				"U.ALIAS SALESMAN_NAME, "+
				"SUM(MC.INFOER_VISIT_AMOUNT) TODAY_INFOER_VISIT_COUNT, "+
				"SUM(MC.CLIENT_VISIT_AMOUNT) TODAY_CLIENT_VISIT_COUNT, "+
				"SUM(MC.TALKING_VISIT_AMOUNT) TODAY_ORDER_VISIT_COUNT, "+
				"SUM(MC.TALKING_AMOUNT) TALKING_ORDER_COUNT, "+
				"SUM(MC.INFOER_ADD_AMOUNT) TODAY_INFOER_ADD_COUNT, "+
				"SUM(MC.INFOER_ADD_AMOUNT) TRACING_INFOER_COUNT, "+
				"SUM(MC.CLIENT_ADD_AMOUNT) TODAY_CLIENT_ADD_COUNT, "+
				"SUM(MC.DEAL_TOTAL) DEAL_ORDER_COUNT, "+
				"SUM(MC.APPLY_TALKING_AMOUNT) MONTH_TALKING_ORDER_COUNT "+
			"FROM "+
				"USER U "+
			"LEFT JOIN MARKETING_COUNT MC ON U.ID = MC.SALESMAN_ID "+
			"AND MC.COUNT_DATE BETWEEN CONCAT(:startDate, ' 00:00:00') "+
			"AND CONCAT(:endDate, ' 23:59:59') "+
			"WHERE (U.ROLE = 1 OR U.ROLE = 2 OR U.ROLE = 3) AND U.`STATUS` = 1 ";
			if(StringUtils.isNotEmpty(salesmanName)){
			sql +="AND U.ALIAS LIKE :salesmanName ";
			paramMap.put("salesmanName", '%' + salesmanName + '%');
		}
		paramMap.put("startDate",startDate);
		paramMap.put("endDate",endDate);
		sql+="GROUP BY U.ID ";
		sql+="ORDER BY U.ALIAS ";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(MarketingCount.class)), count);
	}

	@Override
	public MarketingCount getTodayMarketingCountByUserId(Integer userId,String countDate) {
		String sql = "SELECT U.*, "+
			"COUNT(DISTINCT O2.ID) TODAY_CLIENT_ADD_COUNT, "+
			"COUNT(DISTINCT O3.ID) CONTACTING_CLIENT_COUNT, "+
			"COUNT(DISTINCT O4.ID) DEAL_ORDER_COUNT, "+
			"COUNT(DISTINCT OCL.ORDER_ID) APPLY_TALKING_ORDER_COUNT "+ 
		"FROM "+
		"( "+
			"SELECT U.ID,U.ID SALESMAN_ID, U.ALIAS SALESMAN_NAME, "+
				"COUNT(DISTINCT IV.ID) TODAY_INFOER_VISIT_COUNT, COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT, "+
				"COUNT(DISTINCT OV1.ID) TODAY_CLIENT_VISIT_COUNT, COUNT(DISTINCT O.ID) TALKING_ORDER_COUNT, "+
				"COUNT(DISTINCT I.ID) TODAY_INFOER_ADD_COUNT "+
			"FROM USER U "+
			"LEFT JOIN INFOER_VISIT IV ON U.ID = IV.SALESMAN_ID AND IV.DATE BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
			"LEFT JOIN `ORDER` O ON U.ID = O.SALESMAN_ID AND O.`STATUS` = 34 AND O.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
			"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_STATUS = 34 AND OV.VISITOR_ID = U.ID AND OV.DATE BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
			"LEFT JOIN ORDER_VISIT OV1 ON OV1.ORDER_STATUS IN(10,12,14,20,22,30,32) AND OV1.VISITOR_ID = U.ID AND OV1.DATE BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')  "+
			"LEFT JOIN INFOER I ON I.SALESMAN_ID = U.ID AND I.INSERT_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+  
			"WHERE U.ID = :userId "+
			"GROUP BY U.ID "+
		") U "+
		"LEFT JOIN `ORDER` O2 ON U.ID = O2.SALESMAN_ID AND O2.INSERT_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
		"LEFT JOIN `ORDER` O3 ON U.ID = O3.SALESMAN_ID AND O3.`STATUS` IN(10,30,32) AND O3.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
		"LEFT JOIN `ORDER` O4 ON U.ID = O4.SALESMAN_ID AND O4.`STATUS` = 90 AND O4.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') "+
		"LEFT JOIN `ORDER` O5 ON U.ID = O5.SALESMAN_ID "+
		"LEFT JOIN ORDER_CHANGE_LOG OCL ON OCL.ORDER_ID = O5.ID  AND OCL.`STATUS` =34 AND OCL.CHANGE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59') ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("countDate", countDate);
		return jdbcOps.queryForObject(sql,paramMap, BeanPropertyRowMapper.newInstance(MarketingCount.class));
	}
	
	@Override
	public Integer addMarketingCount(MarketingCount marketingCount) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO MARKETING_COUNT("
				+ "UPDATE_TIME,"
				+ "COUNT_DATE,"
				+ "INFOER_VISIT_AMOUNT,"
				+ "TALKING_VISIT_AMOUNT,"
				+ "CLIENT_VISIT_AMOUNT,"
				+ "INFOER_ADD_AMOUNT,"
				+ "CLIENT_ADD_AMOUNT,"
				+ "CONTACTING_CLIENT_TOTAL,"
				+ "TALKING_AMOUNT,"
				+ "DEAL_TOTAL,"
				+ "APPLY_TALKING_AMOUNT,"
				+ "SALESMAN_ID"
				+ ") "
				+ "VALUES("
				+ "now(),"
				+ ":countDate,"
				+ ":todayInfoerVisitCount,"
				+ ":todayOrderVisitCount,"
				+ ":todayClientVisitCount,"
				+ ":todayInfoerAddCount,"
				+ ":todayClientAddCount,"
				+ ":contactingClientCount,"
				+ ":talkingOrderCount,"
				+ ":dealOrderCount,"
				+ ":applyTalkingOrderCount,"
				+ ":salesmanId)",
				new BeanPropertySqlParameterSource(marketingCount), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<MarketingCount> getLastMarketingCountByUsetrId(Integer userId) {
		return jdbcOps.query("SELECT * FROM marketing_count "
				+ " WHERE SALESMAN_ID = :userId ORDER BY ID DESC LIMIT 0,1 ",
		new MapSqlParameterSource("userId", userId), BeanPropertyRowMapper.newInstance(MarketingCount.class));
	}
}
