package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) {
		// 제목, 작성자, 내용은 요청 시 전달되는 값 Board라는 객체에 세팅해주면 알아서 담김 => 물론 key값을 필드명으로 지어줘야함 
		// but, 첨부파일은 별도로 반환해서 처리해야
		
		// System.out.println(b); // null값 담김 
		// System.out.println(upfile.getOriginalFilename()); // 첨부파일 선택해도 null 
		// 첨부파일 업로드 기능을 실행하기 위해서는 라이브러리 2개 추가, 빈으로 등록해야함 
		
		// 전달된 파일이 있을 경우 => 서버에 업로드 => 원본명, 저장경로 b에 담기 
		if(!upfile.getOriginalFilename().equals("")) { // 빈 문자열이 아닐 경우 
			
			/* 
			// 파일을 업로드 시킬 폴더의 물리적인 경로
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
			// session객체로부터 getServletContext().getrealpath()라는 메소드 통해 결로 알아옴 
					// "/" => webapp 
			
			// 어떤 이름으로 업로드 시킬건지 수정명 (changeName) 
			String originName = upfile.getOriginalFilename(); // ocean.jpg // 확장자 추출하기 위해 셋팅 
			
			String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()); 
			// simplaeDateFormat객체 생성 시 포맷 지정 
			int ranNum = (int)(Math.random() * 90000 + 10000); 
			// 10000에서부터 99999까지의 랜덤값 
			String ext = originName.substring(originName.lastIndexOf("."));
			// .의 위치를 찾아 substring 추출하면 .포함한 마지막값까지

			String changeName = currentTime + ranNum + ext; // 2020120217323045236.jpg
		
		
				try {
					upfile.transferTo(new File(savePath + changeName));
				} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
				}
				// 게시글 수정할 때도 똑같은 레파토리 
				// 공통 코드가 기술될 것 같을 땐 공통 메소드로 만들어서 따로 빼놓음!  
			*/
			String changeName = saveFile(session, upfile);
			// 이 메소드 호출 시 만들어진 changeName이 필요하다 => 반환된 changeName 담아주기  
			
			b.setOriginName(upfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName); // "resources/uplodaFiles/2020120217323045236.jpg"
		}
		

		int result = bService.insertBoard(b); // 케바케로 originName, changeName 담겨있고 없다면 null값 담겨 있을 것 
	
		if(result>0) {//성공했을 경우
			session.setAttribute("alertMsg", "글 작성 성공했습니다!");
			// return "board/boardListView"; XXX 다시 포워딩 하면 안되고 url 재요청해야 selectBoardList 이 과정을 다  거침!!
			return "redirect:list.bo";
			
			
		}else {// 실패했을 경우
			model.addAttribute("errorMsg", "게시글 작성 실패!");
			return "common/errorPage";
		}
	}
	

	
	@RequestMapping("detail.bo")
	public String selectBoard(int bno, Model model) {// bno이라는 키값으로 넘어온 게 바로 담김
		// 먼저 이 게시글을 찾아 조회수 1 추가 
		
		int result = bService.increaseCount(bno);
		
		if(result>0) {// 유효한 게시글
			
			Board b = bService.selectBoard(bno);
			// bno과 일치하는 게시글 Board객체에 담김 
			
			model.addAttribute("b", b); // b라는 키값으로 board객체 담기 
			
			return "board/boardDetailView"; 
			
		}else { // 유효하지 않은 게시글 => 셀렉할 필요 X 
			
			model.addAttribute("errorMsg", "존재하지 않는 게시글이거나 삭제된 게시글입니다.");
			return "common/errorPage";
		}
		
		
	}
	
	

	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, String fileName, HttpSession session, Model model) {
		
		int result = bService.deleteBoard(bno);
		
		if(result > 0) { // 기존의 파일 찾아서 삭제 => 게시글 리스트 페이지 재요청
			
			if(!fileName.equals("")) { // 기존의 첨부파일이 있었을 경우
				new File(session.getServletContext().getRealPath(fileName)).delete();
			}
			
			session.setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다.");
			return "redirect:list.bo";
			
		}else {
			
			model.addAttribute("errorMsg", "게시글 삭제 실패");
			return "common/errorPage";
			
		}
		
	}


	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		
		//Board b = bService.selectBoard(bno);
		//model.addAttribute("b", b);
		model.addAttribute("b", bService.selectBoard(bno));
		return "board/boardUpdateForm";
		
	}
	
	
	@RequestMapping("update.bo")
	public String updateBoard(Board b, MultipartFile reupFile, HttpSession session, Model model) {
		
		if(!reupFile.getOriginalFilename().equals("")) { // 새로 전달된 첨부파일 있을 경우
			
			// 만약에 기존의 첨부파일이 있었을 경우 => 삭제
			if(b.getOriginName() != null) {
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete();
			}
			
			// 새로 전달된 첨부파일 => 업로드
			String changeName = saveFile(session, reupFile);
			b.setOriginName(reupFile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);
			
		}
		
		/*
		 * Board b 객체의 필드 
		 * 
		 * 1. 새로 첨부된 파일 X, 기존의 첨부파일 X
		 *    --> originName:null, changeName:null
		 * 
		 * 2. 새로 첨부된 파일 X, 기존의 첨부파일 O
		 *    --> originName:기존의 첨부파일 원본명, changeName:기존의 첨부파일 저장경로
		 *    
		 * 3. 새로 첨부된 파일 O, 기존의 첨부파일 X
		 * 	  --> 새로운 첨부파일 업로드 후
		 *    --> originName:새로운 첨부파일 원본명, changeName:새로운 첨부파일 저장경로
		 * 
		 * 4. 새로 첨부된 파일 O, 기존의 첨부파일 O
		 *    --> 기존의 첨부파일 삭제 후
		 *    --> 새로운 첨부파일 업로드 후
		 *    --> originName:새로운 첨부파일 원본명, changeName:새로운 첨부파일 저장경로
		 * 
		 */
		
		int result = bService.updateBoard(b);
		
		if(result > 0) { // 게시글 수정 성공 => 상세보기 페이지 재요청(detail.bo)
			
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정됐습니다");
			return "redirect:detail.bo?bno=" + b.getBoardNo();
			
		}else { // 게시글 수정 실패 
			model.addAttribute("errorMsg", "게시글 수정 실패");
			return "common/errorPage";
		}
		
		
	}
	
	
	
	
	// 첨부파일 업로드 시켜주는 메소드 
	public String saveFile(HttpSession session, MultipartFile upfile) {
		
		// 파일을 업로드 시킬 폴더의 물리적인 경로
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		// session객체로부터 getServletContext().getrealpath()라는 메소드 통해 결로 알아옴 
				// "/" => webapp 
		
		// 어떤 이름으로 업로드 시킬건지 수정명 (changeName) 
		String originName = upfile.getOriginalFilename(); // ocean.jpg // 확장자 추출하기 위해 셋팅 
		
		String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()); 
		// simplaeDateFormat객체 생성 시 포맷 지정 
		int ranNum = (int)(Math.random() * 90000 + 10000); 
		// 10000에서부터 99999까지의 랜덤값 
		String ext = originName.substring(originName.lastIndexOf("."));
		// .의 위치를 찾아 substring 추출하면 .포함한 마지막값까지

		String changeName = currentTime + ranNum + ext; // 2020120217323045236.jpg
	
	
			try {
				upfile.transferTo(new File(savePath + changeName));
			} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
			}
			// 게시글 수정할 때도 똑같은 레파토리 
			// 공통 코드가 기술될 것 같을 땐 공통 메소드로 만들어서 따로 빼놓음 => BoardController 
		
			return changeName;
			// 호출한 곳으로 changeName 리턴! (반환형 String)
	}
	
}
