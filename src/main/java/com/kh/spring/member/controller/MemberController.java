package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller	// 어노테이션을 붙여주면 빈 스캐닝을 통해 자동으로 빈으로 등록
public class MemberController {
	
	@Autowired
	private MemberService mService;
	// 이 코드가 실행되는 순간 객체 생성해서 주입
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	
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
							// 이런 키값으로 담아온 게 없다면, 기본값으로 셋팅
							@RequestParam("pwd") String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션 생략하는 방법
	 * 
	 *    요청시 전달되는 키값(name="userId", name="userPwd")과 받아주고자하는 매개변수명과 일치하면
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
//	@RequestMapping("login.me")
//	public void loginMember(Member m, HttpSession session) {
//		
//		//System.out.println("ID : " + m.getUserId());
//		//System.out.println("PWD : " + m.getUserPwd());
//		
//		//MemberService mService = new MemberServiceImpl();
//		// 직접 객체 생성(코드로써 new 키워드)을 하게 되면 => 결합도 높음!!
//		// 결합도가 높을 때 발생할 수 있는 문제
//		// 1. 클래스명이 변경됐을 경우 => 에러 발생
//		// 2. 매번 새로이 생성됐다고 소멸되는 과정이 반복 (즉 , 10000건의 요청이 들어오면 10000개의 객체가 생성될꺼임)
//		
//		// 위의 문제점을 해소하고자 한다면 => 결합도 낮춰주면 됨! (직접 객체 생성 안하면 됨!)
//		// 스프링 컨테이너가 해당 객체를 관리할 수 있도록 "빈으로 등록"
//		// DI(의존성 주입을 통해서 생성된 객체 받아다 쓸꺼임)
//		
//		Member loginUser = mService.loginMember(m);
//		// 요청에 대한 성공/실패 유무 null값으로 판단 
//		
//		if(loginUser != null) {// 로그인 성공
//			// 세션에 로그인한 회원객체 담았을 것 (request.getSession으로 => 이제는 아예 세션 객체를 받아오기) 
//			session.setAttribute("loginUser", loginUser);
//			// 메인페이지 url로 재요청 
//			
//		}else {// 로그인 실패
//			
//			// request에 에러문구 담아서 
//			
//			// 에러페이지로 포워딩 
//			
//		}
//	}
	
	// 이제부터는 요청 처리 후 응답페이지로 어떻게 포워딩하는지
	// 이때 응답데이터가 있다면 어디에 어떤식으로 담으면 되는지에 대해!
	/*
	 * 1. Model 이라는 객체를 사용하는 방법
	 * 
	 * Model이라는 객체를 이용해서 응답뷰에 전달하고자 하는
	 * 응답데이터를 맵 형식 (key, value) 으로 담을 수 있음
	 * 
	 *  scope는 requestScope이다. == 포워딩된 응답페이지에서만 꺼내쓸 수 있다.
	 *  단, setAttribute가 아닌 addAttribute("키", 밸류)
	 * 
	 */
//	@RequestMapping("login.me")
//	public String loginMember(Member m, HttpSession session, Model model) {
//		
//		Member loginUser = mService.loginMember(m);
//		
//		if(loginUser != null) { // 로그인 성공
//			session.setAttribute("loginUser", loginUser);
//			
//			return "redirect:/";
//			// redirect: => 포워딩이 아니라 url 재요청으로 인식 
//			
//		}else { // 로그인 실패 
//			model.addAttribute("erroMsg", "로그인 실패!");
//			
//			// //WEB-INF/views/common/errorPage.jsp 페이지로 포워딩 원함
//			return "common/errorPage";
//			
//			// return된 문자열을 가지고 어떻게 포워딩? 
//			// 우리가 url요청 시 dispatcher 서블릿 구동 -> servlet-context.xml 실행
//			// 이곳에서 이미 등륵되어있는 viewResolver라는 빈이 실행되어
//			// 리턴된 문자열의 앞에는 /WEB-INF/views/가 붙고 
//			// 뒤에는 .jsp가 붙어서 해당 뷰로 포워딩 되는 원리!!
//		}
//		
//	}
//	

