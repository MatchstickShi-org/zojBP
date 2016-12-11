package com.zoj.bp.marketing.dao;

import java.util.HashMap;
import java.util.Map;

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
			return jdbcOps.queryForObject("SELECT * FROM INFOER WHERE ID = :id",
					new MapSqlParameterSource("id", id), BeanPropertyRowMapper.newInstance(Infoer.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	@Override
	public void updateInfoer(Infoer infoer) {
		String sql = "UPDATE INFOER SET NAME = :name, NATURE = :nature, ORG = :org, ADDRESS = :address,"
				+ " TEL = :tel, TEL2 = :tel2, TEL3 = :tel3, TEL4 = :tel4, TEL5 = :tel5, LEVEL = :level, SALESMAN_ID = :salesmanId";
		sql += " WHERE ID = :id";
		jdbcOps.update(sql, new BeanPropertySqlParameterSource(infoer));
	}

	@Override
	public DatagridVo<Infoer> getAllInfoer(Pagination pagination,User loginUser) {
		Map<String, Object> paramMap = new HashMap<>();
		String sql = "SELECT * FROM INFOER WHERE SALESMAN_ID="+loginUser.getId();
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, paramMap, Integer.class);
		sql += " LIMIT :start, :rows";
		paramMap.put("start", pagination.getStartRow());
		paramMap.put("rows", pagination.getRows());
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Infoer.class)), count);
	}
	
	@Override
	public Integer addInfoer(Infoer infoer) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO INFOER(NAME,NATURE,ORG,ADDRESS,TEL,TEL2,TEL3,TEL4,TEL5,LEVEL,SALESMAN_ID) VALUES(:name,:nature,:org,:address,:tel,:tel2,:tel3,:tel4,:tel5,:level,:salesmanId)",
				new BeanPropertySqlParameterSource(infoer), keyHolder);
		return keyHolder.getKey().intValue();
	}

}
