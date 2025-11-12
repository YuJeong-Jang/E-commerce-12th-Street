<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    // 내장 session 객체 직접 사용
    if (request.getAttribute("memberInfo") == null) {
        if (session == null || session.getAttribute("loggedInUser") == null) {
            // 세션 없음: 로그인 페이지로 리다이렉트
            response.sendRedirect("index.jsp");
            return;
        } else {
            // 세션은 있지만 memberInfo가 없으면 서블릿 경유
            response.sendRedirect("mypage.do");
            return;
        }
    }
    // memberInfo 있으면 정상 페이지 렌더링
%>

                    <!DOCTYPE html>
                    <html lang="ko">

                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>마이 페이지</title>
                        <!-- 부트스트랩 CSS CDN 링크 -->
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                            rel="stylesheet">
                    </head>

                    <body>
                        <div class="container mt-5">
                            <div class="row justify-content-center">
                                <div class="col-md-8">

                                    <div class="card">
                                        <div class="card-header bg-dark text-white">
                                            <h2 class="mb-0">마이 페이지</h2>
                                        </div>
                                        <div class="card-body">
                                            <h5 class="card-title">
                                                환영합니다,
                                                <c:out value="${memberInfo.name}" /> 님!
                                            </h5>
                                            <p class="card-text">회원님의 정보를 확인하세요.</p>

                                            <hr>

                                            <%-- 회원 정보를 List Group으로 깔끔하게 표시 --%>
                                                <ul class="list-group list-group-flush">
                                                    <li class="list-group-item">
                                                        <strong>아이디:</strong>
                                                        <c:out value="${memberInfo.memberId}" />
                                                    </li>
                                                    <li class="list-group-item">
                                                        <strong>이름:</strong>
                                                        <c:out value="${memberInfo.name}" />
                                                    </li>
                                                    <li class="list-group-item">
                                                        <strong>연락처:</strong>
                                                        <c:out value="${memberInfo.phone}" />
                                                    </li>
                                                    <li class="list-group-item">
                                                        <strong>가입일:</strong>
                                                        <%-- DTO의 Date 객체를 yyyy-MM-dd 형식으로 포맷팅 --%>
                                                            <fmt:formatDate value="${memberInfo.rgstYmd}"
                                                                pattern="yyyy년 MM월 dd일" />
                                                    </li>
                                                </ul>

                                                <br>
                                                <a href="index.jsp" class="btn btn-primary">메인으로 돌아가기</a>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                        <!-- 부트스트랩 JS CDN 링크 (필요시) -->
                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                    </body>

                    </html>