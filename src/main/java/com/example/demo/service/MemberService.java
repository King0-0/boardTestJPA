package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;

import lombok.Setter;

@Setter
@Service
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberDAO dao;
	

	public Member findByID(String id) {
		return dao.findById(id).get();
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = null;
		//아이디가 잘못되면 못불러오니까 예외적용
		
		System.out.println("회원을 추가했습니다.");

		try {
			System.out.println("회원아이디:"+username);
			Member m = dao.findById(username).get();
		//	Member m = dao.getOne(username);
			System.out.println("회원의 정보:"+m);
			if(m == null) {
				throw new UsernameNotFoundException(username);
			}else {
				user = User.builder().username(username)
						.password(m.getPwd())
						.roles(m.getRole())
						.build();
			}
		} catch (Exception e) {
			System.out.println("예외발생 : "+e.getMessage());
		}
		
		return user;
	}
	
	
	//// 관리자 - 멤버 삭제
	public void delete(String id) {
		dao.deleteById(id);
	}

}
		
		//멤버새로등록 > 비밀번호는 암호화해야해 (비어있는 테이블에 계정넣어주기)
//		dao.save(new Member("kim", "김유신", PasswordEncoderFactories
//				.createDelegatingPasswordEncoder().encode("kim"),"user"));
//		dao.save(new Member("lee", "이순신", PasswordEncoderFactories
//				.createDelegatingPasswordEncoder().encode("lee"), "user"));
//		dao.save(new Member("yu", "유관순", PasswordEncoderFactories
//				.createDelegatingPasswordEncoder().encode("yu"), "user"));
//		dao.save(new Member("gang", "강감찬", PasswordEncoderFactories
//				.createDelegatingPasswordEncoder().encode("gang"), "user"));
//		dao.save(new Member("hong", "홍길동",  PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("hong"),"admin"));
		
//		Member kim = new Member();
//		kim.setId("kim");
//		kim.setName("김유신");
//		kim.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("kim"));
//		kim.setRole("user");
//		dao.save(kim);
//	
//		Member lee = new Member();
//		lee.setId("lee");
//		lee.setName("이순신");
//		lee.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("lee"));
//		lee.setRole("user");
//		dao.save(lee);
//		
//		Member hong = new Member();
//		hong.setId("hong");
//		hong.setName("홍길동");
//		hong.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("hong"));
//		hong.setRole("admin");
//		dao.save(hong);
		
//		Member gang = new Member();
//		gang.setId("gang");
//		gang.setName("강감찬");
//		gang.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("gang"));
//		gang.setRole("user");
//		dao.save(gang);
//		
//		Member wang = new Member();
//		wang.setId("wang");
//		wang.setName("왕건");
//		wang.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("wang"));
//		wang.setRole("user");
//		dao.save(wang);
		
//		Member yoo = new Member();
//		yoo.setId("yoo");
//		yoo.setName("유관순");
//		yoo.setPwd(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("yoo"));
//		yoo.setRole("user");
//		dao.save(yoo);

