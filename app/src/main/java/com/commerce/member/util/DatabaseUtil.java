package com.commerce.member.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    
    // TODO: 여기에 본인의 MySQL DB 연결 정보를 입력하세요.
    // (Docker Compose로 띄울 DB의 정보 또는 로컬 DB 정보)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb"; // DB 주소 및 DB 이름
    private static final String DB_USER = "myuser"; // DB 사용자 아이디
    private static final String DB_PASSWORD = "mypassword"; // DB 비밀번호

    // DB 커넥션 가져오기
    public static Connection getConnection() throws SQLException {
        try {
            // MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        }
        
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

