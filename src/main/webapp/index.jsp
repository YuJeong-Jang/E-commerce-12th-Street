<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
    // 1. 세션에서 로그인 정보 확인
    String username = (String) session.getAttribute("loggedInUser"); // 서블릿이 저장한 이름

    // 2. 로그인 실패 시 전달된 에러 파라미터 확인
    String loginError = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>메인 페이지</title>
    <!-- 부트스트랩 CSS CDN 링크 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <%
                // 3. 세션 정보(username)에 따라 분기
                if (username == null) {
                    // [A] 로그아웃 상태: 환영인사 표시
            %>
            <div class="col-md-8">
                <div class="p-4">
                    <h1>E-commerce 12th Street</h1>
                    <h2 class="mt-4">어서오세요! 환영합니다.</h2> 
                </div>
            </div>
            <%
                } else {
                    // [B] 로그인 상태: 게시판 표시
            %>
            <!-- ========================== -->
            <!--  1. 메인 컨텐츠 (게시판)  -->
            <!-- ========================== -->
            <div class="col-md-8">
                <div class="p-4">
                    <h1>E-commerce 12th Street</h1>
                    <h2 class="mt-4">게시판 목록</h2>
                    <ul class="list-group">
                        <!-- board.do 서블릿을 호출 -->
                        <li class="list-group-item">
                            <a href="board.do">자유 게시판</a>
                        </li>
                        <!-- "상품 문의" 링크 삭제됨 -->
                    </ul>
                </div>
            </div>
            <%
                } // 로그인 했을때만 게시판 감
            %>
            <!-- ========================== -->
            <!--  2. 사이드바 (로그인/정보) -->
            <!-- ========================== -->
            <div class="col-md-4 border-start">
                <div class="p-4">
                    <%
                        // 3. 세션 정보(username)에 따라 분기
                        if (username == null) {
                            // [A] 로그아웃 상태: 로그인 폼 표시
                    %>
                        <div class="card">
                            <div class="card-header bg-dark text-white">
                                <h5 class="mb-0">로그인</h5>
                            </div>
                            <div class="card-body">
                                <form action="login.do" method="POST">
                                    <div class="mb-3">
                                        <label for="loginId" class="form-label">아이디:</label>
                                        <input type="text" class="form-control" id="loginId" name="memberId">
                                    </div>
                                    <div class="mb-3">
                                        <label for="loginPw" class="form-label">비밀번호:</label>
                                        <input type="password" class="form-control" id="loginPw" name="pwd">
                                    </div>
                                    <button type="submit" class="btn btn-primary w-100">로그인</button>
                                </form>
                                <%
                                    // 4. 로그인 실패 시 에러 메시지 표시
                                    if ("1".equals(loginError)) {
                                %>
                                    <div class="alert alert-danger mt-3 p-2" role="alert">
                                        아이디/비밀번호가 틀렸습니다.
                                    </div>
                                <%
                                    }
                                %>
                            </div>
                            <div class="card-footer text-center">
                                <a href="register.jsp">회원가입 하러가기</a>
                            </div>
                        </div>
                    <%
                        } else {
                            // [B] 로그인 상태: 환영 메시지 표시
                    %>
                        <div class="card">
                            <div class="card-header bg-dark text-white">
                                <h5 class="mb-0">내 정보</h5>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <strong><%= username %></strong> 님,
                                </h5>
                                <p class="card-text">환영합니다!</p>
                                <a href="mypage.do" class="btn btn-outline-primary btn-sm">마이 페이지</a>
                                <a href="logout.do" class="btn btn-outline-secondary btn-sm">로그아웃</a>
                            </div>
                        </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>

    <!-- 부트스트랩 JS CDN 링크 (필요시) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>