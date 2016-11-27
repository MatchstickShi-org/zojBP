package com.zoj.bp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * @author MatchstickShi
 */
public class EncryptUtil
{
	public static String encoderByMd5(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return new BASE64Encoder().encode(MessageDigest.getInstance("MD5").digest(plaintext.getBytes("utf-8")));
	}
}