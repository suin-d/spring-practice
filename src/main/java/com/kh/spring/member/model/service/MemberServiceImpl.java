package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

@Service 
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private MemberDao mDao;

	@Override
	public Member loginMember(Member m) {
		
		
		//Member loginUser = mDao.loginMember(sqlSession, m);
		//return loginUser;
		return mDao.loginMember(sqlSession, m);
		// 우리가 생성한 객체가 아니라 Spring이 SqlSession 관련한 모든 생명주기를 관리하기 때문에 결과만 반환!
	
	}

	@Override
	public int insertMember(Member m) {
	
		return mDao.insertMember(sqlSession, m);
		// sqlSession과 insert할 정보 담긴 맴버 객체 리턴
				
	}

	@Override
	public int updateMember(Member m) {
		
		return mDao.updateMember(sqlSession, m);
		
	}

	@Override
	public int deleteMember(String userId) {
		
		return mDao.deleteMember(sqlSession, userId);
		
		
	}

	@Override
	public int idCheck(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 
}
