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
    <style>
        /* 삭제 버튼을 위한 스타일 */
        .form-delete {
            display: inline-block;
            margin-left: 5px;
        }
    </style>
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
                    <th scope="col" style="width: 10%;">번호</th>
                    <th scope="col" style="width: 30%;">제목</th>
                    <th scope="col" style="width: 35%;">내용 (일부)</th>
                    <th scope="col" style="width: 15%;">작성일</th>
                    <th scope="col" style="width: 10%;">관리</th>
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
                                <td>
                                    <%-- TODO: 상세 페이지 링크 구현 (현재는 미사용) --%>
                                    <a href="#">${board.title}</a>
                                </td>
                                <td>${board.contents.length() > 50 ?
                                    board.contents.substring(0, 50).concat('...') : board.contents}</td>
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
                                    
                                    <form action="${pageContext.request.contextPath}/board/delete" method="POST" class="form-delete"
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

    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="editForm" action="${pageContext.request.contextPath}/board/edit" method="POST">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">게시글 수정</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
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
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-primary">저장</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // 'editModal'이 표시되기 직전에 발생하는 이벤트 리스너
        const editModal = document.getElementById('editModal');
        editModal.addEventListener('show.bs.modal', function (event) {
            // 모달을 트리거한 버튼
            const button = event.relatedTarget;

            // data-board-* 속성에서 데이터 추출
            const boardSeq = button.getAttribute('data-board-seq');
            const boardTitle = button.getAttribute('data-board-title');
            const boardContents = button.getAttribute('data-board-contents');

            // 모달 내부의 폼 요소 선택
            const modalTitleInput = editModal.querySelector('#editTitle');
            const modalContentsInput = editModal.querySelector('#editContents');
            const modalSeqInput = editModal.querySelector('#editBoardSeq');

            // 폼 요소에 데이터 설정
            modalTitleInput.value = boardTitle;
            modalContentsInput.value = boardContents;
            modalSeqInput.value = boardSeq;
        });
    </script>
</body>
</html>
