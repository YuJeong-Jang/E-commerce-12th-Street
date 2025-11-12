<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="header.jsp" %>
<% // 내장 session 객체 직접 사용 
        if (request.getAttribute("memberInfo")==null) { 
            if (session==null || session.getAttribute("loggedInUser")==null) { 
                // 세션 없음: 로그인 페이지로 리다이렉트 
                response.sendRedirect("index.jsp");
                return; 
            } else { 
                // 세션은 있지만 memberInfo가 없으면 서블릿 경유 
                response.sendRedirect("mypage.do"); 
                return; 
            } 
        } // memberInfo 있으면 정상 페이지 렌더링 
    %>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">

                    <div class="card shadow-sm">
                        <div class="card-header bg-dark text-white">
                            <h2 class="mb-0">마이 페이지</h2>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">
                                환영합니다,
                                <c:out value="${memberInfo.name}" /> 님!
                            </h5>
                            <p class="card-text mb-4">회원님의 정보를 확인하세요.</p>

                            <ul class="list-group list-group-flush mb-4">
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>아이디:</strong>
                                    <span>
                                        <c:out value="${memberInfo.memberId}" />
                                    </span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>이름:</strong>
                                    <span>
                                        <c:out value="${memberInfo.name}" />
                                    </span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>연락처:</strong>
                                    <span>
                                        <c:out value="${memberInfo.phone}" />
                                    </span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>가입일:</strong>
                                    <span>
                                        <fmt:formatDate value="${memberInfo.rgstYmd}" pattern="yyyy년 MM월 dd일" />
                                    </span>
                                </li>
                            </ul>

                            <a href="index.jsp" class="btn btn-primary w-100">메인으로 돌아가기</a>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        </body>

        </html>