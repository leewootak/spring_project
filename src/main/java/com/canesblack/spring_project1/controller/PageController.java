package com.canesblack.spring_project1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// @Component 스프링 빈으로 등록하기 위한 라벨링 작업
@Controller
public class PageController {
	// "/" = localhost:8080
	@GetMapping("/")
	public String home() {
		return "index";
	}

	// 페이지 조회 및 이동할 때 @GetMapping()
	// @PostMapping()
	// @PutMapping()
	// @DeleteMapping()

	// /register => localhost:8080/register
	@GetMapping("/register")
	public String registerPage() {
		return "register/index";
	}

	// /loginPage => localhost:8080/loginPage
	@GetMapping("/loginPage")
	public String loginPage() {
		return "login/index";
	}
}
