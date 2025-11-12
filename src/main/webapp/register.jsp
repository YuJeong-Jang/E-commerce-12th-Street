<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-dark text-white">
                        <h2 class="mb-0">회원가입</h2>
                    </div>
                    <div class="card-body">
                        <form action="register.do" method="POST">

                            <!-- DBeaver 스키마에 맞춘 name 속성 -->
                            <div class="mb-3">
                                <label for="regMemberId" class="form-label">회원아이디 (ID):</label>
                                <input type="text" class="form-control" id="regMemberId" name="memberId" required>
                            </div>
                            <div class="mb-3">
                                <label for="regPwd" class="form-label">비밀번호:</label>
                                <input type="password" class="form-control" id="regPwd" name="pwd" required>
                            </div>
                            <div class="mb-3">
                                <label for="regName" class="form-label">회원명 (이름):</label>
                                <input type="text" class="form-control" id="regName" name="name" required>
                            </div>
                            <div class="mb-3">
                                <label for="regPhone" class="form-label">연락처:</label>
                                <input type="text" class="form-control" id="regPhone" name="phone" required>
                            </div>

                            <!-- 회원가입 실패 시 에러 메시지 -->
                            <% 
                                String error=request.getParameter("error"); 
                                if ("1".equals(error)) { 
                                    out.println("<div class='alert alert-danger p-2' role='alert'>회원가입에 실패했습니다 (아이디 중복 등)</div>");
                                }
                            %>

                    <button type="submit" class="btn btn-primary w-100">가입하기</button>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="index.jsp">메인으로 돌아가기</a>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>

    </html>