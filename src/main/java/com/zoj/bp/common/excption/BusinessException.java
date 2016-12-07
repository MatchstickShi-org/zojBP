/**
 * 
 */
package com.zoj.bp.common.excption;

/**
 * @author MatchstickShi
 */
public class BusinessException extends Exception
{
	private static final long serialVersionUID = 7465316833414514936L;
	
	private ReturnCode returnCode = ReturnCode.SUCCESS;

	public BusinessException(ReturnCode returnCode, String msg)
	{
		super(msg);
		this.returnCode = returnCode;
		this.returnCode.setMsg(msg);
	}
	
	public BusinessException(ReturnCode returnCode)
	{
		super(returnCode.getMsg());
		this.returnCode = returnCode;
	}
	
	/**
	 * @param returnCode
	 * @param ex
	 */
	public BusinessException(ReturnCode returnCode, Exception ex)
	{
		super(ex.getMessage());
		this.returnCode = returnCode;
		this.setStackTrace(ex.getStackTrace());
	}

	public String getReturnMsg()
	{
		return returnCode.getMsg();
	}

	/**
	 * @return
	 */
	public int getReturnCode()
	{
		return returnCode.getReturnCode();
	}
}