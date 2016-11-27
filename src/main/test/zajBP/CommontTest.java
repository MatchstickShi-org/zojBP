/**  */
package zajBP;

import java.util.Random;

/**
 * @author MatchstickShi
 */
public class CommontTest
{
	public static void main(String[] args)
	{
		Random random = new Random(10000);
		for (int i = 0; i < 100; i++)
		{
			System.out.println(random.nextInt(999999));
		}
	}
}