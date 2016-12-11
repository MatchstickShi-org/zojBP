/**
 * 
 */
package com.zoj.bp.marketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zoj.bp.marketing.service.IInfoerService;

/**
 * @author wangw
 *
 */
@Controller("/marketing")
public class MarketingCtrl
{
	@Autowired
	private IInfoerService infoerService;
	
}