package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dao.MemberDAO;

import lombok.Setter;

@Controller
@Setter
public class AdminController {
	@Autowired
	private MemberDAO dao;
	
	@GetMapping("/admin/member/delete/{id}")
	public String delete(@PathVariable("id") String id) {
		dao.deleteById(id);
		return "redirect:/admin/index";
	}
	
	@GetMapping("/admin/index")
	public void index(Model model) {
		model.addAttribute("list", dao.findAll());
	}
}
