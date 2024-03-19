package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.BoardDAO;
import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class BoardController {
	
	@Autowired
	private MemberDAO dao;
	
	@Autowired
	private BoardService bs;
	
	@Autowired
	private MemberService ms;
	
	//페이징 변수 선언
	public static int pageSIZE = 5;
	public static int totalRecord;
	public static int totalPage;
	
	
	//////// 삭제하기(첨부파일이 있다면 삭제)
	@GetMapping("/board/delete/{no}")
	public String deleteForm(@PathVariable("no") int no,Model model) {
		//삭제할 글 정보를 모델에 추가
		model.addAttribute("b",bs.getBoard(no));
		//model.addAttribute("no",no));
		return "/board/delete";
	}
	
	@PostMapping("/board/delete")
	public String deleteSubmit(int no, String pwd, HttpServletRequest request) {
		System.out.println("deleteSubmit 동작함");
		System.out.println("no:"+no);	
		System.out.println("pwd:"+pwd);
		String view = "redirect:/board/list/1";
		
		//삭제시키기 전에 원래 파일이름 알아놨다가지워줘야함
		String oldFname = bs.getBoard(no).getFname();
		String path = request.getServletContext().getRealPath("images");
		
		//암호가 일치하면 삭제할것
		int re = bs.deleteBoard(no,pwd);
		if(re == 1 && oldFname != null && !oldFname.equals("")) {
			File file =  new File(path+"/"+oldFname);
			file.delete();
		}
		return view;
	}
	
	
	
	//////// 수정하기
	@GetMapping("/board/update/{no}")
	public String updateForm(@PathVariable("no") int no, Model model) {
		Board b = bs.getBoard(no);
		System.out.println(b);
		model.addAttribute("b", b);
		return "/board/update";
	}
	
	@PostMapping("/board/update")
	public String updateSubmit(Board b, HttpServletRequest request) {
		String oldFname = b.getFname();
		String view = "redirect:/board/list/1";
		String path = request.getServletContext().getRealPath("images");
		System.out.println("path:"+path);
		MultipartFile uploadFile = b.getUploadFile();
		String fname = null;
		fname = uploadFile.getOriginalFilename();
		
		//기존에 파일이 첨부되어 있다면
		if(fname != null  && !fname.equals("")) {
			try {
				FileOutputStream fos = 
						new FileOutputStream(path + "/" +fname);
				// 카피 메소드(파일복사) 사용
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
			} catch (Exception e) {
				System.out.println("예외발생 : "+e.getMessage());
			}
			b.setFname(fname);
		}
		
		//3개만 수정할것임(제목, 내용, 첨부파일)
		//수정성공여부
		int re = bs.updateBoard(b);
		if(re == 1 && 
				fname != null  && !fname.equals("") && 
				oldFname != null && !oldFname.equals("")) {	//수정성공 + 사진도 수정하기
			File file = new File(path +  "/" + oldFname);
			file.delete();
		}
		return view;
	}
	
	
	//////// 등록하기
	@GetMapping("/board/insert")
	public void insertForm(@RequestParam(value = "no", defaultValue = "0") int no, Model model) {
		//디폴트값주기
		model.addAttribute("no",no);
	}
	
	//등록하기 + 파일추가
	@PostMapping("/board/insert")
	public String insertSubmit(Board b, HttpServletRequest request) {
		
		//파일 경로
		String path = request.getServletContext().getRealPath("images");
		System.out.println("path:"+path);
		MultipartFile uploadFile = b.getUploadFile();
		String fname = null;
		fname = uploadFile.getOriginalFilename();
		// 파일을 등록한다면 아래와 같이 동작
		if(fname != null  && !fname.equals("")) {
			try {
				byte []data = uploadFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path + "/"+ fname);
				//파일업로드하면 복사하여 저장
				fos.write(data);
				fos.close();
			}catch (Exception e) {
				System.out.println("예외발생:"+e.getMessage());
			}
		}
		b.setFname(fname);
		
		
		int pno = b.getNo();
		String view = "redirect:/board/list/1";
		int no = bs.getNextNo();
		
		//일단 새 글이라고 보고 처리한것
		int b_ref = no;
		int b_level = 0;
		int b_step =  0;
		
		//답글달기 관련 처리
		//부모글에 대한 정보 가져와서 설정
		if(pno != 0) {
			Board p = bs.getBoard(pno);
			b_ref = p.getB_ref();
			b_level = p.getB_level();
			b_step = p.getB_step();
			
			//이미 담겨있는 답글의 스텝을 증가시켜
			bs.updateStep(b_ref, b_step);
			b_level++;
			b_step++;
		}
		
		b.setNo(no);
		b.setB_ref(b_ref);
		b.setB_level(b_level);
		b.setB_step(b_step);
		
		bs.insert(b);
		
		return view;
	}
	
	
	//페이지 번호 주기
	//전체 목록 보기
	//시큐리티 환경설정파일에서 로그인 성공했을 때 이동할 url인 이곳에서
	//로그인한 회원의 정보를 상태유지함
	@GetMapping("/board/list/{pageNUM}")			//생략해도 된다는 뜻
	public String list(HttpSession session, Model model, 
			@PathVariable("pageNUM") int pageNUM,
			@RequestParam(value="id", required=false) String id) {	
		//로그아웃전까지 상태유지를 위해 session변수를 매개변수로 추가함.
		Authentication authentication=
				SecurityContextHolder.getContext().getAuthentication();
		
		//이 Authentication객체를 통해서 로그인한 user객체를 갖고옴.
		User user = (User)authentication.getPrincipal();
		
		//User를 통해서 현재 로그인한 회원의 아이디를 갖고옴
		//@RequestParam으로 id를 받아와서 아래에 String을 뺐음.
		id = user.getUsername();
		
		//로그인한 사용자의 정보를 세션에 저장
		Member m = ms.findByID(id);
		session.setAttribute("loginUSER", m);
		
		
		
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.getTotalRecord();
		totalPage = (int)Math.ceil(totalRecord/(double)pageSIZE);
		
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start + pageSIZE-1;
		if(end>totalRecord) {
			end = totalRecord;
		}
		
		
		
		//사용자 id가 주어지면 해당 글만 조회하기
		if(id != null && !id.isEmpty()) {
			model.addAttribute("list",bs.findById(id));
		}else {
			model.addAttribute("list",bs.findAll(start,end));
		}
		
		
//		model.addAttribute("list",bs.findAll(start,end));
		model.addAttribute("totalPage",totalPage);
		return "/board/list";
	}
	
	
	//namespace 요청할때는 view를 지정해줘야함 String or ModelAndView
	@GetMapping("/board/detail/{no}")
	public ModelAndView detail(@PathVariable("no") int no,Model model) {
		model.addAttribute("b", bs.getBoard(no));
		ModelAndView mav = new ModelAndView("/board/detail");
		return mav;
		// 혹은 String을 쓰고 return "/board/detail";
	}
	
}