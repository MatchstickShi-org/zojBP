/**
 * 
 */
package com.zoj.bp.common.excption;

/**
 * @author MatchstickShi
 */
public enum ReturnCode
{
	/**操作成功*/
	SUCCESS(0, "操作成功。"),
	/**号码已存在*/
	TEL_EXISTS(3000, "号码已存在。"),
	/**登录用户重复*/
	LOGIN_USER_REPEATED(4000, "当前用户已登录。"),
	/**会话过期*/
	SESSION_TIME_OUT(9998, "会话已过期。"),
	/**系统内部错误*/
	SYSTEM_INTERNAL_ERROR(9999, "系统内部错误，请联系管理员。"),
	/**找不到记录*/
	NOT_FIND_RECORD(7000, "找不到要查询的信息，请稍后再试。"),
	/**非法操作*/
	ILLEGALITY_OPERATION(6000, "不允许的操作。"),
	/**验证失败*/
	VALIDATE_FAIL(5000, "验证失败，请检查录入数据。");
	
	private int returnCode = 0;
	private String msg;

	private ReturnCode(int returnCode, String msg)
	{
		this.returnCode  = returnCode;
		this.msg = msg;
	}
	
	public int getValue()
	{
		return returnCode;
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public ReturnCode setMsg(String msg)
	{
		this.msg = msg;
		return this;
	}
}