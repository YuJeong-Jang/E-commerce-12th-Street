<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>
    <h2>로그인 (JSP)</h2>

    <!-- 로그인 실패 시 에러 메시지 표시 -->
    <%
        String error = request.getParameter("error");
        if ("1".equals(error)) {
            out.println("<p style='color:red;'>아이디 또는 비밀번호가 틀렸습니다.</p>");
        }
    %>

    <!-- action="login.do" (서블릿 호출) -->
    <form action="login.do" method="POST">
        <div>
            <label>아이디:</label>
            <input type="text" name="username" required>
        </div>
        <div>
            <label>비밀번호:</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">로그인</button>
    </form>
</body>
</html>


