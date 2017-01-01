package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.dao.BaseDao;
import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

@Repository
public class InfoerDao extends BaseDao implements IInfoerDao {

	@Override
	public Infoer getInfoerByName(String infoerName) {
		try
		{
			return jdbcOps.queryForObject("SELECT * FROM INFOER WHERE NAME = :name",
					new MapSqlParameterSource("name", infoerName), BeanPropertyRowMapper.newInstance(Infoer.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public Infoer getInfoerById(Integer id) {
		try
		{
			return jdbcOps.queryForObject("SELECT I.*,CASE I.`LEVEL` WHEN 1 THEN '金牌' WHEN 2 THEN '银牌' WHEN 3 THEN '铜牌' WHEN 4 THEN '铁牌' END as LEVEL_DESC,U.ALIAS as SALESMAN_NAME FROM INFOER I LEFT JOIN USER U ON I.SALESMAN_ID = U.ID WHERE I.ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(Infoer.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateInfoer(Infoer infoer) {
		String sql = "UPDATE INFOER SET NAME = :name, NATURE = :nature, ORG = :org, ADDRESS = :address, LEVEL = :level, SALESMAN_ID = :salesmanId";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(infoer));
	}

	@Override
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination, User loginUser, String name, String tel, Integer... levels)
	{
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT I.*, "
				+ "		CASE WHEN MAX(IV.DATE) IS NULL THEN DATEDIFF(NOW(),I.INSERT_TIME)  "
				+ "		ELSE DATEDIFF(NOW(),MAX(IV.DATE)) "
				+ " END AS leftVisitDays, U.ALIAS AS SALESMAN_NAME FROM INFOER I "
				+ " LEFT JOIN USER U ON I.SALESMAN_ID = U.ID "
				+ " LEFT JOIN INFOER_VISIT IV ON I.ID = IV.INFOER_ID ";
		if(loginUser.isMarketingSalesman())
			sql += " WHERE U.ID = :userId ";
		else if(loginUser.isMarketingLeader())
		{
			sql += " WHERE U.GROUP_ID = (SELECT U.GROUP_ID FROM USER U WHERE U.ID = :userId) ";
		}
		else
			sql += " WHERE 1=1 ";
		
		paramMap.put("userId", loginUser.getId());
		if(StringUtils.isNotEmpty(name))
		{
			sql += " AND I.NAME LIKE :name";
			paramMap.put("name", '%' + name + '%');
		}
		
		if (StringUtils.isNotEmpty(tel))
		{
			sql += " AND (I.TEL1 LIKE :tel1 OR I.TEL2 LIKE :tel2 OR I.TEL3 LIKE :tel3 OR I.TEL4 LIKE :tel4 OR I.TEL5 LIKE :tel5)";
			paramMap.put("tel1", '%' + tel + '%');
			paramMap.put("tel2", '%' + tel + '%');
			paramMap.put("tel3", '%' + tel + '%');
			paramMap.put("tel4", '%' + tel + '%');
			paramMap.put("tel5", '%' + tel + '%');
		}
		if(ArrayUtils.isNotEmpty(levels))
			sql += " AND I.LEVEL IN(" + StringUtils.join(levels, ',') + ")";
		
		sql +=" GROUP BY I.ID";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY leftVisitDays DESC,I.INSERT_TIME LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Infoer.class)), count);
	}
	
	@Override
	public Integer addInfoer(Infoer infoer)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO INFOER(NAME,NATURE,ORG,ADDRESS,TEL1,TEL2,TEL3,TEL4,TEL5,LEVEL,SALESMAN_ID,INSERT_TIME) VALUES(:name,:nature,:org,:address,:tel1,:tel2,:tel3,:tel4,:tel5,:level,:salesmanId,now())",
				new BeanPropertySqlParameterSource(infoer), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public Infoer findByTel(String tel) {
		try
		{
			return jdbcOps.queryForObject("SELECT I.*,U.ALIAS as SALESMAN_NAME FROM INFOER I LEFT JOIN USER U ON I.SALESMAN_ID = U.ID WHERE tel1 = :tel or tel2 =:tel or tel3 =:tel or tel4 =:tel or tel5 =:tel",
					new MapSqlParameterSource("tel", tel), BeanPropertyRowMapper.newInstance(Infoer.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public DatagridVo<Infoer> findBySalesmanId(Integer salesmanId, Pagination pagination) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT I.* FROM INFOER I "
				+ " WHERE I.SALESMAN_ID = :salesmanId ";
		paramMap.put("salesmanId",salesmanId);
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " ORDER BY I.INSERT_TIME LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Infoer.class)), count);
	}

	@Override
	public Integer updateInfoerSalesmanId(Integer[] infoerIds, Integer salesmanId) {
		return jdbcOps.update("UPDATE INFOER SET SALESMAN_ID = :salesmanId "
				+ " WHERE ID IN(" + StringUtils.join(infoerIds, ',') + ")", new MapSqlParameterSource("salesmanId",salesmanId));
	}
}
