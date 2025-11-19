package com.commerce.member.model;

import com.commerce.database.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date; // java.sql.Timestamp 대신 Date 사용

public class MemberDAO {

    /**
     * 회원가입 (Tier 3)
     * @param memberId 사용자가 입력한 아이디
     * @param hashedPassword 암호화된 비밀번호
     * @param name 사용자 이름
     * @param phone 연락처
     * @return 성공 시 true, 실패 시 false
     */
    public boolean register(String memberId, String hashedPassword, String name, String phone) {
        // DBeaver 스키마 (한글 컬럼명) 기준 SQL
        String sql = "INSERT INTO Member (memberId, pwd, name, phone, rgstYmd, delYn) VALUES (?, ?, ?, ?, CURDATE(), 0)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberId);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, name);
            pstmt.setString(4, phone);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("[MemberDAO] register 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 로그인 (Tier 3)
     * @param memberId 사용자가 입력한 아이디
     * @param plainPassword 사용자가 입력한 원본 비밀번호
     * @return 성공 시 true, 실패 시 false
     */
    public boolean login(String memberId, String plainPassword) {
        // DBeaver 스키마 (한글 컬럼명) 기준 SQL
        String sql = "SELECT pwd FROM Member WHERE memberId = ? AND delYn = 0";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("pwd");
                    // DB에 저장된 해시값과 입력된 평문 비밀번호를 비교
                    return BCrypt.checkpw(plainPassword, hashedPassword);
                }
            }
        } catch (Exception e) {
            System.err.println("[MemberDAO] login 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 마이페이지 정보 조회 (Tier 3)
     * @param memberId 세션에 저장된 사용자 아이디
     * @return 회원 정보가 담긴 DTO 객체 (없으면 null)
     */
    public MemberDTO getMemberByUsername(String memberId) {
        // DBeaver 스키마 (한글 컬럼명) 기준 SQL
        String sql = "SELECT memSeq, memberId, name, phone, rgstYmd FROM Member WHERE memberId = ?";
        MemberDTO member = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new MemberDTO();
                    member.setMemberId(rs.getString("memberId"));
                    member.setName(rs.getString("name"));
                    member.setPhone(rs.getString("phone"));
                    member.setRgstYmd(rs.getDate("rgstYmd")); // Timestamp 대신 Date 사용
                    member.setMemSeq(rs.getInt("memSeq"));
                }
            }
        } catch (Exception e) {
            System.err.println("[MemberDAO] getMemberByUsername 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }
}
