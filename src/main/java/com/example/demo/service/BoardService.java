package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BoardDAO;
import com.example.demo.entity.Board;

import lombok.Setter;

@Service
@Setter
public class BoardService {
	@Autowired
	private BoardDAO dao;

	
	//페이징처리
	public int getTotalRecord() {
		return (int)dao.count();
	}
	
	//이건 왜고치는건지 모르겠는데 암튼 고쳤음
	public Board getBoard(int no) {
//		return dao.getOne(no);
		return dao.findById(no).get();
	}
	
	public List<Board> findAll(int start, int end){
		//return dao.findAll();
		// 게시글과 답글 정렬
		return dao.selectAll(start,end);
	}
	
	
	//230206
	//특정 사용자가 작성한 게시물만 가져오기
	public List<Board> findById(String id){
		return dao.findById(id);
	}
	
	
	
	public int getNextNo() {
		return dao.getNextNo();
	}
	
	public void insert(Board b) {
		dao.insert(b);
	}

	// 답글(답글 추가되면 번호에 +1하기)
	public void updateStep(int b_ref, int b_step) {
		dao.updateStep(b_ref,b_step);
	}
	
	public Board findById(int no) {
		return dao.findById(no).get();
	}

	
	//수정하기
	public int updateBoard(Board b) {
		return dao.updateBoard(b);
	}
	
	
	//삭제하기
	public int deleteBoard(int no, String pwd) {
		return dao.deleteBoard(no,pwd);

	}
}