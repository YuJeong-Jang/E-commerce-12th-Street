package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;
import com.commerce.member.model.MemberDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/mypage.do")
public class MyPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. 세션에서 로그인 정보 확인
        HttpSession session = request.getSession(false);
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("loggedInUser");
        }

        if (username == null) {
            // 2. [실패] 로그인 안 한 사용자 -> 메인 페이지(로그인폼)로 쫓아냄
            System.out.println("[MyPageServlet] 비로그인 사용자 접근. index.jsp로 리다이렉트");
            response.sendRedirect("index.jsp");
            return;
        }

        // 3. [성공] 로그인 한 사용자 -> DB에서 정보 조회
        System.out.println("[MyPageServlet] " + username + " 님 정보 조회");
        MemberDAO dao = new MemberDAO();
        MemberDTO memberInfo = dao.getMemberByUsername(username);

        // 4. 조회한 정보를 request 객체에 "memberInfo"라는 이름으로 저장
        request.setAttribute("memberInfo", memberInfo);

        // 5. request 객체를 mypage.jsp (View)로 전달 (forward)
        RequestDispatcher dispatcher = request.getRequestDispatcher("mypage.jsp");
        dispatcher.forward(request, response);
    }
}

