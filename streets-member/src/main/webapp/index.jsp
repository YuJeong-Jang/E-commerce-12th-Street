<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<% 
    String username = (String) session.getAttribute("loggedInUser");
    String loginError = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>E-commerce 12th Street</title>
</head>
<body>
<script>
    window.addEventListener( 'message', function ( event ) {
        const trustedOrigin = 'https://board.12-streets.store';
        
        if ( event.origin !== trustedOrigin && event.origin !== trustedOrigin + ":9090" ) {
            return; 
        }

        if ( event.data.ready ) {
            const iframe = document.getElementById( 'boardIframe' );
            const username = '<%= username != null ? username : "" %>';  
            
            iframe.contentWindow.postMessage( { loggedInUser: username }, event.origin );
        }
    } );
</script>

<div class="container mt-5">
    <div class="row g-4">
        <div class="col-md-8">
            <div class="p-4">
                <% if (username == null) { %>
                    <h1>E-commerce 12th Street</h1>
                    <h2 class="mt-4 mb-3">"어서오세요! 환영합니다."</h2>
                <% } else { %>
                    <iframe src="/board.do?action=list" id="boardIframe"
                        style="width:100%; height:100vh; border:none;"></iframe>
                <% } %>
            </div>
        </div>

        <div class="col-md-4 border-start">
            <div class="p-4">
                <% if (username == null) { %>
                    <div class="card shadow-sm">
                        <div class="card-header bg-dark text-white">
                            <h5 class="mb-0">로그인</h5>
                        </div>
                        <div class="card-body">
                            <form action="login.do" method="POST" novalidate>
                                <div class="mb-3">
                                    <label for="loginId" class="form-label">아이디</label>
                                    <input type="text" class="form-control" id="loginId" name="memberId" required>
                                </div>
                                <div class="mb-3">
                                    <label for="loginPw" class="form-label">비밀번호</label>
                                    <input type="password" class="form-control" id="loginPw" name="pwd" required>
                                </div>
                                <button type="submit" class="btn btn-primary w-100">로그인</button>
                            </form>
                            <% if ("1".equals(loginError)) { %>
                                <div class="alert alert-danger mt-3 p-2" role="alert">
                                    아이디/비밀번호가 틀렸습니다.
                                </div>
                            <% } %>
                        </div>
                        <div class="card-footer text-center">
                            <a href="register.jsp" class="text-decoration-none">회원가입 하러가기</a>
                        </div>
                    </div>
                <% } else { %>
                    <div class="card shadow-sm">
                        <div class="card-header bg-dark text-white">
                            <h5 class="mb-0">내 정보</h5>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title mb-3">
                                <strong><%= username %></strong> 님,
                            </h5>
                            <p class="card-text mb-4">환영합니다!</p>
                            <a href="mypage.do" class="btn btn-outline-primary btn-sm me-2">마이 페이지</a>
                            <a href="logout.do" class="btn btn-outline-secondary btn-sm">로그아웃</a>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
</body>
</html>
