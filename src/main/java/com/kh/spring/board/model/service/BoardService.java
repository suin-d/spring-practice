package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	// 1. 게시판 리스트 조회용 서비스
	int selectListCount();
	ArrayList<Board> selectBoardList(PageInfo pi);
	
	// 2. 게시판 작성용 서비스
	int insertBoard(Board b);

	// 3. 게시판 상세조회용 서비스
	int increaseCount(int bno);
	Board selectBoard(int bno);
	
	// 4. 게시판 수정용 서비스
	int updateBoard(Board b);
	
	// 5. 게시판 삭제용 서비스 
	int deleteBoard(int bno);
	
	// 6. 게시판에 딸린 댓글 리스트 조회용 서비스 
	ArrayList<Reply> selectReplyList(int bno);
	
	// 7. 댓글 작성용 서비스 
	int insertReply(Reply r); // 어떤 사용자, 어떤 댓글, 어쩐 게시글인지 Reply 객체 넘어와서 매개변수로 받아주기
	
	
}
