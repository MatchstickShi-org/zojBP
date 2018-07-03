/**  */
package com.decoration.bp.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.decoration.bp.common.model.Order;

/**
 * @author MatchstickShi
 */
public class CommontTest
{
	@Test
	public void testInteger()
	{
		Long i = new Long("13813800001");
		System.out.println(i);
	}
	
	@Test
	public void testLocalDateTime()
	{
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}
	
	@Test
	public void testStringJoin()
	{
		System.out.println(StringUtils.join(Order.Status.values(), ','));
	}
}