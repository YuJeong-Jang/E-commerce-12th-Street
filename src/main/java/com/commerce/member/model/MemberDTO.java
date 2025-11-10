package com.commerce.member.model;

import java.sql.Timestamp;

// DB의 member 테이블 데이터를 담기 위한 "Data Transfer Object" (데이터 그릇)
public class MemberDTO {
    private String username;
    private String email;
    private Timestamp createdAt;

    // Getter와 Setter (JSP/Servlet에서 데이터를 읽고 쓰기 위해 필수)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

