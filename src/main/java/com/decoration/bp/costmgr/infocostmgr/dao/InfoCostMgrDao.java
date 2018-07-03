package com.decoration.bp.costmgr.infocostmgr.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.decoration.bp.common.dao.BaseDao;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;
import com.decoration.bp.costmgr.infocostmgr.vo.InfoCost;

/**
 * @author MatchstickShi
 */
@Repository
public class InfoCostMgrDao extends BaseDao implements IInfoCostMgrDao
{
	@Override
	public Integer addInfoCostRecord(InfoCost infoCost)
	{
		String sql = "INSERT INTO INFO_COST(INFOER_ID, ORDER_ID, DATE, AMOUNT, REMARK) "
				+ " VALUES(:infoerId, :orderId, CURRENT_DATE, :cost, :remark)";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(infoCost), keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public DatagridVo<InfoCost> getAllInfoCosts(User user, Integer status, String clientName, String orderId, Pagination pagination)
	{
		MapSqlParameterSource params = new MapSqlParameterSource();
		String sql = "SELECT O.ID ORDER_ID, O.STATUS ORDER_STATUS, C.ID CLIENT_ID, C.NAME CLIENT_NAME, "
				+ " 	O.PROJECT_ADDR, O.INFOER_ID, I.NAME INFOER, O.DESIGNER_ID, D.ALIAS DESIGNER, O.SALESMAN_ID, "
				+ " 	S.ALIAS SALESMAN, IC.DATE REMIT_DATE, IC.AMOUNT COST, IC.REMARK "
				+ " FROM `ORDER` O "
				+ " LEFT JOIN INFO_COST IC ON IC.ORDER_ID = O.ID "
				+ " LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "
				+ " LEFT JOIN INFOER I ON O.INFOER_ID = I.ID "
				+ " LEFT JOIN USER D ON O.DESIGNER_ID = D.ID "
				+ " LEFT JOIN USER S ON O.SALESMAN_ID = S.ID ";
		if(user.isMarketingSalesman())
			sql += " WHERE O.SALESMAN_ID = :userId ";
		else if(user.isMarketingLeader())
			sql += " WHERE S.GROUP_ID = (SELECT U.GROUP_ID FROM USER U WHERE U.ID = :userId) ";
		else
			sql += " WHERE 1=1 ";
		
		if(status != null)
			sql += " AND IC.ID IS " + (status == 1 ? " NOT NULL " : "NULL");
		if(StringUtils.isNotEmpty(clientName))
		{
			sql += " AND C.NAME LIKE :clientName ";
			params.addValue("clientName", '%' + clientName + '%');
		}
		if(StringUtils.isNotEmpty(orderId))
		{
			sql += " AND O.ID LIKE :orderId ";
			params.addValue("orderId", '%' + orderId + '%');
		}
		
		params.addValue("userId", user.getId());
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, params, Integer.class);
		sql += " ORDER BY IC.DATE DESC, O.ID DESC LIMIT :start, :rows";
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, params.addValue("start", pagination.getStartRow())
					.addValue("rows", pagination.getRows()), BeanPropertyRowMapper.newInstance(InfoCost.class)), count);
	}

	@Override
	public InfoCost getInfoCostByOrder(Integer orderId)
	{
		String sql = "SELECT O.ID ORDER_ID, C.ID CLIENT_ID, C.NAME CLIENT_NAME, O.PROJECT_ADDR, O.INFOER_ID, I.NAME INFOER, "
				+ " O.DESIGNER_ID, D.ALIAS DESIGNER, O.SALESMAN_ID, S.ALIAS SALESMAN, "
				+ " IC.DATE REMIT_DATE, IC.AMOUNT COST, IC.REMARK FROM `ORDER` O "
				+ " LEFT JOIN INFO_COST IC ON IC.ORDER_ID = O.ID "
				+ " LEFT JOIN CLIENT C ON O.ID = C.ORDER_ID "
				+ " LEFT JOIN INFOER I ON O.INFOER_ID = I.ID "
				+ " LEFT JOIN USER D ON O.DESIGNER_ID = D.ID "
				+ " LEFT JOIN USER S ON O.SALESMAN_ID = S.ID "
				+ " WHERE O.ID = :orderId";
		return jdbcOps.queryForObject(sql, 
				new MapSqlParameterSource("orderId", orderId), BeanPropertyRowMapper.newInstance(InfoCost.class));
	}
}
