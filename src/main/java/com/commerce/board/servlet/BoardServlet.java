package com.commerce.board.servlet;

import com.commerce.board.dao.BoardDao;
import com.commerce.board.model.Board;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// URL 패턴을 /board/* 로 매핑
@WebServlet(name = "boardServlet", urlPatterns = "/board/*")
public class BoardServlet extends HttpServlet {

    private BoardDao boardDao;

    @Override
    public void init() throws ServletException {
        // 서블릿 초기화 시 DAO 인스턴스 생성
        this.boardDao = new BoardDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/dashboard"; // 기본 경로
        }

        switch (pathInfo) {
            case "/dashboard":
                showDashboard(req, res); // 게시판 목록 (dashbord.jsp)
                break;
            case "/write":
                showWriteForm(req, res); // 글쓰기 폼 (write.jsp)
                break;
            // TODO: /view, /edit 등 상세/수정 페이지 라우팅 구현
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // POST 요청도 인코딩 UTF-8 설정
        req.setCharacterEncoding("UTF-8");

        switch (pathInfo) {
            case "/write":
                handleWritePost(req, res); // 글쓰기 처리
                break;
            case "/edit":
                handleEditPost(req, res); // 수정 처리
                break;
            case "/delete":
                handleDeletePost(req, res); // 삭제 처리
                break;
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // GET /board/dashboard
    private void showDashboard(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Board> boardList = boardDao.getBoardList();
        req.setAttribute("boardList", boardList);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/dashboard.jsp");
        dispatcher.forward(req, res);
    }

    // GET /board/write
    private void showWriteForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/write.jsp");
        dispatcher.forward(req, res);
    }

    // POST /board/write
    private void handleWritePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // 요청 인코딩 설정 (JSP에서 UTF-8로 넘어오도록 설정)
        // -> doPost 상단에서 공통 처리
        // req.setCharacterEncoding("UTF-8"); 

        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        
        // TODO: 실제로는 세션에서 로그인한 회원 memSeq를 가져와야 함
        int mockMemSeq = 1; // 임시 회원 ID

        Board board = new Board();
        board.setTitle(title);
        board.setContents(contents);
        board.setMemSeq(mockMemSeq);

        boolean success = boardDao.insertBoard(board);

        if (success) {
            // 등록 성공 시 목록 페이지로 리다이렉트
            res.sendRedirect(req.getContextPath() + "/board/dashboard");
        } else {
            // 실패 시 에러 메시지와 함께 다시 작성 폼으로
            req.setAttribute("errorMessage", "게시글 등록에 실패했습니다.");
            showWriteForm(req, res);
        }
    }

    // POST /board/edit
    private void handleEditPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
            String title = req.getParameter("title");
            String contents = req.getParameter("contents");

            Board board = new Board();
            board.setBoardSeq(boardSeq);
            board.setTitle(title);
            board.setContents(contents);
            
            // TODO: 수정 권한 검사 (로그인한 사용자와 작성자가 같은지)

            boardDao.updateBoard(board);
            
        } catch (NumberFormatException e) {
            // boardSeq 파라미터가 숫자가 아닐 경우
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 처리 후 대시보드로 리다이렉트
        res.sendRedirect(req.getContextPath() + "/board/dashboard");
    }
    
    // POST /board/delete
    private void handleDeletePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
            
            // TODO: 삭제 권한 검사

            boardDao.deleteBoard(boardSeq);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 처리 후 대시보드로 리다이렉트
        res.sendRedirect(req.getContextPath() + "/board/dashboard");
    }
}
