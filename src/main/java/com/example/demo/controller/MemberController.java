package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class MemberController {
	
	@Autowired
	private MemberService ms;
	
	//로그인처리
	@GetMapping("/member/login")
	public void login() {		
	
	}
	
	
	//로그인 후 로그아웃처리
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session != null ) {		//로그인중이라면
			session.invalidate();	//세션무효화
		}
		return "redirect:/member/login";
	}
	
	

	// 관리자계정(admin) > 회원삭제
	public String delete(String id) {
		String view = "redirect:index";
		ms.delete(id);
		return view;
	}
}
