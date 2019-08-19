package project.suhbuway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import project.suhbuway.dto.Complain;
import project.suhbuway.service.complain.ComplainService;

@Controller
public class ComplainController {
	@Autowired
	private ComplainService service;
	
	/*
	 * ��� �Խñ� �˻�
	 * */
	@RequestMapping("/board/complainList")
	public String selectAll(HttpServletRequest request) {
		 List<Complain> list = service.selectAll();
		 request.setAttribute("list",list);
		return "board/complainList";
	}
	
	/*
	 * �Խ��� ��ȣ�� �˻�
	 * */
	@RequestMapping("/board/Read/{complainId}")
	public ModelAndView selectByCompainId(@PathVariable int complainId) {
		Complain complain=service.selectByCompainId(complainId);
		return new ModelAndView("board/complainRead","complain",complain);
	}
	
	/*
	 * �۾��� ��
	 * */
	@RequestMapping("/board/write")
	public String insertForm() {
		return "board/complainWrite";
	}
	
	/*
	 * �Խñ� �Է�
	 * */
	 @RequestMapping("/board/insert")
	 public String insert(Complain complain) {
		 System.out.println(complain.getComplainId());
		 System.out.println(complain.getUserId());
		 System.out.println(complain.getTitle());
		 System.out.println(complain.getContent());
		 System.out.println(complain.getDate());
		 
		 return "redirect:complainList";
	 }
	 
	 /*
	  * �����ϱ�
	  * */
	 @RequestMapping("/board/delete")
	 public String delete(String userId, int complainId) {
		 service.delete(userId, complainId);
		 return "redirect:complainList";
	 }
	
}
