package com.zoj.bp.design.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.vo.DesignerVisitApply;

@Repository
public class DesignerVisitApplyDao extends BaseDao implements IDesignerVisitApplyDao {

	@Override
	public DatagridVo<DesignerVisitApply> getAllDesignerVisitApply(Pagination pagination,String designerName,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT DVA.*,U.ALIAS APPROVER_NAME,U2.ALIAS DESIGNER_NAME"
				+ " FROM DESIGNER_VISIT_APPLY DVA"
				+ " LEFT JOIN `USER` U ON U.ID = DVA.APPROVER"
				+ " LEFT JOIN `USER` U2 ON U2.ID = DVA.DESIGNER"
				+ " WHERE 1=1 ";
		if(StringUtils.isNotEmpty(designerName)){
			sql += " AND U2.ALIAS LIKE :designerName";
			paramMap.put("designerName",'%' + designerName+ '%');
		}
		if(orderId != null && orderId > 0)
		{
			sql += " AND DVA.ORDER_ID = :orderId";
			paramMap.put("orderId", orderId);
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DesignerVisitApply.class)), count);
	}
	
	@Override
	public Integer addDesignerVisitApply(DesignerVisitApply designerVisitApply) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO DESIGNER_VISIT_APPLY(ORDER_ID,DESIGNER,APPROVER,STATUS,CREATE_TIME) VALUES(:orderId,:designer,:approver,:status,now())",
				new BeanPropertySqlParameterSource(designerVisitApply), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
