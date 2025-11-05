<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이 페이지</title>
</head>
<body>
    <h2>마이 페이지</h2>

    <%-- 
        MyPageServlet이 request.setAttribute("memberInfo", ...);로
        저장한 회원 정보를 여기서 꺼내서 사용합니다.
    --%>
    <div>
        <strong>아이디:</strong>
        <span>${requestScope.memberInfo.username}</span>
    </div>
    <div>
        <strong>이메일:</strong>
        <span>${requestScope.memberInfo.email}</span>
    </div>
    <div>
        <strong>가입일:</strong>
        <span>${requestScope.memberInfo.createdAt}</span>
    </div>

    <br>
    <a href="index.jsp">메인으로 돌아가기</a>
</body>
</html>

