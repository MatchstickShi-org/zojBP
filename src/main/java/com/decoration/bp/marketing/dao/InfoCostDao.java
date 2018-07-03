package com.decoration.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.decoration.bp.common.dao.BaseDao;
import com.decoration.bp.common.model.InfoCost;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

@Repository
public class InfoCostDao extends BaseDao implements IInfoCostDao {

	@Override
	public DatagridVo<InfoCost> getAllInfoCost(Pagination pagination,Integer infoerId,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = 
				"SELECT IC.*, O.PROJECT_NAME, O.STATUS orderStatus, "
				+ " I.`NAME` AS infoerName,U.ALIAS AS salesmanName, U2.ALIAS AS designerName "
				+ " FROM INFO_COST IC "
				+ " LEFT JOIN `ORDER` O ON O.ID = IC.ORDER_ID "
				+ " LEFT JOIN INFOER I ON I.ID = IC.INFOER_ID "
				+ " LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID "
				+ " LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID "
				+ " WHERE 1=1 ";
		if(infoerId != null)
		{
			sql += "AND IC.INFOER_ID= :infoerId ";
			paramMap.put("infoerId",infoerId);
		}
		if(orderId != null)
		{
			sql += "AND IC.ORDER_ID = :orderId ";
			paramMap.put("orderId",orderId);
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(InfoCost.class)), count);
	}
	
	@Override
	public Integer addInfoCost(InfoCost infoCost) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO INFO_COST(INFOER_ID,ORDER_ID,DATE,AMOUNT,REMARK) VALUES(:infoerId,:orderId,now(),:amount,:remark)",
				new BeanPropertySqlParameterSource(infoCost), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
