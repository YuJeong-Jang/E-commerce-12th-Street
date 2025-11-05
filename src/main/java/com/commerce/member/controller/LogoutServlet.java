package com.commerce.member.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout.do")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("[LogoutServlet] 로그아웃 시도");

        // 세션 무효화(invalidate)
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 않음
        if (session != null) {
            session.invalidate();
        }

        // 로그아웃 후 로그인 페이지로 이동
        response.sendRedirect("index.jsp");
    }
}

