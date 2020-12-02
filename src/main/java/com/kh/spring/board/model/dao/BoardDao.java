package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;

@Repository 
public class BoardDao {
	// int = 처리된 행수가 아니고 조회된 함수 
	public int selectListCount(SqlSessionTemplate sqlSession) {
		
		return sqlSession.selectOne("boardMapper.selectListCount");
		
	}
	
	public ArrayList<Board> selectBoardList(SqlSessionTemplate sqlSession, PageInfo pi){
		// 각각의 행들은 board 객체에 담겨있고, 여러개의 board 객체가 이곳에 
		
		// 마이바티스는 페이징 처리를 위해 RowBounds라는 객체 제공
		// RowBounds => 몇개의 게시글 건너(offset)띄고 몇개조회할껀지에 대해 정의
		
		// * offset : 이번에 조회할때 몇개의 게시글 건너띄고 조회할지에 대한 값
		// ex) boardLimit :5
		// currentPage = 1		1 ~ 5		"0"개의 게시글 건너띄고 1부터 5개 조회
		// currentPage = 2 		6 ~ 10		"5"개의 게시글 건너띄고 6부터 5개 조회
		// currentPage = 3		11 ~ 15		"10"개의 게시글 건너띄고 11부터 5개 조회
		// currentPage = 4		16 ~ 20		"15"개의 게시글 건너띄고 16부터 5개 조회
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList", null, rowBounds); // null자리에 원래는 완성시킬 값, 하지만 없을 땐 null 
	}
	
	
	public int insertBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.insert("boardMapper.insertBoard", b);
	}

	
	public int increaseCount(SqlSessionTemplate sqlSession, int bno) {
		return sqlSession.update("boardMapper.increaseCount", bno);
	}

	public Board selectBoard(SqlSessionTemplate sqlSession, int bno) {
		return sqlSession.selectOne("boardMapper.selectBoard", bno);
	}
}

