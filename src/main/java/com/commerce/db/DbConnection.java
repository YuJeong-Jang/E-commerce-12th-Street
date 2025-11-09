package com.commerce.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * application.properties 파일에서 DB 정보를 읽어
 * MariaDB 커넥션을 제공하는 유틸리티 클래스
 */
public class DbConnection {
    
    // DB 설정을 저장할 Properties 객체
    private static Properties properties = new Properties();
    private static String DB_DRIVER = "org.mariadb.jdbc.Driver";

    // static 블록: 클래스가 로드될 때 한 번만 실행됨
    static {
        try {
            // 1. application.properties 파일 로드
            String resource = "application.properties";
            try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream(resource)) {
                
                if (input == null) {
                    throw new RuntimeException("DB 설정 파일(" + resource + ")을 찾을 수 없습니다.");
                }
                properties.load(input);
            }
            
            // 2. MariaDB JDBC 드라이버 로드
            Class.forName(DB_DRIVER);
            
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC 드라이버를 찾을 수 없습니다.");
            throw new RuntimeException("드라이버 로드 실패", e);
        } catch (Exception e) {
            System.err.println("DB 설정 파일 로드 중 오류 발생");
            throw new RuntimeException("설정 로드 실패", e);
        }
    }

    /**
     * 설정된 정보를 바탕으로 새 DB 커넥션을 반환합니다.
     * @return Connection 객체 (실패 시 null 대신 SQLException 발생)
     * @throws SQLException DB 연결 실패 시
     */
    public static Connection getConnection() throws SQLException {
        try {
            // properties 파일에서 DB 정보를 가져와 연결
            return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            System.err.println("DB 연결에 실패했습니다.");
            System.err.println("URL: " + properties.getProperty("db.url"));
            System.err.println("User: " + properties.getProperty("db.username"));
            throw e; // 호출한 쪽(DAO)에서 예외를 처리하도록 전달
        }
    }
}
