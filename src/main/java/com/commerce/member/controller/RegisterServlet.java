package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;
import org.mindrot.jbcrypt.BCrypt;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register.do")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("[RegisterServlet] 회원가입 시도: " + username);
        MemberDAO dao = new MemberDAO();
        boolean success = dao.register(username, hashedPassword, email);

        if (success) {
            System.out.println("[RegisterServlet] 회원가입 성공");
            
            // [경로 수정!]
            // 성공 시 로그인 폼이 있는 메인 페이지(index.jsp)로 이동
            response.sendRedirect("index.jsp");
        } else {
            System.out.println("[RegisterServlet] 회원가입 실패 (아이디 중복 등)");
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
