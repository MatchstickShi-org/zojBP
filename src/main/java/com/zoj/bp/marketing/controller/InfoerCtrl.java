/**
 * 
 */
package com.zoj.bp.marketing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoj.bp.common.model.Infoer;
import com.zoj.bp.common.model.User;
import com.zoj.bp.common.vo.DatagridVo;
import com.zoj.bp.common.vo.Pagination;
import com.zoj.bp.marketing.service.IInfoerService;
import com.zoj.bp.marketing.service.IInfoerVisitService;

/**
 * @author wangw
 *
 */
@Controller("/marketingMgr/infoerMgr")
public class InfoerCtrl
{
	@Autowired
	private IInfoerService infoerService;
	
	@Autowired
	private IInfoerVisitService infoerVisitService;
	
	@RequestMapping(value = "/getAllInfoers")
	@ResponseBody
	public DatagridVo<Infoer> getAllInfoers(Pagination pagination,
			@RequestParam(required=false) String userName, HttpSession session)
	{
		User loginUser = (User) session.getAttribute("loginUser");
		return infoerService.getAllInfoer(pagination, loginUser);
	}
	
}