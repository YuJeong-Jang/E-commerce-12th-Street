package com.commerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // 하드코딩된 값 대신, 환경 변수(Environment Variables)에서 값을 읽어옵니다.
    // 이 환경 변수는 3단계의 Deployment.yaml에서 주입됩니다.
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    // DB 커넥션 가져오기
    public static Connection getConnection() throws SQLException {
        try {
            // MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        }

        // 환경 변수가 제대로 주입되었는지 확인 (선택적이지만 좋은 습관입니다)
        if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
            throw new SQLException("데이터베이스 연결 정보(환경 변수)가 설정되지 않았습니다.");
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}