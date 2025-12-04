package com.commerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

public class DatabaseUtil {
        
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
 
    
    static {
        String env = System.getProperty("env", "local");
        Properties props = new Properties();
        Boolean isLocal = "local".equals(env);

        if (isLocal) {
             // 로컬 properties 파일 로드 (try-with-resources 대신 일반 try)
            InputStream input = null;
            try {
                input = DatabaseUtil.class.getClassLoader()
                    .getResourceAsStream("application-local.properties");
                if (input == null) {
                    throw new RuntimeException("application-local.properties 파일을 찾을 수 없습니다.");
                }
                props.load(input);
                DB_URL = props.getProperty("DB_URL");
                DB_USER = props.getProperty("DB_USER");
                DB_PASSWORD = props.getProperty("DB_PASSWORD");
                System.out.println("로컬 Properties 로드 완료");
            } catch (IOException e) {
                throw new RuntimeException("Properties 파일 로드 실패", e);
            } finally {
                // 수동으로 close
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        System.err.println("InputStream close 실패: " + e.getMessage());
                    }
                }
            }
        } else {
            // k8s 환경변수 사용
            DB_URL = System.getenv("DB_URL");
            DB_USER = System.getenv("DB_USER");
            DB_PASSWORD = System.getenv("DB_PASSWORD");
            // 환경변수로 DB 연결
        }
    }

    // DB 커넥션 가져오기
    public static Connection getConnection() throws SQLException {
        try {
            // MySQL JDBC 드라이버 로드
            System.out.println( " DB_USER : " + DB_USER);
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