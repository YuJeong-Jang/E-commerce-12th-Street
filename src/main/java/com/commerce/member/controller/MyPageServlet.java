package com.commerce.member.controller;

// ★★★★★ 스크린샷에 보이는 DAO/DTO 경로로 수정 ★★★★★
import com.commerce.member.model.MemberDAO;
import com.commerce.member.model.MemberDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 마이페이지(/mypage.do) 요청을 처리하는 서블릿 (Controller)
 */
@WebServlet("/mypage.do")
public class MyPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 세션에서 현재 로그인된 사용자 ID 가져오기
        HttpSession session = request.getSession(false); 
        String memberId = null;

        if (session != null) {
            memberId = (String) session.getAttribute("loggedInUser");
        }

        // 2. 로그인 상태 확인
        if (memberId == null) {
            response.sendRedirect("index.jsp");
            return; 
        }

        // 3. (Tier 3: Model) DAO를 통해 DB에서 회원 정보 조회
        System.out.println("[MyPageServlet] " + memberId + " 님 정보 조회");
        MemberDAO dao = new MemberDAO();
        MemberDTO memberInfo = dao.getMemberByUsername(memberId); 

        if (memberInfo == null) {
            System.err.println("[MyPageServlet] 세션 유저(" + memberId + ")의 DB 정보를 찾을 수 없음");
            response.sendRedirect("index.jsp?error=2"); 
            return;
        }

        // 4. (Tier 1: View) JSP로 데이터 전달
        request.setAttribute("memberInfo", memberInfo);

        // 5. mypage.jsp로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("mypage.jsp");
        dispatcher.forward(request, response);
    }
}