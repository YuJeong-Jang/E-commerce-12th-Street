package com.commerce.member.controller;

import com.commerce.member.model.MemberDAO;
import org.mindrot.jbcrypt.BCrypt; // Bcrypt 라이브러리

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

        // DBeaver 스키마에 맞게 파라미터 받기
        String memberId = request.getParameter("memberId");
        String password = request.getParameter("pwd");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone"); // 'email' 대신 'phone'을 받음

        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("[RegisterServlet] 회원가입 시도: " + memberId);
        MemberDAO dao = new MemberDAO();
        
        // [수정] DAO의 register 메서드 호출 (인자 4개)
        boolean success = dao.register(memberId, hashedPassword, name, phone);

        if (success) {
            System.out.println("[RegisterServlet] 회원가입 성공");
            // 회원가입 성공 시 로그인 폼이 있는 메인(index) 페이지로 이동
            response.sendRedirect("index.jsp");
        } else {
            System.out.println("[RegisterServlet] 회원가입 실패 (아이디 중복 등)");
            // 실패 시 에러 파라미터를 가지고 다시 회원가입 페이지로
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
