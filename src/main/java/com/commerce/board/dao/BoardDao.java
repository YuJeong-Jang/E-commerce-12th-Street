package com.commerce.board.dao;

// ★★★★★ [오류 2 해결] DB 연결 클래스의 올바른 경로로 수정
import com.commerce.member.util.DatabaseUtil; 
import com.commerce.board.model.Board; // 스크린샷의 DTO 파일 이름 'Board.java'

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date; // java.sql.Date 사용
import java.util.ArrayList;
import java.util.List;

// ★★★★★ [오류 3 해결] 클래스 문법 수정 (파일 끝 중복 코드 제거)
public class BoardDAO { 

    /**
     * '게시판'의 모든 글 목록을 조회합니다. (삭제유무 0인 것만)
     * @return 게시글 DTO 리스트
     */
    public List<Board> getBoardList() { 
        
        // DBeaver 스키마의 한글 컬럼명 사용
        String sql = "SELECT 게시판일련번호, 회원일련번호, 게시판제목, 등록일 " +
                     "FROM 게시판 WHERE 삭제유무 = 0 ORDER BY 게시판일련번호 DESC";
        
        List<Board> list = new ArrayList<>();

        // ★★★★★ [오류 2 해결] DB 연결 클래스 이름 수정
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Board board = new Board(); 
                board.setBoardSeq(rs.getInt("게시판일련번호"));
                board.setMemSeq(rs.getInt("회원일련번호")); 
                board.setTitle(rs.getString("게시판제목"));
                board.setRgstYmd(rs.getDate("등록일"));
                list.add(board);
            }
        } catch (Exception e) {
            System.err.println("[BoardDAO] getBoardList 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // DBeaver 스키마에 맞게 수정한 insertBoard
    public boolean insertBoard(Board board) {
        String sql = "INSERT INTO 게시판 (회원일련번호, 게시판제목, 게시판내용, 등록일, 수정일, 삭제유무) " +
                     "VALUES (?, ?, ?, NOW(), NOW(), 0)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, board.getMemSeq()); 
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContents());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("[BoardDAO] insertBoard 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // DBeaver 스키마에 맞게 수정한 updateBoard
    public boolean updateBoard(Board board) {
        String sql = "UPDATE 게시판 SET 게시판제목 = ?, 게시판내용 = ?, 수정일 = NOW() WHERE 게시판일련번호 = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContents());
            pstmt.setInt(3, board.getBoardSeq());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("[BoardDAO] updateBoard 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // DBeaver 스키마에 맞게 수정한 deleteBoard (논리적 삭제)
    public boolean deleteBoard(int boardSeq) {
        String sql = "UPDATE 게시판 SET 삭제유무 = 1, 삭제일 = NOW() WHERE 게시판일련번호 = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, boardSeq);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("[BoardDAO] deleteBoard 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}