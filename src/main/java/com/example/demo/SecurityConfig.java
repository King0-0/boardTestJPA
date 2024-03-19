package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//비밀번호 암호화하기  //메인메소드에 갖다넣어도 됨
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean					//메소드 이름이 아이디 역할
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
		.requestMatchers("/", "/member/login").permitAll()
		.requestMatchers("/admin/**").hasRole("admin") //admin의 모든 요청
		.anyRequest().authenticated();	//나머지는 로그인하면 다 사용
		
		//로그인 시 list로 연결하기
		http.formLogin().loginPage("/member/login").permitAll()
		.defaultSuccessUrl("/board/list/1");
		//로그인 성공시 보드리스트의 첫번째 페이지로
		
		
		//로그아웃 설정
		http.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.invalidateHttpSession(true)
		.logoutSuccessUrl("/member/login");	//로그아웃되면 로그인창으로 가기
		
		http.httpBasic();	//나머지는 http 기본으로 따른다
		
		return http.build();
	}
}
