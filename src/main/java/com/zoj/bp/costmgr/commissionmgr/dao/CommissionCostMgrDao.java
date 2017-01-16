package com.zoj.bp.costmgr.commissionmgr.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.costmgr.commissionmgr.vo.CommissionCost;

/**
 * @author MatchstickShi
 */
@Repository
public class CommissionCostMgrDao extends BaseDao implements ICommissionCostMgrDao
{
	@Override
	public Integer addCommissionCostRecord(CommissionCost commissionCost)
	{
		String sql = "INSERT INTO COMMISSION_COST(INFOER_ID, ORDER_ID, DATE, AMOUNT, REMARK) "
				+ " VALUES(:infoerId, :orderId, CURRENT_DATE, :cost, :remark)";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(commissionCost), keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public DatagridVo<CommissionCost> getCommissionCostsByOrder(Integer orderId, Pagination pagination)
	{
		MapSqlParameterSource params = new MapSqlParameterSource();
		String sql = "SELECT IC.ID, O.ID ORDER_ID, C.ID CLIENT_ID, C.NAME CLIENT_NAME, O.PROJECT_ADDR, O.INFOER_ID, I.NAME INFOER, "
				+ " O.DESIGNER_ID, D.ALIAS DESIGNER, O.SALESMAN_ID, S.ALIAS SALESMAN, "
				+ " IC.DATE REMIT_DATE, IC.AMOUNT COST, IC.REMARK FROM COMMISSION_COST IC "
				+ " LEFT JOIN `ORDER` O ON IC.ORDER_ID = O.ID "
				+ " LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "
				+ " LEFT JOIN INFOER I ON O.INFOER_ID = I.ID "
				+ " LEFT JOIN USER D ON O.DESIGNER_ID = D.ID "
				+ " LEFT JOIN USER S ON O.SALESMAN_ID = S.ID "
				+ " WHERE O.ID = :orderId ";
		
		params.addValue("orderId", orderId);
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY IC.DATE DESC, O.ID DESC LIMIT :start, :rows";
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params.addValue("start", pagination.getStartRow())
					.addValue("rows", pagination.getRows()), BeanPropertyRowMapper.newInstance(CommissionCost.class)), count);
	}

	@Override
	public CommissionCost getCommissionCostByOrder(Integer orderId)
	{
		String sql = "SELECT O.ID ORDER_ID, C.ID CLIENT_ID, C.NAME CLIENT_NAME, O.PROJECT_ADDR, O.INFOER_ID, I.NAME INFOER, "
				+ " O.DESIGNER_ID, D.ALIAS DESIGNER, O.SALESMAN_ID, S.ALIAS SALESMAN, "
				+ " IC.DATE REMIT_DATE, IC.AMOUNT COST, IC.REMARK FROM `ORDER` O "
				+ " LEFT JOIN COMMISSION_COST IC ON IC.ORDER_ID = O.ID "
				+ " LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "
				+ " LEFT JOIN INFOER I ON O.INFOER_ID = I.ID "
				+ " LEFT JOIN USER D ON O.DESIGNER_ID = D.ID "
				+ " LEFT JOIN USER S ON O.SALESMAN_ID = S.ID "
				+ " WHERE O.ID = :orderId";
		return jdbcOps.queryForObject(sql, 
				new MapSqlParameterSource("orderId", orderId), BeanPropertyRowMapper.newInstance(CommissionCost.class));
	}
}
