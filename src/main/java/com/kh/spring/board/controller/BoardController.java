package com.kh.spring.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller 
public class BoardController {

	@Autowired
	private BoardService bService;
	
	
	@RequestMapping("list.bo") // list.bo?currentPage=xx  
	public String selectBoardList(@RequestParam(value="currentPage", defaultValue="1") int currentPage,
								Model model) { // currentPage라는 키값으로 넘어온 값이 알아서 parsing 되어 담김 
		// 요청시 전달하는 값중 키값: value영역에 기술, 없다면 defaultValue로 기본값 설정  
	
		// System.out.println(currentPage); // 1
		
		// * listCount : 총 게시글 갯수
		int listCount = bService.selectListCount();
		
		// * pageLimit : 한 페이지 하단에 보여질 페이징바의 페이지 최대 갯수 => 10
		// * boardLimit : 한 페이지에 보여질 게시글 최대 갯수 => 5
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = bService.selectBoardList(pi);
		
		model.addAttribute("pi", pi);
		// pi라는 키값으로 pi 담기
		model.addAttribute("list", list);
		// list라는 키값으로 ArrayList담아서 포워딩 
		
		return "board/boardListView";
		// 문자열 리턴 -> 반환형 바꾸기 void=>string
	}
	
}
