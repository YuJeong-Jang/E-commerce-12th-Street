<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // 세션에서 사용자 정보 확인
    String username = (String) session.getAttribute("loggedInUser");

    // 세션에 정보가 없으면 (로그인 안 했으면) 로그인 페이지로 쫓아냄
    if (username == null) {
        response.sendRedirect("login.jsp");
        return; // 현재 JSP 실행 중지
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
</head>
<body>
    <h2>메인 페이지</h2>
    <!-- 세션에서 가져온 사용자 이름 출력 -->
    <p>환영합니다, <strong><%= username %></strong> 님!</p>

    <a href="logout.do">로그아웃</a>
</body>
</html>

