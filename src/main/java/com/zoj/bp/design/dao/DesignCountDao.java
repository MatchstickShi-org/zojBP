package com.zoj.bp.design.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
		String sql = "SELECT "+
						"U.ID DESIGNER_ID, U.DESIGNER_NAME, "+
						"COUNT(DISTINCT U.OV_ID) TODAY_ORDER_VISIT_COUNT, "+
						"COUNT(DISTINCT U.O_ID) TALKING_ORDER_COUNT, "+
						"COUNT(DISTINCT U.O2_ID) DEAL_ORDER_COUNT, "+
						"COUNT(DISTINCT U.O3_ID) DEAD_ORDER_COUNT "+
					"FROM "+ 
					"( "+
						"SELECT "+
							"U.ID, U.ALIAS DESIGNER_NAME,OV.ID OV_ID,O.ID O_ID,O2.ID O2_ID,O3.ID O3_ID "+
						"FROM `USER` U "+
						"LEFT JOIN `ORDER` O ON U.ID = O.DESIGNER_ID AND O.`STATUS` = 34 "+
						"LEFT JOIN ORDER_VISIT OV ON OV.ORDER_ID = O.ID AND OV.DATE BETWEEN CONCAT(CURRENT_DATE,' 00:00:00') AND CONCAT(CURRENT_DATE,' 23:59:59') "+
						"LEFT JOIN `ORDER` O2 ON U.ID = O2.DESIGNER_ID AND O2.`STATUS` = 90 "+
						"LEFT JOIN `ORDER` O3 ON U.ID = O3.DESIGNER_ID AND O3.`STATUS` = 0 "+
						"WHERE (U.ROLE = 4 OR U.ROLE = 5 OR U.ROLE = 6) "+
						"ORDER BY U.ID "+
					") U "+
					"WHERE 1= 1 ";
		if(StringUtils.isNotEmpty(designerName)){
			sql +="AND U.DESIGNER_NAME LIKE :designerName ";
			paramMap.put("designerName", '%' + designerName + '%');
		}
		sql+="GROUP BY U.ID";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DesignCount.class)), count);
	}
}
