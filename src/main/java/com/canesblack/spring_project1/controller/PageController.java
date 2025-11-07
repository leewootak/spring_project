package com.canesblack.spring_project1.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

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
	@GetMapping("/registerPage")
	public String registerPage(HttpServletRequest request, org.springframework.ui.Model model) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf", csrfToken);
		return "register/index";
	}

	@GetMapping("/loginPage")
	public String loginPage(HttpServletRequest request, org.springframework.ui.Model model) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf", csrfToken);
		return "login/index";
	}
}
