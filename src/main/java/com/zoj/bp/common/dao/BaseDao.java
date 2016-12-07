package com.zoj.bp.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDao
{
	@Autowired
	protected NamedParameterJdbcOperations jdbcOps;
}