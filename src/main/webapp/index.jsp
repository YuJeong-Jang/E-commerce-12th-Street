<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // 1. 세션에서 로그인 정보 확인
    String username = (String) session.getAttribute("loggedInUser");

    // 2. 로그인 실패 시 전달된 에러 파라미터 확인
    String loginError = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
    <style>
        /* 간단한 2단 레이아웃 */
        #container { display: flex; }
        #content { flex: 3; padding: 10px; }
        #sidebar { flex: 1; padding: 10px; border-left: 1px solid #ccc; }
    </style>
</head>
<body>

    <div id="container">
        <div id="content">
            <h1>E-commerce 12th Street</h1>
            <h2>게시판 목록</h2>
            <ul>
                <li><a href="board.jsp?id=1">자유 게시판</a> (아직 구현 안 됨)</li>
                <li><a href="board.jsp?id=2">상품 문의</a> (아직 구현 안 됨)</li>
            </ul>
        </div>

        <div id="sidebar">
            <%
                // 3. 세션 정보(username)에 따라 분기
                if (username == null) {
                    // [A] 로그아웃 상태: 로그인 폼 표시
            %>
                <h3>로그인</h3>
                <form action="login.do" method="POST">
                    <div>
                        <label>ID:</label>
                        <input type="text" name="username" style="width: 90%;">
                    </div>
                    <div>
                        <label>PW:</label>
                        <input type="password" name="password" style="width: 90%;">
                    </div>
                    <button type="submit" style="width: 100%; margin-top: 5px;">로그인</button>
                </form>

                <%
                    // 4. 로그인 실패 시 에러 메시지 표시
                    if ("1".equals(loginError)) {
                %>
                    <p style="color:red; font-size: 0.9em;">아이디/비밀번호가 틀렸습니다.</p>
                <%
                    }
                %>
                
                <hr>
                <a href="register.jsp">회원가입 하러가기</a>
            
            <%
                } else {
                    // [B] 로그인 상태: 환영 메시지 표시
            %>
                <h3>내 정보</h3>
                <p><strong><%= username %></strong> 님, 환영합니다!</p>
                <a href="mypage.jsp">마이 페이지</a><br>
                <a href="logout.do">로그아웃</a>
            <%
                }
            %>
        </div>
    </div>

</body>
</html>
