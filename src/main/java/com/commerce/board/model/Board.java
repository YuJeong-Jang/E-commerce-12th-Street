package com.commerce.board.model;

import java.sql.Date; // java.util.Date 대신 SQL과 호환되는 Date 사용

public class Board {
    private int boardSeq;
    private int memSeq;
    private String title;
    private String contents;
    private Date rgstYmd;
    private Date modYmd;
    private Date delYmd;
    private boolean delYn;

    // 기본 생성자
    public Board() {}

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
    
    public Date getModYmd() { return modYmd; }
    public void setModYmd(Date modYmd) { this.modYmd = modYmd; }
    
    public Date getDelYmd() { return delYmd; }
    public void setDelYmd(Date delYmd) { this.delYmd = delYmd; }
    
    public boolean isDelYn() { return delYn; }
    public void setDelYn(boolean delYn) { this.delYn = delYn; }
}
