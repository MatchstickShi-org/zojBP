package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.InfoerVisit;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class InfoerVisitDao extends BaseDao implements IInfoerVisitDao {

	@Override
	public DatagridVo<InfoerVisit> getAllInfoerVisit(Pagination pagination,Integer infoerId) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM INFOER_VISIT WHERE INFOER_ID="+infoerId;
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(InfoerVisit.class)), count);
	}
	
	@Override
	public Integer addInfoerVisit(InfoerVisit infoerVisit) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO INFOER_VISIT(INFOER_ID,SALESMAN_ID,DATE,CONTENT) VALUES(:infoerId,:salesmanId,now(),:content)",
				new BeanPropertySqlParameterSource(infoerVisit), keyHolder);
		return keyHolder.getKey().intValue();
	}
}
