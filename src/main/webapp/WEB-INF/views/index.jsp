<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- c에 jstl의 기능 담기 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>wt 카페</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css" />
	</head>
	
	<body>
		<%@include file="/WEB-INF/views/common/header.jsp" %>
	
			<div id="container">
				<div id="menuAdmin">
					<h2 id="menuAdminH2">공지사항</h2>
					
					<!-- 특별한 기능(jstl 라이브러리를 이용하여 세션에 있는 변수 세팅 조건 설정) -->
					<!-- 세션 공간에 저장되어있는 "MANAGER"의 값이 true일 때 작성이라는 버튼이 보이도록 설정 -->
					<c:if test="${MANAGER == true}">
						<!-- location.href=localhost:8080/noticeAdd -->
						<button type="button" onclick="location.href=`${pageContext.request.contextPath}/noticeAdd`">작성</button>
					</c:if>
					
					<div id="menuList"></div>
				</div>
			</div>
	
			<%@include file="/WEB-INF/views/common/footer.jsp" %>
	</body>
</html>