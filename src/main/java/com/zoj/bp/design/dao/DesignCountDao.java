package com.zoj.bp.design.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
	public DatagridVo<DesignCount> getTodayDesignerCount(Pagination pagination,String designerName,String startDate,String endDate) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT U.*, U2.TODAY_DEAL_AMOUNT, U3.MONTH_DEAL_AMOUNT, U4.TOTAL_DEAL_AMOUNT "+
						"FROM "+
						"( "+
							"SELECT "+
								"U.ID, U.ALIAS DESIGNER_NAME, COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT, "+
								"COUNT(DISTINCT O.ID) TALKING_ORDER_COUNT, COUNT(DISTINCT O2.ID) DEAL_ORDER_COUNT, COUNT(DISTINCT O3.ID) DEAD_ORDER_COUNT "+
							"FROM `USER` U "+
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 34 "+
							"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND OV.DATE BETWEEN CONCAT(:startDate,' 00:00:00') AND CONCAT(:endDate,' 23:59:59') "+
							"LEFT JOIN `ORDER` O2 ON U.ID = O2.DESIGNER_ID AND O2.`STATUS` = 90 "+
							"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 0 "+
							"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) AND U.`STATUS` = 1 "+
							"GROUP BY U.ID "+
						") U "+
						"LEFT JOIN "+
						"( "+
							"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TODAY_DEAL_AMOUNT FROM USER U "+ 
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
								"AND (O.INSERT_TIME BETWEEN CONCAT(:startDate,' 00:00:00') AND CONCAT(:endDate,' 23:59:59')) "+ 
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
						") U4 ON U.ID = U4.ID ";
		if(StringUtils.isNotEmpty(designerName)){
			sql +="AND U.DESIGNER_NAME LIKE :designerName ";
			paramMap.put("designerName", '%' + designerName + '%');
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		paramMap.put("startDate", StringUtils.isEmpty(startDate) ? sdf.format(new Date()):startDate);
		paramMap.put("endDate", StringUtils.isEmpty(endDate) ? sdf.format(new Date()):endDate);
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
	public DesignCount getTodayDesignCountByUserId(Integer userId) {
		String sql = "SELECT U.*, U2.TODAY_DEAL_AMOUNT, U3.MONTH_DEAL_AMOUNT, U4.TOTAL_DEAL_AMOUNT "+
						"FROM "+
						"( "+
							"SELECT "+
								"U.ID,U.ID DESIGNER_ID, U.ALIAS DESIGNER_NAME, COUNT(DISTINCT OV.ID) TODAY_ORDER_VISIT_COUNT, "+
								"COUNT(DISTINCT O.ID) TALKING_ORDER_COUNT, COUNT(DISTINCT O2.ID) DEAL_ORDER_COUNT, COUNT(DISTINCT O3.ID) DEAD_ORDER_COUNT "+
							"FROM `USER` U "+
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 34 "+
							"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND OV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
							"LEFT JOIN `ORDER` O2 ON U.ID = O2.DESIGNER_ID AND O2.`STATUS` = 90 "+
							"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 0 "+
							"GROUP BY U.ID "+
						") U "+
						"LEFT JOIN "+
						"( "+
							"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TODAY_DEAL_AMOUNT FROM USER U "+
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
								"AND (O.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59')) "+
							"GROUP BY U.ID "+
						") U2 ON U.ID = U2.ID "+
						"LEFT JOIN "+
						"( "+
							"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) MONTH_DEAL_AMOUNT FROM USER U "+
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
								"AND (O.INSERT_TIME BETWEEN CONCAT(DATE_FORMAT(CURRENT_DATE, '%Y-%m'), '-01 00:00:00') AND CONCAT(LAST_DAY(CURRENT_DATE),' 23:59:59')) "+
							"GROUP BY U.ID "+
						") U3 ON U.ID = U3.ID "+
						"LEFT JOIN "+
						"( "+
							"SELECT U.ID, IFNULL(SUM(O.DEAL_AMOUNT), 0.00) TOTAL_DEAL_AMOUNT FROM USER U "+
							"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 90 "+
							"GROUP BY U.ID "+
						") U4 ON U.ID = U4.ID "+
				"WHERE U.ID =:userId ";
		return jdbcOps.queryForObject(sql,new MapSqlParameterSource("userId", userId), BeanPropertyRowMapper.newInstance(DesignCount.class));
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
				+ "MONTH_DEAL_AMOUNT,"
				+ "TOTAL_DEAL_AMOUNT,"
				+ "DESIGNER_ID"
				+ ") "
				+ "VALUES("
				+ "now(),"
				+ "date_sub(CURRENT_DATE,interval 1 day),"
				+ ":todayOrderVisitCount,"
				+ ":talkingOrderCount,"
				+ ":dealOrderCount,"
				+ ":deadOrderCount,"
				+ ":monthDealAmount,"
				+ ":totalDealAmount,"
				+ ":designerId)",
				new BeanPropertySqlParameterSource(designCount), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
