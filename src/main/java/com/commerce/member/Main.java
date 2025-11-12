// package com.commerce.member; // [중요] 이 패키지 선언이 build.gradle과 일치해야 합니다.

// import org.apache.catalina.Context;
// import org.apache.catalina.WebResourceRoot;
// import org.apache.catalina.startup.Tomcat;
// import org.apache.catalina.webresources.DirResourceSet;
// import org.apache.catalina.webresources.StandardRoot;

// import java.io.File;

// public class Main {
    
//     private static final int PORT = 8080;

//     public static void main(String[] args) throws Exception {
//         System.out.println("=== 내장 톰캣 서버 시작 ===");

//         // 1. 톰캣 객체 생성
//         Tomcat tomcat = new Tomcat();
//         tomcat.setPort(PORT);

//         // 톰캣 9+ 이상 HTTP 커넥터 초기화
//         tomcat.getConnector(); 

//         // 2. JSP 파일이 위치할 폴더(webapp) 경로 설정
//         // [수정] 루트 프로젝트 구조에 맞게 경로 보정
//         String webappDirLocation = "src/main/webapp/";
//         String webappAbsolutePath = new File(webappDirLocation).getAbsolutePath();

//         System.out.println("웹 앱 경로(Context Path): " + webappAbsolutePath);

//         // 3. 톰캣에 웹 앱(JSP) 컨텍스트 추가
//         // [수정] addWebapp 사용 (JSP 엔진 자동 로드)
//         Context context = tomcat.addWebapp("", webappAbsolutePath);
        
//         // 4. [404 해결] 컴파일된 서블릿 클래스 경로 추가
//         // Gradle이 컴파일한 .class 파일들이 있는 위치를 톰캣에 알려줌
//         File additionWebInfClasses = new File("build/classes/java/main");
//         WebResourceRoot resources = new StandardRoot(context);
//         resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//                 additionWebInfClasses.getAbsolutePath(), "/"));
//         context.setResources(resources);

//         // 5. 톰캣 서버 시작 및 대기
//         tomcat.start();
//         System.out.println("서버가 실행 중입니다. 접속을 기다립니다...");
//         System.out.println("URL: http://localhost:" + PORT + "/register.jsp");

//         tomcat.getServer().await();
//     }
// }
