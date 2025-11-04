<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>
    <h2>회원가입 (JSP)</h2>
    
    <!-- action="register.do" (서블릿 호출) -->
    <form action="register.do" method="POST">
        <div>
            <label>아이디:</label>
            <input type="text" name="username" required>
        </div>
        <div>
            <label>비밀번호:</label>
            <input type="password" name="password" required>
        </div>
        <div>
            <label>이메일:</label>
            <input type="email" name="email" required>
        </div>
        <button type="submit">가입하기</button>
    </form>
</body>
</html>


