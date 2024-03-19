package com.sist.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.ImageDAO;
import com.sist.dao.ProductDAO;
import com.sist.vo.ImageVO;
import com.sist.vo.ProductVO;

public class DetailProductAction implements SistAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		ImageDAO dao = ImageDAO.getInstance();
		//int pno = Integer.parseInt(request.getParameter("pno"));
		int pno = 760001;
		ImageVO img = dao.listImage(pno);
		request.setAttribute("imageList", img);
		
		ProductDAO pdao = ProductDAO.getInstance();
		ProductVO pd = pdao.detailProduct(pno);
		request.setAttribute("pd", pd);
		
		return "detailProduct.jsp";

	}

}
