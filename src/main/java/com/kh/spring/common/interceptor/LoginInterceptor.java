package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 * Interceptor 사용 예 
 * 
 * 로그인 기능 구현 시 사용 (회원만 요청가능한 페이지일 경우 해당 사용자가 회원인지/비회원인지 파악 후 제어) 
 * 로그인한 사용자의 권한 체크 
 * 
 */

public class LoginInterceptor extends HandlerInterceptorAdapter{


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		// 이 메소드는 url 요청시 HandlerMapping을 통해서 해당 Controller 구동되기 이전에 낚아채서 실행됨!! 
		// 이때 true 리턴될 경우 => 기존의 그 요청 흐름대로 구동시키겠다는 의미 
		//    false 리턴될 경우 => 기존의 요청 흐름대로 실행될 Controller를 구동시키지 않겠다는 의미! 
		
		HttpSession session = request.getSession();
		// 현재 로그인되어있지 않을 경우 => 알람 출력, 메인페이지 요청
		if(session.getAttribute("loginUser") == null ) {
			
			session.setAttribute("alertMsg", "로그인 후 이용가능한 서비스 입니다. 로그인 먼저 진행해주세요.");
			response.sendRedirect(request.getContextPath());
			
			return false;
		}else {
		// 현재 로그인한 사용자만 => 기존의 흐름대로 진행되게끔

			return true;
		}
	}
	
	// Interceptor 등록해야하고! 등록 시 어떤 url로 요청시 이 Interceptor 거쳐가게할 것인지 직접 작성!
	// => DispatcherServlet 클래스와 관련된 servlet-context.xml 문서에 등록
	
}
