package com.infotop.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infotop.common.BasicController;

@Controller
@RequestMapping(value = "/home")
public class MainController extends BasicController {

	/**
	 * 首页系统选择页面
	 * @return
	 */
	@RequestMapping(value = "")
	public String mainframe() {
		return "mainFrame";
	}

}
