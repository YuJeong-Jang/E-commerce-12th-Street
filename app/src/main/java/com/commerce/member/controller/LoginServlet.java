package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("[LoginServlet] 로그인 시도: " + username);

        // Tier 3 (Model) 호출
        MemberDAO dao = new MemberDAO();
        boolean success = dao.login(username, password); // DAO가 암호화 비교(bcrypt) 처리

        if (success) {
            System.out.println("[LoginServlet] 로그인 성공");
            // 로그인 성공 시, 세션(Session) 생성
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", username);
            
            // 메인 페이지(index.jsp)로 이동
            response.sendRedirect("index.jsp");
        } else {
            System.out.println("[LoginServlet] 로그인 실패");
            // 실패 시 에러 파라미터와 함께 로그인 페이지로 이동
            response.sendRedirect("login.jsp?error=1");
        }
    }
}

