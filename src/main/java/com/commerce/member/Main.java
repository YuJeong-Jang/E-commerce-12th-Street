package com.commerce.member;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import com.commerce.board.servlet.BoardServlet;

import java.io.File;
import java.util.Properties;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. 서버 설정 읽기
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("application.properties 파일을 찾을 수 없습니다.");
                return;
            }
            props.load(input);
        }
        
        int port = Integer.parseInt(props.getProperty("server.port", "8080"));
        System.out.println("=== Embedded Tomcat 서버 시작 ===");
        
        // 2. Tomcat 인스턴스 생성
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();
        
        // 3. 웹 애플리케이션 컨텍스트 설정
        String webappDirLocation = "src/main/webapp/";
        Context context = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        
        // 4. 서블릿 수동 등록 (중요!)
        System.out.println("=== 서블릿 등록 중 ===");
        
        // BoardServlet 등록
        Tomcat.addServlet(context, "boardServlet", new BoardServlet());
        context.addServletMappingDecoded("/board/*", "boardServlet");
        
        System.out.println("BoardServlet 등록 완료: /board/*");
        
        // 5. 서버 시작
        tomcat.start();
        System.out.println("서버 포트: " + port);
        System.out.println("웹 루트: " + new File(webappDirLocation).getAbsolutePath());
        System.out.println("URL: http://localhost:" + port);
        System.out.println("게시판: http://localhost:" + port + "/board/dashboard");
        System.out.println("서버가 성공적으로 실행되었습니다. (중지: CTRL+C)");
        
        tomcat.getServer().await();
    }
}
