package com.zoj.bp.design.dao;

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
import com.zoj.bp.common.model.DesignCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class DesignCountDao extends BaseDao implements IDesignCountDao{

	@Override
	public DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, COUNT(DISTINCT O3.ID) DEAD_ORDER_COUNT, U2.TODAY_DEAL_AMOUNT, U3.MONTH_DEAL_AMOUNT, U4.TOTAL_DEAL_AMOUNT "+ 
		"FROM "+
		"( "+
			"SELECT "+
				"U.ID,U.ID DESIGNER_ID, U.ALIAS DESIGNER_NAME, "+
				 "COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT, "+
				 "COUNT(DISTINCT O.ID) TALKING_ORDER_COUNT, "+
				 "COUNT(DISTINCT O2.ID) DEAL_ORDER_COUNT "+
			"FROM `USER` U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 34 "+
			"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND OV.VISITOR_ID = U.ID AND OV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
			"LEFT JOIN `ORDER` O2 ON U.ID = O2.DESIGNER_ID AND O2.`STATUS` = 90 "+
			"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 "+
			"GROUP BY U.ID "+
		") U "+
		"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 0 "+
		"LEFT JOIN "+
		"( "+
			"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TODAY_DEAL_AMOUNT FROM USER U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
				"AND (O.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59')) "+
			"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 "+
			"GROUP BY U.ID "+
		") U2 ON U.ID = U2.ID "+
		"LEFT JOIN "+
		"( "+
			"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) MONTH_DEAL_AMOUNT FROM USER U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
				"AND (O.INSERT_TIME BETWEEN CONCAT(DATE_FORMAT(CURRENT_DATE, '%Y-%m'), '-01 00:00:00') AND CONCAT(LAST_DAY(CURRENT_DATE),' 23:59:59')) "+
			"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 "+
			"GROUP BY U.ID "+
		") U3 ON U.ID = U3.ID "+
		"LEFT JOIN "+
		"( "+
			"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TOTAL_DEAL_AMOUNT FROM USER U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
			"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 "+
			"GROUP BY U.ID "+
		") U4 ON U.ID = U4.ID "+
		"WHERE 1=1 ";
		if(StringUtils.isNotEmpty(designerName)){
			sql +="AND U.DESIGNER_NAME LIKE :designerName ";
			paramMap.put("designerName", '%' + designerName + '%');
		}
		sql+="GROUP BY U.ID ";
		sql+="ORDER BY U.DESIGNER_NAME";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DesignCount.class)), count);
	}
	@Override
	public DatagridVo<DesignCount> getDesignerCountByDate(Pagination pagination,String designerName,String startDate,String endDate) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT "+
			"U.ID, "+
			"U.ALIAS DESIGNER_NAME, "+
			"SUM(DC.TALKING_VISIT_AMOUNT) TODAY_ORDER_VISIT_COUNT, "+
			"SUM(DC.TALKING_AMOUNT) TALKING_ORDER_COUNT, "+
			"SUM(DC.DEAL_TOTAL) DEAL_ORDER_COUNT, "+
			"SUM(DC.DEAD_TOTAL) DEAD_ORDER_COUNT, "+
			"SUM(DC.TODAY_DEAL_AMOUNT) TOTAL_DEAL_AMOUNT "+
		"FROM "+
			"USER U "+
		"LEFT JOIN DESIGN_COUNT DC ON U.ID = DC.DESIGNER_ID "+
		"AND DC.COUNT_DATE BETWEEN CONCAT(:startDate, ' 00:00:00') "+
		"AND CONCAT(:endDate, ' 23:59:59') "+
		"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 ";
		if(StringUtils.isNotEmpty(designerName)){
			sql +="AND U.ALIAS LIKE :designerName ";
			paramMap.put("designerName", '%' + designerName + '%');
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
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DesignCount.class)), count);
	}

	@Override
	public DesignCount getTodayDesignCountByUserId(Integer userId,String countDate) {
		String sql = "SELECT U.*, COUNT(DISTINCT O3.ID) DEAD_ORDER_COUNT, IFNULL(U2.TODAY_DEAL_AMOUNT, 0.00) TODAY_DEAL_AMOUNT "+
		"FROM "+
		"( "+
			"SELECT "+
				"U.ID,U.ID DESIGNER_ID, U.ALIAS DESIGNER_NAME "+
				 ", COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT "+
				 ", COUNT(DISTINCT O3.ID) TALKING_ORDER_COUNT "+
				 ", COUNT(DISTINCT O2.ID) DEAL_ORDER_COUNT "+
			"FROM `USER` U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 34 "+
			"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND (OV.DATE BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')) "+
			"LEFT JOIN `ORDER` O2 ON U.ID = O2.DESIGNER_ID AND O2.`STATUS` = 90 AND (O2.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')) "+
			"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 34 AND (O3.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')) "+
			"GROUP BY U.ID "+
		") U "+
		"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 0 AND (O3.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')) "+
		"LEFT JOIN "+
		"( "+
			"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TODAY_DEAL_AMOUNT FROM USER U "+
			"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
				"AND (O.UPDATE_TIME BETWEEN CONCAT(:countDate,' 00:00:00') AND CONCAT(:countDate,' 23:59:59')) "+ 
		") U2 ON U.ID = U2.ID "+
		"WHERE U.ID =:userId ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("countDate", countDate);
		return jdbcOps.queryForObject(sql,paramMap, BeanPropertyRowMapper.newInstance(DesignCount.class));
	}

	@Override
	public Integer addDesignCount(DesignCount designCount) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO DESIGN_COUNT("
				+ "UPDATE_TIME,"
				+ "COUNT_DATE,"
				+ "TALKING_VISIT_AMOUNT,"
				+ "TALKING_AMOUNT,"
				+ "DEAL_TOTAL,"
				+ "DEAD_TOTAL,"
				+ "TODAY_DEAL_AMOUNT,"
				+ "DESIGNER_ID"
				+ ") "
				+ "VALUES("
				+ "now(),"
				+ ":countDate,"
				+ ":todayOrderVisitCount,"
				+ ":talkingOrderCount,"
				+ ":dealOrderCount,"
				+ ":deadOrderCount,"
				+ ":todayDealAmount,"
				+ ":designerId)",
				new BeanPropertySqlParameterSource(designCount), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<DesignCount> getLastDesignCountByUsetrId(Integer userId) {
		return jdbcOps.query("SELECT * FROM design_count "
				+ " WHERE DESIGNER_ID = :userId ORDER BY ID DESC LIMIT 0,1 ",
		new MapSqlParameterSource("userId", userId), BeanPropertyRowMapper.newInstance(DesignCount.class));
	}
}
