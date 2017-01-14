package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.MarketingCount;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class MarkertingCountDao extends BaseDao implements IMarketingCountDao
{

	@Override
	public DatagridVo<MarketingCount> getTodayMarketingCout(Pagination pagination,String salesmanName) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT "+
					"U.ID SALESMAN_ID, U.SALESMAN_NAME, "+
					"COUNT(DISTINCT U.IV_ID) TODAY_INFOER_VISIT_COUNT, "+
					"COUNT(DISTINCT U.OV_ID) TODAY_ORDER_VISIT_COUNT, "+
					"COUNT(DISTINCT U.I_ID) TODAY_INFOER_ADD_COUNT, "+ 
					"COUNT(DISTINCT U.O2_ID) TODAY_CLIENT_ADD_COUNT, "+ 
					"COUNT(DISTINCT U.I2_ID) TRACING_INFOER_COUNT, "+ 
					"COUNT(DISTINCT U.O3_ID) CONTACTING_CLIENT_COUNT, "+ 
					"COUNT(DISTINCT U.O_ID) TALKING_ORDER_COUNT, "+ 
					"COUNT(DISTINCT U.O4_ID) DEAL_ORDER_COUNT, "+ 
					"COUNT(DISTINCT U.O5_ID) MONTH_TALKING_ORDER_COUNT "+
				"FROM "+
				"("+
					"SELECT "+
						"U.ID, U.ALIAS SALESMAN_NAME, IV.ID IV_ID, OV.ID OV_ID, I.ID I_ID, O2.ID O2_ID, I2.ID I2_ID, O3.ID O3_ID, O.ID O_ID, O4.ID O4_ID, O5.ID O5_ID "+
					"FROM `USER` U "+
					"LEFT JOIN INFOER_VISIT IV ON U.ID = IV.SALESMAN_ID AND IV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
					"LEFT JOIN `ORDER` O ON U.ID = O.SALESMAN_ID AND O.`STATUS` = 34 "+
					"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND OV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
					"LEFT JOIN INFOER I ON I.SALESMAN_ID = U.ID AND I.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
					"LEFT JOIN INFOER I2 ON I2.SALESMAN_ID = U.ID "+
					"LEFT JOIN `ORDER` O2 ON U.ID = O2.SALESMAN_ID AND O2.INSERT_TIME BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
					"LEFT JOIN `ORDER` O3 ON U.ID = O3.SALESMAN_ID "+
					"LEFT JOIN `ORDER` O4 ON U.ID = O4.SALESMAN_ID AND O4.`STATUS` = 90 "+
					"LEFT JOIN `ORDER` O5 ON U.ID = O5.SALESMAN_ID AND O5.`STATUS` in(34,90,0,60,62,64) AND O5.INSERT_TIME BETWEEN CONCAT(concat(date_format(LAST_DAY(now()),'%Y-%m-'),'01'),' 00:00:00') AND CONCAT(LAST_DAY(now()),' 23:59:59') "+
					"WHERE (U.ROLE = 1 OR U.ROLE = 2 OR U.ROLE = 3) "+
					"ORDER BY U.ID "+
				") U "+
				"WHERE 1=1 ";
		if(StringUtils.isNotEmpty(salesmanName)){
			sql +="AND U.SALESMAN_NAME LIKE :salesmanName";
			paramMap.put("salesmanName", '%' + salesmanName + '%');
		}
		sql+="GROUP BY U.ID";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(MarketingCount.class)), count);
	}
	
}
