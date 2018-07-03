package com.decoration.bp.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.decoration.bp.common.model.OrderApprove;
import com.decoration.bp.common.model.User;
import com.decoration.bp.common.vo.DatagridVo;
import com.decoration.bp.common.vo.Pagination;

@Repository
public class OrderApproveDao extends BaseDao implements IOrderApproveDao
{
	@Override
	public DatagridVo<OrderApprove> getAllOrderApprove(Pagination pagination,User loginUser,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT OA.*,U.ALIAS APPROVER_NAME,U2.ALIAS CLAIMER_NAME"
				+ " FROM ORDER_APPROVE OA"
				+ " LEFT JOIN `USER` U ON U.ID = OA.APPROVER"
				+ " LEFT JOIN `USER` U2 ON U2.ID = OA.CLAIMER"
				+ " WHERE 1=1 ";
		if(loginUser != null){
			sql += " AND OA.APPROVER= :approver";
			paramMap.put("approver", loginUser.getId());
		}
		if(orderId != null && orderId > 0)
		{
			sql += " AND OA.ORDER_ID = :orderId";
			paramMap.put("orderId", orderId);
		}
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(OrderApprove.class)), count);
	}
	
	@Override
	public Integer addOrderApprove(OrderApprove orderApprove) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO ORDER_APPROVE(ORDER_ID,CLAIMER,APPROVER,OPERATE,STATUS,OPERATE_TIME,REMARK) VALUES(:orderId,:claimer,:approver,:operate,:status,now(),:remark)",
				new BeanPropertySqlParameterSource(orderApprove), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Integer deleteBySalesmans(Integer... salesmanIds)
	{
		return jdbcOps.update("DELETE A FROM ORDER_APPROVE A LEFT JOIN `ORDER` O ON A.ORDER_ID = O.ID"
				+ " WHERE O.SALESMAN_ID IN (" + StringUtils.join(salesmanIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
	
	@Override
	public Integer deleteByDesigners(Integer... designerIds)
	{
		return jdbcOps.update("DELETE A FROM ORDER_APPROVE A LEFT JOIN `ORDER` O ON A.ORDER_ID = O.ID"
				+ " WHERE O.DESIGNER_ID IN (" + StringUtils.join(designerIds, ',') + ")", EmptySqlParameterSource.INSTANCE);
	}
}
