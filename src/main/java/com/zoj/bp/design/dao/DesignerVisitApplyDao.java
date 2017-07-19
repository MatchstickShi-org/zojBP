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
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.design.vo.DesignerVisitApply;

@Repository
public class DesignerVisitApplyDao extends BaseDao implements IDesignerVisitApplyDao {

	@Override
	public DesignerVisitApply getDesignerVisitApplyById(Integer id) {
		String sql = "SELECT DVA.* "
				+ " FROM DESIGNER_VISIT_APPLY DVA"
				+ " WHERE DVA.id =:id";
		return jdbcOps.queryForObject(sql,new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(DesignerVisitApply.class));
	}
	@Override
	public List<DesignerVisitApply> getTodayApplysByOrder(Integer orderId)
	{
		String sql = "SELECT DVA.*,U.ALIAS APPROVER_NAME,U2.ALIAS DESIGNER_NAME "
				+ " FROM DESIGNER_VISIT_APPLY DVA"
				+ " LEFT JOIN `USER` U ON U.ID = DVA.APPROVER"
				+ " LEFT JOIN `USER` U2 ON U2.ID = DVA.DESIGNER"
				+ " WHERE DVA.ORDER_ID = :orderId AND DVA.`STATUS` = 0 AND TO_DAYS(CREATE_TIME) = TO_DAYS(CURRENT_DATE) ";
		return jdbcOps.query(sql, new MapSqlParameterSource("orderId", orderId), BeanPropertyRowMapper.newInstance(DesignerVisitApply.class));
	}
	@Override
	public DatagridVo<DesignerVisitApply> getTodayDesignerVisitApplys(
			Pagination pagination,String designerName,Integer orderId,Integer... status)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT DVA.*," + 
				" 	CASE WHEN MAX(OV.DATE) IS NULL THEN DATEDIFF(CURRENT_DATE, O.INSERT_TIME) " + 
				" 	ELSE DATEDIFF(CURRENT_DATE, MAX(OV.DATE)) END AS notVisitDays, " + 
				" 	U.ALIAS APPROVER_NAME,U2.ALIAS DESIGNER_NAME,U3.ALIAS SALESMAN_NAME,C.`NAME`,O.`STATUS` ORDER_STATUS " + 
				" FROM DESIGNER_VISIT_APPLY DVA " + 
				" LEFT JOIN `USER` U ON U.ID = DVA.APPROVER " + 
				" LEFT JOIN `USER` U2 ON U2.ID = DVA.DESIGNER " + 
				" LEFT JOIN `ORDER` O ON O.ID = DVA.ORDER_ID " + 
				" LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID " + 
				" LEFT JOIN `USER` U3 ON O.SALESMAN_ID = U3.ID " + 
				" LEFT JOIN ORDER_VISIT OV ON O.ID = OV.ORDER_ID AND O.DESIGNER_ID = OV.VISITOR_ID " + 
				" WHERE TO_DAYS(DVA.CREATE_TIME) = TO_DAYS(CURRENT_DATE) ";
		if(StringUtils.isNotEmpty(designerName))
		{
			sql += " AND U2.ALIAS LIKE :designerName";
			paramMap.put("designerName",'%' + designerName+ '%');
		}
		if(orderId != null && orderId > 0)
		{
			sql += " AND DVA.ORDER_ID = :orderId";
			paramMap.put("orderId", orderId);
		}
		if(status != null && status.length > 0)
			sql +=" AND DVA.`STATUS` IN(" + StringUtils.join(status, ',') + ")";
		sql += " GROUP BY DVA.ID";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY DVA.`STATUS`,DVA.CREATE_TIME";
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

	@Override
	public Integer updateVisitApply(DesignerVisitApply designerVisitApply) {
		String sql = "UPDATE DESIGNER_VISIT_APPLY SET APPROVER = :approver, STATUS = :status";
		sql += " WHERE ID = :id";
		return jdbcOps.update(sql, new BeanPropertySqlParameterSource(designerVisitApply));
	}
}