	/*
	 * 2. ModalAndView 객체를 사용하는 방법
	 * 
	 * 첫번째 방법의 Model이라는 객체는 응답하고자 하는 데이터를 맵형식(key, value)으로 담는 공간이라고 한다면
	 * View는 포워딩할 그 뷰페이지에 대한 정보를 담을 수 있는 영역 
	 * 
	 * ModelAndView는 이 두가지를 합쳐놓은 객체 
	 * 즉, 응답데이터도 담을 수 있고, 응답페이지에 대한 정보도 담을 수 있는 공간 
	 * 
	 * 
	 */
	@RequestMapping("login.me")
	public ModelAndView loginMember(Member m, HttpSession session, ModelAndView mv) {
		
		Member loginUser = mService.loginMember(m); 
		// loginUser.getUserPwd() == 디비로부터 아이디를 가지고 조회했을 때의 암호문이 담겨 있을 것(암호문)
		// m.getUserPwd() == 로그인 요청 시 입력했던 비밀번호 값(평문)
		
		if(loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) { // 로그인 성공
			// matches를 이용해서 사용자가 입력한 날것 그대로의 비밀번호, 암호문 두개의 값을 던지면 '복호화' 
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
		}else { // 로그인 실패 
			mv.addObject("errorMsg", "로그인 실패!");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	
	@RequestMapping("logout.me")
	public String logloutMember(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		// WEB-INF/views/member/memberEnrollForm.jsp
		return "member/memberEnrollForm";
	}

	@RequestMapping("insert.me")
	public String insertMember(Member m, Model model, HttpSession session) {
		// 담기는 원리 
		// key&value 세트로, 어떤 필드에 담고 싶은지 필드명을 name속성으로 key값 지정하면 거기 담김 
	
		// System.out.println(m);
		// 각 필드에 잘 담겼는지 확인 
	
		/*
		 * 문제1. 한글 값들이 깨져있음 => 인코딩 해야함 
		 * 해결1. 스프링에서 제공하는 인코딩 필터 등록 => web.xml에 
		 * 		 어떤 요청이든 해당 인코딩 필터를 거쳐가도록!  
		 * 
	 	 * 문제2. 만약에 나이를 입력하지 않고 요청을 하게 되면 "" 빈문자열이 넘어오기 때문에 파싱x
		 *       만약에 20이라는 값을 입력하고 요청하게 되면 "20"이 자동으로 파싱되서 int형변수에 담김
		 *       단, "" 은 파싱이 될수 없음! => NumberFormatException 발생
		 * 해결2. int age => String age 타입 바꾸기!
		 *
		 * 
		 * 문제3. 비밀번호가 사용자가 입력한 있는 그대로의 평문임!!
		 * 해결3. 암호화 과정을 거쳐서 db에 저장해야함!
		 * 
		 * 		* bcrypt방식
		 * 		    사용자가 입력한 비밀번호(평문) + salt값(랜덤값) -----> 암호문 
		 * 
		 * 		  BCryptPasswordEncoder (스프링 시큐리티 모듈에서 제공함) 
		 * 
		 */
		
		
		// System.out.println("암호화 전(평문): " + m.getUserPwd());
		
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd()); 
		// 날것 그대로의, 평문 값을 넘겨주면 암호화 과정을 통해 암호문을 만들어 리턴!
		
		// System.out.println("암호화 후(암호문): " + encPwd);
		m.setUserPwd(encPwd);
		
		int result = mService.insertMember(m);
	
		
		if(result > 0) { // 회원가입 성공 => 메인페이지 url 재요청
			
			session.setAttribute("alertMsg", "회원가입 성공!");
			return "redirect:/";
		
		}else { // 회원가입 실패 => 에러페이지 
			
			model.addAttribute("errorMsg", "회원가입 실패!");
			return "common/errorPage";
			
		}
	}
	
	
	@RequestMapping("myPage.me")
	public String myPage() {
		// /WEB-INF/views/member/myPage.jsp
		return  "member/myPage";
	}
	
	@RequestMapping("update.me")
	public String updateMember(Member m, Model model, HttpSession session) {
		
		int result = mService.updateMember(m);
		
		if(result>0) { // 성공
			
			// 세션에 담겨있던 loginUser의 Member객체를 갱신된 객체로 변경해야함!
			session.setAttribute("loginUser", mService.loginMember(m));
			session.setAttribute("alertMsg", "회원 정보 변경 성공!");
			
			// 마이페이지 다시 보고 싶으면 myPage.me 라는 url로 재요청 
			return "redirect:myPage.me";
			
		}else { //실패
			model.addAttribute("errorMsg", "회원 정보 수정 실패!");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("delete.me")
	public String deleteMember(String userPwd, HttpSession session, Model model) {
		// userPwd: 비밀번호(평문)
		
		Member loginUser = (Member)session.getAttribute("loginUser"); // 로그인된 회원 객체 
		String encPwd = loginUser.getUserPwd();
		//encPwd : 비밀번호 (암호문)
		
		if(bcryptPasswordEncoder.matches(userPwd, encPwd)) { // 본인이 맞을 경우
			
			int result = mService.deleteMember(loginUser.getUserId());
			// 이미 비밀번호를 가지고 일치하는지 확인한 이후이기 때문에, 아이디만을 넘겨서 변경하면 됨! 
			
			if(result > 0) { // 회원탈퇴 성공 
				// 더이상 유효한 회원이 아니기 때문에 로그아웃 처리 = 세션에 담겨있떤 loginUser 삭제  
				session.removeAttribute("loginUser");
				
				session.setAttribute("alertMsg", "성공적으로 회원탈퇴되었습니다.");
				
				// 메인페이지 url 재요청 
				return "redirect:/";
				
			}else { // 탈퇴 실패
				model.addAttribute("errorMsg", "회원 탈퇴 실패");
				return "common/errorPage";
		
			}
			
		}else { // 비밀번호가 틀렸을 경우 
			model.addAttribute("errorMsg", "비밀번호가 틀렸습니다.");
			return "common/errorPage";
		}
		
	}
	
}








