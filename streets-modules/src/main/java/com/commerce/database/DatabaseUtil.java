package com.commerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

public class DatabaseUtil {

    // 하드코딩된 값 대신, 환경 변수(Environment Variables)에서 값을 읽어옵니다.
    // 이 환경 변수는 3단계의 Deployment.yaml에서 주입됩니다.
    private static final Properties props = new Properties();
    // 뒤에 local은 default값으로 값이 null exception 대비
    static String env = System.getProperty("env", "local");
    static String fileNm = "application-" + env + ".properties";
    static {
        try (InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream(fileNm)) {
            if (is == null) {
                throw new RuntimeException(fileNm + "파일을 찾을 수 없습니다.");
            }
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Boolean isLocal = "local".equals(props.getProperty("spring.profiles.active"));
    
    private static final String DB_URL = isLocal ? props.getProperty("DB_URL") : System.getenv("DB_URL"); // DB 주소 및 DB 이름
    private static final String DB_USER = isLocal ? props.getProperty("DB_USER") : System.getenv("DB_USER"); // DB 사용자 아이디
    private static final String DB_PASSWORD = isLocal ? props.getProperty("DB_PASSWORD") : System.getenv("DB_PASSWORD"); // DB 비밀번호

    // DB 커넥션 가져오기
    public static Connection getConnection() throws SQLException {
        try {
            // MySQL JDBC 드라이버 로드
            System.out.println("isLocal : " + isLocal + " DB_USER : " + DB_USER);
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