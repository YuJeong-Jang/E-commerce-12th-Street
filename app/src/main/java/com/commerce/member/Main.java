package com.commerce.member;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class Main {
    // 서버가 사용할 포트
    private static final int PORT = 8080;

    // ./gradlew run 시 이 main 메소드가 실행됩니다.
    public static void main(String[] args) throws Exception {
        System.out.println("=== 내장 톰캣 서버 시작 ===");
        
        // 1. 톰캣 객체 생성
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        
        // 톰캣 9 이상에서 HTTP 커넥터를 초기화 (오류 방지)
        tomcat.getConnector(); 

        // 2. JSP 파일이 위치할 폴더(webapp) 경로 설정
        // (Gradle로 실행할 때와 IDE로 실행할 때 경로가 다를 수 있어 보정)
        String webappDirLocation = "app/src/main/webapp/";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
             webappDirLocation = "src/main/webapp/"; // IDE 실행 경로 보정
        }
        
        String webappAbsolutePath = new File(webappDirLocation).getAbsolutePath();
        System.out.println("웹 앱 경로(Context Path): " + webappAbsolutePath);

        // 3. 톰캣에 웹 앱(JSP) 컨텍스트 추가
        // contextPath="" (루트 경로, 즉 /)로, docBase=webappDir (JSP 파일 위치)로 설정
        Context context = tomcat.addWebapp("", webappAbsolutePath);
        
        // 4. JSP가 컴파일될 수 있도록 톰캣 기본 설정 적용
        // (이것이 없으면 JSP가 실행되지 않고 소스 코드가 노출됨)
        Tomcat.initWebappDefaults(context);

        // 5. 톰캣 서버 시작 및 대기
        tomcat.start();
        System.out.println("서버가 실행 중입니다. 접속을 기다립니다...");
        System.out.println("URL: http://localhost:" + PORT + "/login.jsp");
        
        // 서버가 바로 종료되지 않도록 대기 상태 유지
        tomcat.getServer().await();
    }
}


