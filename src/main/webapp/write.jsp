<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 작성</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1>새 게시글 작성</h1>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/board/write" method="POST">
            <div class="mb-3">
                <label for="title" class="form-label">제목</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="mb-3">
                <label for="contents" class="form-label">내용</label>
                <textarea class="form-control" id="contents" name="contents" rows="10" required></textarea>
            </div>
            
            <a href="${pageContext.request.contextPath}/board/dashboard" class="btn btn-secondary">취소</a>
            <button type="submit" class="btn btn-primary">등록</button>
        </form>
    </div>
</body>
</html>
