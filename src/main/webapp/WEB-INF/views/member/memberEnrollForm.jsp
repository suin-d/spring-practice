<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
   <!-- 이쪽에 메뉴바 포함 할꺼임 -->
    <jsp:include page="../common/menubar.jsp"/>
    
    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>회원가입</h2>
            <br>

            <form id="enrollForm" action="insert.me" method="post">
                <div class="form-group">
                    <label for="userId">* ID :</label>
                    <input type="text" class="form-control" id="userId" name="userId" placeholder="Please Enter ID" required>
                    <div id="check-result" style="Font-size:0.8em; display:none"></div><br>
                    
                    <label for="userPwd">* Password :</label>
                    <input type="password" class="form-control" id="userPwd" name="userPwd" placeholder="Please Enter Password" required><br>
                    
                    <label for="checkPwd">* Password Check :</label>
                    <input type="password" class="form-control" id="checkPwd" placeholder="Please Enter Password" required><br>
                    
                    <label for="userName">* Name :</label>
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="Please Enter Name" required><br>
                    
                    <label for="email"> &nbsp; Email :</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Please Enter Email"><br>
                    
                    <label for="age"> &nbsp; Age :</label>
                    <input type="number" class="form-control" id="age" name="age" placeholder="Please Enter Age"><br>
                    
                    <label for="phone"> &nbsp; Phone :</label>
                    <input type="tel" class="form-control" id="phone" name="phone" placeholder="Please Enter Phone (-없이)"><br>
                    
                    <label for="address"> &nbsp; Address :</label>
                    <input type="text" class="form-control" id="address" name="address" placeholder="Please Enter Address"><br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" name="gender" id="Male" value="M">
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" name="gender" id="Female" value="F">
                    <label for="Female">여자</label><br>
                    
                </div>
                <br>
                <div class="btns" align="center">
                    <button id="enroll-btn" type="submit" class="btn btn-primary" disabled>회원가입</button>
                    <button type="reset" class="btn btn-danger"> 초기화</button>
                </div>
            </form>
        </div>
        <br><br>
    </div>

	<script>
		$(function(){
			
			var idInput = $("#enrollForm input[name=userId]")//input 요소 자체 받아줌 
			
			idInput.keyup(function(){
				
				//console.log("입력됨");
				
				// 우선 최소 5글자 이상으로 입력했을 때만 중복체크 하도록 
				if(idInput.val().length >= 5){ // 중복체크 할만함 
					
					// console.log("중복체크할것!")
					
				$.ajax({
					url:"idCheck.me",
					data:{userId:idInput.val()},
					type:"post",
					success:function(count){
						if(count == 1){
							// 중복된 아이디 존재 => 사용불가
							// => 메세지 빨간색 출력, 버튼 비활성화
							$("#check-result").show();
							$("#check-result").css("color", "red").text("중복된 아이디가 존재합니다. 다시 입력해주세요.");
							$("#enroll-btn").attr("disabled", true);
							
						}else{
							// 중복된 아이디 없음 => 사용가능
							// => 메세지 초록색 출력, 버튼 활성화 
							$("#check-result").show();
							$("#check-result").css("color", "green").text("사용가능한 아이디입니다.");
							$("#enroll-btn").removeAttr("disabled");
							
						}
					},
					error:function(){
						console.log("아이디중복체크용 ajax 통신 실패");
					}
					
				});
					
					
				}else{ //중복체크할 필요도 없음! 애초에 유효한 아이디가 아님! 
					
					// console.log("중복체크안할것..")
					// 어떠한 메세지도 보이지 않고, 버튼 비활성화 
					$("#check-result").hide(); 
					$("enroll-btn").attr("disabled", true);
				}
			}) // 키보드로부터 값이 띄워졌을 때, 눌렀다가 떼는 순간  
			
		})
		
	</script>


    <!-- 이쪽에 푸터바 포함할꺼임 -->
    <jsp:include page="../common/footer.jsp"/>
    
</body>
</html>