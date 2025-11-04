package com.commerce.member;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * ./gradlew run 명령어로 내장 톰캣 서버를 실행하는 메인 클래스입니다.
 * 3-Tier 아키텍처의 Tier 1(View)과 Tier 2(Controller)를 호스팅합니다.
 */
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        System.out.println("=== 내장 톰캣 서버 시작 ===");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector(); // HTTP 커넥터 초기화

        // [경로 수정]
        // 1stpjt 루트에서 src/main/webapp을 바라봅니다.
        String webappDirLocation = "src/main/webapp/";
        String webappAbsolutePath = new File(webappDirLocation).getAbsolutePath();
        
        System.out.println("웹 앱 경로(Context Path): " + webappAbsolutePath);

        // [핵심 수정 1]
        // addWebapp를 사용해야 톰캣이 JSP 엔진을 정상적으로 초기화합니다. (500 에러 해결)
        Context context = tomcat.addWebapp("", webappAbsolutePath);

        // [핵심 수정 2]
        // 404 에러를 해결하기 위해, 컴파일된 클래스 경로를 수동으로 추가합니다.
        // 1. 톰캣이 컴파일된 .class 파일들을 찾을 경로를 지정합니다.
        File additionWebInfClasses = new File("build/classes/java/main");

        // 2. 톰캣의 리소스 설정을 가져옵니다.
        WebResourceRoot resources = new StandardRoot(context);
        
        // 3. .class 파일 경로를 톰캣 리소스의 "/WEB-INF/classes"에 마운트(연결)합니다.
        DirResourceSet resourceSet = new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/");
        resources.addPreResources(resourceSet);
        
        // 4. 톰캣 컨텍스트에 수정된 리소스 설정을 다시 적용합니다.
        context.setResources(resources);

        // [수정] addWebapp를 사용했으므로, initWebappDefaults는 절대 호출하면 안 됩니다.
        // (호출 시 "Child name [default] is not unique" 에러 발생)

        // 5. 톰캣 서버 시작 및 대기
        tomcat.start();
        System.out.println("서버가 실행 중입니다. 접속을 기다립니다...");
        System.out.println("URL: http://localhost:" + PORT + "/register.jsp");

        tomcat.getServer().await();
    }
}


