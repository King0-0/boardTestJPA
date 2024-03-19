package com.example.demo.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;

import jakarta.transaction.Transactional;

@Repository
public interface BoardDAO extends JpaRepository<Board, Integer> {
	
	//writer > id로 수정
	//게시글+답글 정렬 , 페이징
	@Query(value = "select no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select rownum n,no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select * from board order by b_ref desc, b_step)) a "
			+ "where a.n between ?1 and ?2", nativeQuery = true)
	public List<Board> selectAll(int start, int end);
	
	
	
	
	//230206
	//특정 사용자가 작성한 게시물만 가져오기
	@Query(value="select * from board where id=?1 order by b_ref desc,b_step", nativeQuery = true)
	public List<Board> findById(String id);
	
	
	
	
	
	//게시글 답글 메소드
	@Modifying
	@Transactional
	@Query(value="update Board b set b.b_step = b.b_step+1 where b.b_ref=?1 and b.b_step>?2",nativeQuery = true)
	public void updateStep(int b_ref, int b_step);
	// b.b_ref=?1  첫번째 변수라는 뜻,  b.b_step>?2  두번째 변수라는 뜻
	
	
	@Query(value = "select nvl(max(no),0) + 1 from board", nativeQuery = true)
	public int getNextNo();
	
	@Modifying
	@Query(value = "insert into Board b(b.no,b.title,b.id,b.pwd,b.content,b.regdate,b.hit,b.fname,b.b_ref,b.b_level,b.b_step) values(:#{#b.no},:#{#b.title},:#{#b.member.id},:#{#b.pwd},:#{#b.content},sysdate,0,:#{#b.fname},:#{#b.b_ref},:#{#b.b_level},:#{#b.b_step})", nativeQuery = true)
	@Transactional
	public void insert(Board b);

	
	// 글 제목, 글 내용, 파일명 수정하기		:b.no
	@Modifying
	@Query(value = "update Board b set "
			+ "b.title=:#{#b.title},"
			+ "b.content=:#{#b.content},"
			+ "b.fname=:#{#b.fname} "
			+ "where b.no=:#{#b.no} and b.pwd=:#{#b.pwd}", 
				nativeQuery = true)
	@Transactional
	public int updateBoard(Board b);

	
	
	//삭제하기
	@Modifying
	@Query(value = "delete board where no=?1 and pwd = ?2", nativeQuery = true)
	@Transactional
	public int deleteBoard(int no, String pwd);
	
	
	@Query(value = "select count(*) from board where id=?1", 
			nativeQuery = true)
	public int getCount(String id);

	@Query(value = "select no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select rownum n,no,title,id,pwd,content,regdate,hit,fname,b_ref,b_level,b_step "
			+ "from (select * from board where id = ?3 order by b_ref desc, b_step)) a "
			+ "where a.n between ?1 and ?2", nativeQuery = true)
	public List<Board> findAll(int start, int end, String id);

}