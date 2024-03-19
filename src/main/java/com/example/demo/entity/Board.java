package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "board")
public class Board {
	@Id
	private int no;
	private String title;
//	private String writer;
	
	@ManyToOne	  //board에는 writer이지만, member에서는 id 참조한다는 소리
//	@JoinColumn(name="wirter", referencedColumnName = "id")
	@JoinColumn(name="id", insertable = true, updatable = true)
	private Member member;
	
	private String pwd;
	private String content;
	private String regdate;
	private int hit;
	@Transient
	private MultipartFile uploadFile;
	private String fname;
	private int b_ref;
	private int b_level;
	private int b_step;
}