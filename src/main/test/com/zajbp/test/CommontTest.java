/**  */
package com.zajbp.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

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
}