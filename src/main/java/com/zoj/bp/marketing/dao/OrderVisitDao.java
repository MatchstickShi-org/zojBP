package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.OrderVisit;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class OrderVisitDao extends BaseDao implements IOrderVisitDao {

	@Override
	public DatagridVo<OrderVisit> getAllOrderVisit(Pagination pagination,User loginUser) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM ORDER_VISIT WHERE VISITOR_ID="+loginUser.getId();
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(OrderVisit.class)), count);
	}
	
	@Override
	public Integer addOrderVisit(OrderVisit orderVisit) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO ORDER_VISIT(ORDER_ID,VISITOR_ID,DATE,CONTENT) VALUES(:infoerId,now(),:content)",
				new BeanPropertySqlParameterSource(orderVisit), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
