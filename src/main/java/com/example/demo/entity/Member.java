package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Setter
@Getter
@Entity
//@Data		setter/getter추가
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
public class Member {
	
	@Id
	private String id;
	private String name;
	private String pwd;
	private String role;
	

	
	//양방향 연관관계 설정시에 
	//로그인 할 때에 그 회원이 등록한 게시물 목록까지 
	//갖고 올때에 시큐리티 환경설정에서는 
	//아래를 주석으로 처리하여 단방향 연관관계 설정으로 합니다.
	
//	//board클래스에 member라는 이름의 변수를 참조하도록 하겠다
//	//board에 만들겠다는 소리		//즉시 읽어들이기(EAGER) / 필요할때 읽어들이기(LAZY)
	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER,
			cascade = CascadeType.REMOVE)	
	private List<Board> boards;
	//회원(부모)가 삭제가 되면 그 계정이 작성한 게시물도 연쇄적으로 삭제	
	
}
