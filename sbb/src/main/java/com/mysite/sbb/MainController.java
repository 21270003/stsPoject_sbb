package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/sbb")
	@ResponseBody
	public String index() {
		System.out.println("index");
		return "한녕하세요. SBB에 오신것을 환영합니다...";
	}
	
	@GetMapping({"/",""})
	public String goRoot() {
		return "redirect:/question/list";
		
	}
	
	
}
