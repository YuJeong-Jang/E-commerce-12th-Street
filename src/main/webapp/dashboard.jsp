<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <%-- JSTL Core 태그 라이브러리 --%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <%-- JSTL Formatting 태그 라이브러리 --%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 목록 (Dashboard)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h1>게시판</h1>
            <a href="${pageContext.request.contextPath}/board/write" class="btn btn-primary">글쓰기</a>
        </div>

        <table class="table table-hover">
            <thead class="table-dark">
                <tr>
                    <th scope="col">번호</th>
                    <th scope="col">제목</th>
                    <th scope="col">내용 (일부)</th>
                    <th scope="col">작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty boardList}">
                        <tr>
                            <td colspan="4" class="text-center">게시글이 없습니다.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="board" items="${boardList}">
                            <tr>
                                <td>${board.boardSeq}</td>
                                <td>
                                    <%-- TODO: 상세 페이지 링크 구현 --%>
                                    <a href="#">${board.title}</a>
                                </td>
                                <td>${board.contents.length() > 50 ? board.contents.substring(0, 50).concat('...') : board.contents}</td>
                                <td>
                                    <fmt:formatDate value="${board.rgstYmd}" pattern="yyyy-MM-dd" />
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</body>
</html>
