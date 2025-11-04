package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;
import org.mindrot.jbcrypt.BCrypt; // Bcrypt 라이브러리

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ".do" 확장자 패턴은 서블릿임을 명확히 구분해줍니다.
@WebServlet("/register.do")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // 비밀번호 암호화 (jbcrypt)
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("[RegisterServlet] 회원가입 시도: " + username);

        // Tier 3 (Model) 호출
        MemberDAO dao = new MemberDAO();
        boolean success = dao.register(username, hashedPassword, email);

        if (success) {
            System.out.println("[RegisterServlet] 회원가입 성공");
            // 성공 시 로그인 페이지로 이동
            response.sendRedirect("login.jsp");
        } else {
            System.out.println("[RegisterServlet] 회원가입 실패 (아이디 중복 등)");
            // 실패 시 (간단히) 회원가입 페이지로 다시 보냄
            response.sendRedirect("register.jsp?error=1");
        }
    }
}

