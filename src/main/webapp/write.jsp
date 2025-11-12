<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
    <div class="container mt-5">
        <h1>새 게시글 작성</h1>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                ${errorMessage}
            </div>
        </c:if>

        <%-- [수정] /board/write 서블릿을 호출 --%>
            <form action="${pageContext.request.contextPath}/board.do?action=write" method="POST">
                <div class="mb-3">
                    <label for="title" class="form-label">제목</label>
                    <input type="text" class="form-control" id="title" name="title" required>
                </div>
                <div class="mb-3">
                    <label for="contents" class="form-label">내용</label>
                    <textarea class="form-control" id="contents" name="contents" rows="10" required></textarea>
                </div>

                <%-- [수정] /board/dashboard 서블릿을 호출 --%>
                    <a href="${pageContext.request.contextPath}/board.do" class="btn btn-secondary">취소</a>
                    <button type="submit" class="btn btn-primary">등록</button>
            </form>
    </div>
    </body>

    </html>