<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- ★★★★★ [오류 수정] Tomcat 9 (javax)용 JSTL uri로 변경 ★★★★★ --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 목록 (Dashboard)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-delete { display: inline-block; margin-left: 5px; }
    </style>
</head>
<body>
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h1>게시판</h1>
            <%-- [수정] /board/write 서블릿을 호출 --%>
            <a href="${pageContext.request.contextPath}/board.do?action=writeForm" class="btn btn-primary">글쓰기</a>
        </div>

        <table class="table table-hover">
            <thead class="table-dark">
                <tr>
                    <th scope="col">번호</th>
                    <th scope="col">제목</th>
                    <th scope="col">내용 (일부)</th>
                    <th scope="col">작성일</th>
                    <th scope="col">관리</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty boardList}">
                        <tr>
                            <td colspan="5" class="text-center">게시글이 없습니다.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="board" items="${boardList}">
                            <tr>
                                <td>${board.boardSeq}</td>
                                <td><a href="#">${board.title}</a></td>
                                <td>
                                    <c:set var="contents" value="${board.contents}" />
                                    ${contents.length() > 50 ? 
                                      contents.substring(0, 50).concat('...') : contents}
                                </td>
                                <td>
                                    <fmt:formatDate value="${board.rgstYmd}" pattern="yyyy-MM-dd" />
                                </td>
                                <td>
                                    <button type="button" class="btn btn-sm btn-outline-secondary"
                                            data-bs-toggle="modal"
                                            data-bs-target="#editModal"
                                            data-board-seq="${board.boardSeq}"
                                            data-board-title="${board.title}"
                                            data-board-contents="${board.contents}">
                                        수정
                                    </button>
                                    
                                    <form action="${pageContext.request.contextPath}/board.do?action=delete" method="POST" class="form-delete"
                                          onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                        <input type="hidden" name="boardSeq" value="${board.boardSeq}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">삭제</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- ... (모달 창 코드) ... -->
    <div class="modal fade" id="editModal" ...>
        <div class="modal-dialog">
            <div class="modal-content">
                <%-- [수정] /board/edit 서블릿을 호출 --%>
                <form id="editForm" action="${pageContext.request.contextPath}/board.do?action=edit" method="POST">
                    <div class="modal-header">...</div>
                    <div class="modal-body">
                        <input type="hidden" id="editBoardSeq" name="boardSeq">
                        <div class="mb-3">
                            <label for="editTitle" class="form-label">제목</label>
                            <input type="text" class="form-control" id="editTitle" name="title" required>
                        </div>
                        <div class="mb-3">
                            <label for="editContents" class="form-label">내용</label>
                            <textarea class="form-control" id="editContents" name="contents" rows="10" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">...</div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // 모달 관련 JavaScript (수정 없음)
        const editModal = document.getElementById('editModal');
        editModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const boardSeq = button.getAttribute('data-board-seq');
            const boardTitle = button.getAttribute('data-board-title');
            const boardContents = button.getAttribute('data-board-contents');
            const modalTitleInput = editModal.querySelector('#editTitle');
            const modalContentsInput = editModal.querySelector('#editContents');
            const modalSeqInput = editModal.querySelector('#editBoardSeq');
            
            modalTitleInput.value = boardTitle;
            modalContentsInput.value = boardContents;
            modalSeqInput.value = boardSeq;
        });
    </script>
</body>
</html>