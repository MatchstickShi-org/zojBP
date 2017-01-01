package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.CommissionCost;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class CommissionCostDao extends BaseDao implements ICommissionCostDao {

	@Override
	public DatagridVo<CommissionCost> getAllCommissionCost(Pagination pagination,Integer infoerId,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT CC.*,O.PROJECT_NAME,I.`NAME` AS infoerName,U.ALIAS AS salesmanName,U2.ALIAS AS designerName FROM COMMISSION_COST CC "+
				"LEFT JOIN `ORDER` O ON O.ID = CC.ORDER_ID "+
				"LEFT JOIN INFOER I ON I.ID = CC.INFOER_ID "+
				"LEFT JOIN `USER` U ON U.ID = O.SALESMAN_ID "+
				"LEFT JOIN `USER` U2 ON U2.ID = O.DESIGNER_ID "+
				"WHERE 1=1 ";
		if(infoerId != null)
		{
			sql +=" AND CC.INFOER_ID =: infoerId";
			paramMap.put("infoerId", infoerId);
		}
		if(orderId != null)
		{
			sql +=" AND CC.ORDER_ID =: orderId";
			paramMap.put("orderId", orderId);
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(CommissionCost.class)), count);
	}
	
	@Override
	public Integer addCommissionCost(CommissionCost commissionCost) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO COMMISSION_COST(INFOER_ID,ORDER_ID,DATE,AMOUNT,REMARK) VALUES(:infoerId,:orderId,now(),:amount,:remark)",
				new BeanPropertySqlParameterSource(commissionCost), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
