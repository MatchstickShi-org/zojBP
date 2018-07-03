/**  */
package com.decoration.bp.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author MatchstickShi
 */
public class DateTimeUtils
{
	private static DateTimeUtils instance = new DateTimeUtils();
	
	public static DateTimeUtils instance()
	{
		return instance;
	}
	
	public LocalDateTime toDateTime(Date date)
	{
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}