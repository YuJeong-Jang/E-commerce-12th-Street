package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * /login.do POST 요청을 처리하는 컨트롤러 (서블릿)
 * (index.jsp의 로그인 폼에서 호출됨)
 */
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // 1. View(index.jsp)에서 폼 데이터 받기 (name 속성 기준)
        String memberId = request.getParameter("memberId");
        String password = request.getParameter("pwd");

        System.out.println("[LoginServlet] 로그인 시도: " + memberId);
        
        // 2. Model(DAO)에 로직 위임
        MemberDAO dao = new MemberDAO();
        boolean success = dao.login(memberId, password); 

        // 3. 로직 결과에 따라 View(페이지) 결정
        if (success) {
            // 로그인 성공
            System.out.println("[LoginServlet] 로그인 성공");
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", memberId); // 세션에 "로그인한 유저 아이디" 저장
            
            // 메인 페이지(index.jsp)로 리다이렉트
            response.sendRedirect("index.jsp");
        } else {
            // 로그인 실패
            System.out.println("[LoginServlet] 로그인 실패");
            
            // 에러 플래그와 함께 메인 페이지(index.jsp)로 리다이렉트
            response.sendRedirect("index.jsp?error=1");
        }
    }
}
