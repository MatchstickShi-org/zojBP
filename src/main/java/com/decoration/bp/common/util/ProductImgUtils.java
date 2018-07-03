/**  */
package com.decoration.bp.common.util;

import java.io.File;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;

/**
 * @author MatchstickShi
 */
public class ProductImgUtils
{
	public static String PRODUCT_IMG_PATH_PREFIX = File.separatorChar + "img" + File.separatorChar + "product";
	
	public static String PRODUCT_IMG_URL_PREFIX = "img/product";
	
	public static String buildRelativeImgUrl(Integer productId, String fileName)
	{
		return PRODUCT_IMG_URL_PREFIX + "/" + productId + "/" + fileName;
	}
	
	public static String buildFullPath(Integer productId, String fileName)
	{
		return buildPrefixPath(productId) + File.separatorChar + buildUuidFileName(fileName);
	}
	
	public static String buildFullPath(Integer productId, String fileName, boolean mkdir)
	{
		return buildPrefixPath(productId, mkdir) + File.separatorChar + buildUuidFileName(fileName);
	}
	
	public static String buildPrefixPath(Integer productId)
	{
		return ContextLoader.getCurrentWebApplicationContext().getServletContext()
				.getRealPath("") + PRODUCT_IMG_PATH_PREFIX + File.separatorChar + productId;
	}
	
	public static String buildPrefixPath(Integer productId, boolean mkdir)
	{
		String path = buildPrefixPath(productId);
		if(mkdir)
		{
			File file = new File(path);
			if(!file.exists())
				file.mkdirs();
		}
		return path;
	}
	
	public static String buildUuidFileName(String fileName)
	{
		return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
	}
}