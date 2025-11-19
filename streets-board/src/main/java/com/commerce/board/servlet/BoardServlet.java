package com.commerce.board.servlet;

import com.commerce.board.dao.BoardDAO;
import com.commerce.board.model.Board;
// ★★★★★ [수정] MemberDAO/DTO의 올바른 경로 import ★★★★★
import com.commerce.member.model.MemberDAO;
import com.commerce.member.model.MemberDTO;

// ★★★★★ [수정] Tomcat 9 (javax)용 서블릿으로 변경
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // 세션 사용을 위해 임포트

import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;

@WebServlet(name = "boardServlet", urlPatterns = "/board.do")
public class BoardServlet extends HttpServlet {

    private BoardDAO boardDao;

    @Override
    public void init() throws ServletException {
        this.boardDao = new BoardDAO();
    }

    // GET 요청 처리 (목록 보기, 글쓰기 폼)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // 기본값
        }

        switch (action) {
            case "writeForm":
                showWriteForm(req, res); // 글쓰기 폼 (write.jsp)
                break;
            case "list":
            default:
                showDashboard(req, res); // 게시판 목록 (dashboard.jsp)
                break;
            case "delete":
                handleDelete(req, res); // 삭제 처리
                break;
        }
    }

    // POST 요청 처리 (글쓰기, 수정, 삭제)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        req.setCharacterEncoding("UTF-8");

        switch (action) {
            case "write":
                handleWritePost(req, res); // 글쓰기 처리
                break;
            case "edit":
                handleEditPost(req, res); // 수정 처리
                break;
            case "sessionSave":
                handleSessionSave(req,res); // 세션 처리
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // GET /board.do (또는 /board.do?action=list)
    private void showDashboard(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("[BoardServlet] /board.do (list) GET 요청 수신");
        List<Board> boardList = boardDao.getBoardList();
        req.setAttribute("boardList", boardList);
        System.out.print("=============================");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/dashboard.jsp");
        dispatcher.forward(req, res);
    }

    // GET /board.do?action=writeForm
    private void showWriteForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("[BoardServlet] /board.do?action=writeForm GET 요청 수신");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/write.jsp");
        dispatcher.forward(req, res);
    }

    // GET /board.do?action=delete
    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("[BoardServlet] /board.do?action=delete GET 요청 수신");
        try {
            int boardSeq = Integer.parseInt(req.getParameter("seq"));
            System.out.println(boardSeq);
            // TODO: 삭제 권한 검사
            boardDao.deleteBoard(boardSeq);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        res.sendRedirect(req.getContextPath() + "/board.do");
    }

    // POST /board.do?action=write
    private void handleWritePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("[BoardServlet] /board.do?action=write POST 요청 수신");
        
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            res.sendRedirect("index.jsp"); // 로그인이 안되어있으면 튕겨냄
            return;
        }
        
        // ★★★★★ [오류 수정] MemberDAO/MemberDTO 사용 ★★★★★
        MemberDAO memberDao = new MemberDAO(); 
        String memberId = (String) session.getAttribute("loggedInUser");
        MemberDTO member = memberDao.getMemberByUsername(memberId);
        
        if (member == null) {
            System.err.println("[BoardServlet] 세션 유저 " + memberId + "의 회원일련번호(memSeq)를 찾을 수 없음");
            res.sendRedirect("index.jsp?error=2");
            return;
        }

        // ★★★★★ [오류 수정] member 객체에서 getMemSeq() 호출 ★★★★★
        int memSeq = member.getMemSeq(); 

        Board board = new Board();
        board.setTitle(title);
        board.setContents(contents);
        board.setMemSeq(memSeq); // 실제 세션의 회원일련번호(memSeq) 사용

        boolean success = boardDao.insertBoard(board);

        if (success) {
            res.sendRedirect(req.getContextPath() + "/board.do");
        } else {
            req.setAttribute("errorMessage", "게시글 등록에 실패했습니다.");
            showWriteForm(req, res);
        }
    }

    // POST /board.do?action=edit
    private void handleEditPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("[BoardServlet] /board.do?action=edit POST 요청 수신");
        try {
            req.setCharacterEncoding("UTF-8");
            int boardSeq = Integer.parseInt(req.getParameter("boardSeq"));
            String title = req.getParameter("title");
            String contents = req.getParameter("contents");
            System.out.println(contents);
            Board board = new Board();
            board.setBoardSeq(boardSeq);
            board.setTitle(title);
            board.setContents(contents);
            
            // TODO: 수정 권한 검사 (로그인한 사용자와 작성자가 같은지)
            boardDao.updateBoard(board);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        res.sendRedirect(req.getContextPath() + "/board.do");
    }
    
    // 세션 저장 관련 처리 메서드
    private void handleSessionSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // JSON body 읽기
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String requestBody = sb.toString();

        // 간단히 파싱(예: userName 필드만)
        // 실무에선 Gson, Jackson같은 라이브러리 사용 권장
        String username = null;
        if (requestBody.contains("username")) {
            username = requestBody.replaceAll(".*\"username\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        }

        if (username != null) {
            HttpSession session = req.getSession();
            System.out.println(username);
            session.setAttribute("loggedInUser", username);
            res.setContentType("application/json");
            res.getWriter().write("{\"status\":\"success\", \"username\":\"" + username + "\"}");
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "username missing");
        }
    }
}