package com.commerce.board.model;

import java.sql.Date; // java.util.Date 대신 SQL과 호환되는 Date 사용

// DBeaver/Workbench '게시판' 테이블 데이터를 담을 Java 객체 (DTO)
public class Board {
    private int boardSeq;      // 게시판일련번호
    private int memSeq;        // 회원일련번호
    private String title;      // 게시판제목
    private String contents;   // 게시판내용
    private Date rgstYmd;      // 등록일
    
    // (DBeaver 스키마의 나머지 컬럼들은 지금 당장 필요하지 않으므로 생략)

    // Getters and Setters
    public int getBoardSeq() { return boardSeq; }
    public void setBoardSeq(int boardSeq) { this.boardSeq = boardSeq; }
    
    public int getMemSeq() { return memSeq; }
    public void setMemSeq(int memSeq) { this.memSeq = memSeq; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }
    
    public Date getRgstYmd() { return rgstYmd; }
    public void setRgstYmd(Date rgstYmd) { this.rgstYmd = rgstYmd; }
}