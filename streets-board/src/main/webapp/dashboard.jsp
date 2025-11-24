<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="header.jsp" %>
    <script>
        // 메시지 이벤트 리스너는 최상단에 위치하여 메시지를 놓치지 않도록 등록
        window.addEventListener( 'message', ( event ) => {
            const myDomain = 'https://board.12-streets.store';
	    if ( event.origin !== trustedOrigin ) return;
            const data = event.data;

            if ( data.loggedInUser ) {
                // 세션 동기화용 AJAX 요청 등 추가 작업
                fetch( '${pageContext.request.contextPath}/board.do?action=sessionSave', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify( { username: data.loggedInUser } ),
                credentials: 'include'  // 쿠키 전달용
            } ).then( res => res.json() )
                .then( data => console.log( '세션저장 결과', data ) );
              }
        } );

        // DOMContentLoaded 시 부모에 준비 완료 신호 전송
        window.addEventListener( 'DOMContentLoaded', () => {
		window.parent.postMessage({ ready: true }, myDomain);
        } );

    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold text-primary" id="text-primary">게시판</h2>
                <div id="userInfo">로그인 정보 대기중...</div>
                <a href="${pageContext.request.contextPath}/board.do?action=writeForm"
                    class="btn btn-outline-primary btn-sm" id="writeForm">글쓰기</a>
            </div>

            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th scope="col" class="text-center">번호</th>
                                <th scope="col">제목</th>
                                <th scope="col">내용 (일부)</th>
                                <th scope="col" class="text-center">작성일</th>
                                <th scope="col" class="text-center">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty boardList}">
                                    <tr>
                                        <td colspan="5" class="text-center py-4 text-muted">게시글이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="board" items="${boardList}">
                                        <tr>
                                            <td class="text-center">${board.boardSeq}</td>
                                            <td>
                                                <p class="mb-0">${board.title}</p>
                                            </td>
                                            <td>
                                                <c:set var="contents" value="${board.contents}" />
                                                ${contents.length() > 50 ?
                                                contents.substring(0, 50).concat('...') : contents}
                                            </td>
                                            <td class="text-center">
                                                <fmt:formatDate value="${board.rgstYmd}" pattern="yyyy-MM-dd" />
                                            </td>
                                            <td class="text-center">
                                                <button type="button" class="btn btn-sm btn-outline-secondary me-1"
                                                    data-bs-toggle="modal" data-bs-target="#editModal"
                                                    data-board-seq="${board.boardSeq}" data-board-title="${board.title}"
                                                    data-board-contents="${board.contents}">
                                                    수정
                                                </button>
                                                <a href="${pageContext.request.contextPath}/board.do?action=delete&seq=${board.boardSeq}"
                                                    class="btn btn-sm btn-outline-danger"
                                                    onclick="return confirm('정말 삭제하시겠습니까?');">
                                                    삭제
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 모달 창 -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="editModalLabel">게시글 수정</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                    <form id="editForm" action="${pageContext.request.contextPath}/board.do?action=edit" method="POST">
                        <div class="modal-body">
                            <input type="hidden" id="boardSeq" name="boardSeq">
                            <div class="mb-3">
                                <label for="editTitle" class="form-label">제목</label>
                                <input type="text" class="form-control" id="editTitle" name="title" required>
                            </div>
                            <div class="mb-3">
                                <label for="editContents" class="form-label">내용</label>
                                <textarea class="form-control" id="editContents" name="contents" rows="5"
                                    required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">수정</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            // 수정모달 열기
                const editModal = document.getElementById( 'editModal' );
                editModal.addEventListener( 'show.bs.modal', function ( event ) {
                    const button = event.relatedTarget;
                    const boardSeq = button.getAttribute( 'data-board-seq' );
                    const boardTitle = button.getAttribute( 'data-board-title' );
                    const boardContents = button.getAttribute( 'data-board-contents' );
                    document.getElementById( 'boardSeq' ).value = boardSeq;
                    document.getElementById( 'editTitle' ).value = boardTitle;
                    document.getElementById( 'editContents' ).value = boardContents;
                } );

                // 수정 액션
                document.getElementById( 'editForm' ).addEventListener( 'submit', function ( event ) {
                    const boardSeq = document.getElementById( 'boardSeq' ).value;
                    const title = document.getElementById( 'editTitle' ).value;
                    const contents = document.getElementById( 'editContents' ).value;
                    console.log( 'boardSeq:', boardSeq );
                    console.log( 'title:', title );
                    console.log( 'contents:', contents );
                } );
        </script>
        </body>

        </html>
