<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	table *{margin:5px;}
    table{width:100%;}
</style>
</head>
<body>
	<!-- 이쪽에 메뉴바 포함 할꺼임 -->
    <jsp:include page="../common/menubar.jsp"/>

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 상세보기</h2>
            <br>
            
            <a class="btn btn-secondary" style="float:right" href="list.bo">목록으로</a>
            <br><br>
            
            <table id="contentArea" align="center" class="table">
                <tr>
                    <th width="100">제목</th>
                    <td colspan="3">${ b.boardTitle }</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td>${ b.boardWriter }</td>
                    <th>작성일</th>
                    <td>${ b.createDate }</td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td colspan="3">
                    	<c:choose>
                    		<c:when test="${ empty b.originName }">
	                    		첨부파일이 없습니다.
	                    	</c:when>
	                    	<c:otherwise>
	                        	<a href="${ b.changeName }" download="${ b.originName }">${ b.originName }</a>
	                        </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="4"><p style="height:150px">${ b.boardContent }</p></td>
                </tr>
            </table>
            <br>

			<c:if test="${ loginUser.userId eq b.boardWriter }">
	            <div align="center">
	                <!-- 수정하기, 삭제하기 버튼은 이글이 본인글일 경우만 보여져야됨 -->
	                <a class="btn btn-primary" onclick="postFormSubmit(1);">수정하기</a>
	                <a class="btn btn-danger" onclick="postFormSubmit(2);">삭제하기</a>
	            </div><br><br>
            </c:if>
            
            <form action="" method="post" id="postForm">
            	<input type="hidden" name="bno" id="bno" value="${ b.boardNo }">
            	<input type="hidden" name="fileName" value="${b.changeName}">
            </form>
            
            <script>
            	function postFormSubmit(num){
            		var url = "";
            		if(num == 1){ // 수정하기 클릭
            			url = "updateForm.bo";
            		}else if(num == 2){ // 삭제하기 클릭
            			url = "delete.bo";
            		}
            		
            		$("#postForm").attr("action", url).submit();
            	}
            </script>



            <!-- 댓글 기능은 나중에 ajax 배우고 접목시킬예정! 우선은 화면구현만 해놓음 -->
            <table id="replyArea" class="table" align="center">
                <thead>
                    <tr>
                        <th colspan="2">
                            <textarea class="form-control" name="" id="content" cols="55" rows="2" style="resize:none; width:100%"></textarea>
                        </th>
                        <th style="vertical-align: middle"><button class="btn btn-secondary" onclick="addReply();">등록하기</button></th>
                    </tr>
                    <tr>
                       <td colspan="3">댓글 (<span id="rcount"></span>) </td> 
                    </tr>
                </thead>
                <tbody>
	
                </tbody>
            </table>
        </div>
        <br><br>
    </div>

     <script>
             $(function () {
               // 모든 html요소 만들어지고 난 후 호출
               selectReplyList();
             })

             	function addReply(){
          // 넘겨야할 값: 작성된 댓글 내용, 게시글 번호, 로그인한 사용자의 아이디
          if($("#content").val().trim().length > 0 ){
          	 // 만약 댓글 입력을 하지않았다면? => 빈문자열 => null값 오류
                 // 공백을 제거한 문자열의 길이가 0이상일 때 등록되도록!
          	$.ajax({
          		url:"rinsert.bo",
          		data:{
          				replyContent: $("#content").val(),
          				refBno: ${ b.boardNo }, // 2
          			//	replyWrtier: ${ loginUser.userId } // admin
          				replyWriter: "${ loginUser.userId }" // "admin
          			// 많이하는 실수, el로 가져오고자하는 값이 문자열이면 "" 묶어줘야!

          		},
          		success:function(result){ // 매개변수에 success or fail 담김
          			if(result == "success"){
          				// textarea 초기화
          				$("#content").val("");

          				// list 조회하는 ajax 재호출
          				selectReplyList();
          			}
          		}, error:function(){
          			console.log("댓글 작성용 ajax 통신 실패")
          		}
			})
			
		}
	}

               function selectReplyList() {
                 // 이 게시글에 딸린 댓글 리스트 조회용 ajax
                 $.ajax({
                    url: "rlist.bo",
                   //data:{bno:$("#bno").val()}, // data에 넘길 값 담아주기,  보고 있는 게시글 번호
              	 	data:{bno:${b.boardNo}},
                    success: function(list) {
                     console.log(list);
                     $("#rcount").text(list.length);

                     var value="";
                     for(var i in list){
                   	  value += "<tr>" +
                     	  		"<th>" +  list[i].replyWriter + "</th>" +
                     			"<td>" + list[i].replyContent + "</td>" +
                     			"<td>" + list[i].createDate + "</td>" +
                     	"</tr>";
                     }
                     $("#replyArea tbody").html(value);
                   }, error: function() {
                     console.log("ajax통신실패");
               	}
         		})
         		
         	}
      </script>


    <!-- 이쪽에 푸터바 포함할꺼임 -->
    <jsp:include page="../common/footer.jsp"/>
</body>
</html>