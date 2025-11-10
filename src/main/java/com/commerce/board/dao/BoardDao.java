package com.commerce.board.dao;

import com.commerce.board.model.Board;
import com.commerce.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {

    /**
     * 게시판 목록 조회 (dashboard.jsp)
     */
    public List<Board> getBoardList() {
        List<Board> boardList = new ArrayList<>();
        // ERD의 rgstYmd (등록일) 기준으로 내림차순 정렬
        String sql = "SELECT boardSeq, memSeq, title, contents, rgstYmd FROM Board WHERE delYn = false ORDER BY rgstYmd DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Board board = new Board();
                board.setBoardSeq(rs.getInt("boardSeq"));
                board.setMemSeq(rs.getInt("memSeq"));
                board.setTitle(rs.getString("title"));
                board.setContents(rs.getString("contents"));
                board.setRgstYmd(rs.getDate("rgstYmd"));
                boardList.add(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boardList;
    }

    /**
     * 게시글 등록 (write.jsp에서 사용)
     */
    public boolean insertBoard(Board board) {
        // boardSeq는 auto-increment, delYn은 default false 가정
        // rgstYmd, modYmd는 DB에서 NOW() 또는 SYSDATE() 사용
        String sql = "INSERT INTO Board (memSeq, title, contents, rgstYmd, modYmd) VALUES (?, ?, ?, NOW(), NOW())";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, board.getMemSeq()); // TODO: 실제로는 세션에서 회원 ID를 가져와야 함
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContents());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 게시글 수정 (dashboard.jsp의 모달에서 사용)
     */
    public boolean updateBoard(Board board) {
        String sql = "UPDATE Board SET title = ?, contents = ?, modYmd = NOW() WHERE boardSeq = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContents());
            pstmt.setInt(3, board.getBoardSeq());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 게시글 삭제 (논리적 삭제: delYn = true로 변경)
     */
    public boolean deleteBoard(int boardSeq) {
        String sql = "UPDATE Board SET delYn = true, delYmd = NOW() WHERE boardSeq = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, boardSeq);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
