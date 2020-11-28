package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller	// 어노테이션을 붙여주면 빈 스캐닝을 통해 자동으로 빈으로 등록
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	
	// * 요청시 전달값(파라미터)을 전송받는 방법 (요청시 전달되는 값들 처리방법) 
	/*
	 * 1. HttpServletRequest를 통해 전송받기 (기존의 jsp/servlet 때의 방식)
	 */
	/*
	@RequestMapping("login.me") // HandlerMapping으로 등록
	public void loginMember(HttpServletRequest request) {
		
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
	}
	*/
	
	/*
	 * 2. @RequestParam 어노테이션 방식 (내부적으로 HttpServletRequest로 부터 작업되고 있을 것)
	 * 
	 */
	/*
	@RequestMapping("login.me")
	public void loginMember(@RequestParam(value="id", defaultValue="ssss") String userId, 
							@RequestParam("pwd") String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션 생략하는 방법
	 * 
	 *    요청시 전달되는 키값과 받아주고자하는 매개변수명과 일치하면
	 *    해당 그 매개변수에 값이 바로 담기게 됨!!
	 *    
	 *    어노테이션을 생략했기 때문에 defaultValue같은 속성은 이용할 수 없음
	 * 
	 */
	/*
	@RequestMapping("login.me")
	public void loginMember(String userId, String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		
	}
	*/
	
	/*
	 * 4. 바로 객체의 필드에 담는 방법
	 * 
	 * 커맨드 객체 방식이라고 얘기함!
	 * 스프링 컨테이너가 해당 객체를 기본생성자로 생성한 후
	 * setter메소드로 값을 주입해줌!
	 * 
	 * (주의 : 반드시 name의 속성값(키값)을 내가 담고자하는 객체의 필드명으로 해야됨!)
	 */
	@RequestMapping("login.me")
	public void loginMember(Member m) {
		
		//System.out.println("ID : " + m.getUserId());
		//System.out.println("PWD : " + m.getUserPwd());
		
		//MemberService mService = new MemberServiceImpl();
		// 직접 객체 생성(코드로써 new 키워드)을 하게 되면 => 결합도 높음!!
		// 결합도가 높을 때 발생할 수 있는 문제
		// 1. 클래스명이 변경됐을 경우 => 에러 발생
		// 2. 매번 새로이 생성됐다고 소멸되는 과정이 반복 (즉 , 10000건의 요청이 들어오면 10000개의 객체가 생성될꺼임)
		
		// 위의 문제점을 해소하고자 한다면 => 결합도 낮춰주면 됨! (직접 객체 생성 안하면 됨!)
		// 스프링 컨테이너가 해당 객체를 관리할 수 있도록 "빈으로 등록"
		// DI(의존성 주입을 통해서 생성된 객체 받아다 쓸꺼임)
		
		
		
		Member loginUser = mService.loginMember(m);
		
		
	}
	
	
	
	
	

}








