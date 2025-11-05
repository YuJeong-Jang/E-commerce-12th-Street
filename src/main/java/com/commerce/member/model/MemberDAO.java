package com.commerce.member.model;

import com.commerce.member.util.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DAO (Data Access Object)
// 오직 DB 관련 로직만 처리합니다. (Tier 3)
public class MemberDAO {

    // 회원가입 (DB에 INSERT)
    public boolean register(String username, String hashedPassword, String email) {
        String sql = "INSERT INTO member (username, password, email) VALUES (?, ?, ?)";
        
        // try-with-resources (자동으로 Connection, PreparedStatement 종료)
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword); // 암호화된 비밀번호 저장
            pstmt.setString(3, email);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // 아이디 중복(UNIQUE 제약 위반) 등
            System.err.println("[MemberDAO.register] SQL 오류: " + e.getMessage());
            return false;
        }
    }

    // 로그인 (DB에서 SELECT 및 비밀번호 비교)
    public boolean login(String username, String plainPassword) {
        String sql = "SELECT password FROM member WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DB에 저장된 암호화된 비밀번호
                    String hashedPassword = rs.getString("password");
                    
                    // Bcrypt로 입력된 비밀번호와 DB의 해시 비교
                    return BCrypt.checkpw(plainPassword, hashedPassword);
                } else {
                    // 해당 아이디 없음
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[MemberDAO.login] SQL 오류: " + e.getMessage());
            return false;
        }
    }
	public MemberDTO getMemberByUsername(String username) {
        String sql = "SELECT username, email, created_at FROM member WHERE username = ?";
        MemberDTO member = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new MemberDTO();
                    member.setUsername(rs.getString("username"));
                    member.setEmail(rs.getString("email"));
                    member.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (Exception e) {
            System.err.println("[MemberDAO] getMemberByUsername 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }
}

