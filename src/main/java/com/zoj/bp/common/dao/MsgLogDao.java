/**  */
package com.zoj.bp.common.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.zoj.bp.common.model.MsgLog;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;

/**
 * @author MatchstickShi
 */
@Repository
public class MsgLogDao extends BaseDao implements IMsgLogDao
{
	@Override
	public DatagridVo<MsgLog> getAllBroadcastMsgs(Pagination pagination)
	{
		String sql = "SELECT * FROM MSG_LOG";
		String countSql = "SELECT COUNT(1) count FROM (" + sql + ") T";
		Integer count = jdbcOps.queryForObject(countSql, EmptySqlParameterSource.INSTANCE, Integer.class);
		sql += " ORDER BY SEND_TIME DESC LIMIT :start, :rows";
		return DatagridVo.buildDatagridVo(jdbcOps.query(sql, new MapSqlParameterSource("start", pagination.getStartRow())
					.addValue("rows", pagination.getRows()), BeanPropertyRowMapper.newInstance(MsgLog.class)), count);
	}

	@Override
	public Integer addBroadcastMsg(String content)
	{
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOps.update(
				"INSERT INTO MSG_LOG(CONTENT) VALUES(:content)",
				new MapSqlParameterSource("content", content), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public MsgLog getMgsById(Integer msgId)
	{
		return jdbcOps.queryForObject("SELECT * FROM MSG_LOG WHERE ID = :id", new MapSqlParameterSource("id", msgId), BeanPropertyRowMapper.newInstance(MsgLog.class));
	}

	@Override
	public List<MsgLog> getLast24hoursBroadcastMsgs()
	{
		String sql = "SELECT * FROM MSG_LOG WHERE SEND_TIME >= DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -1 DAY)";
		List<MsgLog> ms = jdbcOps.query(sql, BeanPropertyRowMapper.newInstance(MsgLog.class));
		while(ms.size() > 50)
			ms.remove(0);
		return ms;
	}
}