package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.OrderApprove;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class OrderApproveDao extends BaseDao implements IOrderApproveDao {

	@Override
	public DatagridVo<OrderApprove> getAllOrderApprove(Pagination pagination,User loginUser,Integer orderId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM ORDER_APPROVE WHERE APPROVER="+loginUser.getId();
		if(orderId != null && orderId > 0)
		{
			sql += " AND ORDER_ID = :orderId";
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
				"INSERT INTO ORDER_APPROVE(ORDER_ID,CLAIMER,APPROVER,OPERATE,OPERATE_TIME,REMARK) VALUES(:orderId,:claimer,:approver,:operate,now(),:remark)",
				new BeanPropertySqlParameterSource(orderApprove), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
