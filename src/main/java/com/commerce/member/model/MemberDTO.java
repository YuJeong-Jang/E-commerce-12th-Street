package com.commerce.member.model;

import java.sql.Date;

// DBeaver '회원' 테이블 데이터를 담는 Java 객체 (DTO)
public class MemberDTO {
    
    // DBeaver 스키마 기준 (`회원` 테이블)
    private int memberSeq;    // 회원일련번호
    private String memberId;  // 회원아이디
    private String name;      // 회원명
    private String phone;     // 연락처
    private Date rgstYmd;     // 가입일

    // Getters and Setters
    
    // ★★★★★ [오류 수정] BoardServlet이 필요로 하는 getMemberSeq() 추가 ★★★★★
    public int getMemberSeq() { 
        return memberSeq; 
    }
    public void setMemberSeq(int memberSeq) { 
        this.memberSeq = memberSeq; 
    }

    public String getMemberId() { 
        return memberId; 
    }
    public void setMemberId(String memberId) { 
        this.memberId = memberId; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public Date getRgstYmd() { 
        return rgstYmd; 
    }
    public void setRgstYmd(Date rgstYmd) { 
        this.rgstYmd = rgstYmd; 
    }
}