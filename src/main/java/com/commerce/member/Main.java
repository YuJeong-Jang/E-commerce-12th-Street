package com.commerce.member;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet; 
import org.apache.catalina.webresources.StandardRoot;

// ✅ [추가] JSP 엔진(Jasper) 및 서블릿 설정을 위한 클래스
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.Wrapper;

import java.io.File;
import java.net.URL;

public class Main {
    
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        System.out.println("=== 내장 톰캣 서버 시작 ===");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        // ⚠️ [변경] addContext를 사용하되, JSP 설정을 위해 StandardContext로 캐스팅
        // docBase를 임시로 "." (현재 디렉토리)로 설정합니다.
        Context context = tomcat.addContext("", new File(".").getAbsolutePath());

        // ⚠️ [변경] 리소스 경로 설정 (이전과 동일)
        WebResourceRoot resources = new StandardRoot(context);
        URL location = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String locationStr = (location != null) ? location.toString() : "";

        if (locationStr.endsWith(".jar")) {
            System.out.println("[Main.java] Uber-JAR 모드로 실행");
            String jarPath = location.toURI().getPath();
            
            // 1. JSP 리소스 추가
            resources.addPreResources(new JarResourceSet(resources, "/", jarPath, "/webapp"));
            // 2. 클래스(서블릿) 리소스 추가
            resources.addPreResources(new JarResourceSet(resources, "/WEB-INF/classes", jarPath, "/"));

        } else {
            System.out.println("[Main.java] 로컬(IDE) 모드로 실행");
            
            // 1. JSP 리소스 추가
            File webappDir = new File("src/main/webapp/");
            resources.addPreResources(new DirResourceSet(resources, "/", webappDir.getAbsolutePath(), "/"));

            // 2. 클래스(서블릿) 리소스 추가
            File classesDir = new File("build/classes/java/main");
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
        }
        
        context.setResources(resources);

        // --- ✅ 404 해결을 위한 핵심 코드 ---
        
        // 1. 톰캣이 @WebServlet 어노테이션을 스캔하도록 설정
        // (이것이 LoginServlet [cite: yujeong-jang/e-commerce-12th-street/E-commerce-12th-Street-00311d7c8f77d5c929f853878b3aee24a1daace8/src/main/java/com/commerce/member/controller/LoginServlet.java] 등을 찾게 만듭니다)
        context.addLifecycleListener(new ContextConfig());

        // 2. JSP 엔진(Jasper)을 수동으로 등록
        // "jsp"라는 이름의 서블릿을 만들고,
        Wrapper jspServlet = context.createWrapper();
        jspServlet.setName("jsp");
        // 이 서블릿의 실제 클래스는 톰캣의 Jasper 엔진 클래스라고 알려줍니다.
        jspServlet.setServletClass("org.apache.jasper.servlet.JspServlet");
        jspServlet.setLoadOnStartup(1);
        
        // "jsp" 서블릿을 컨텍스트에 추가
        context.addChild(jspServlet);

        // 3. ".jsp" 확장자를 가진 모든 요청을 "jsp" 서블릿이 처리하도록 매핑
        context.addServletMappingDecoded("*.jsp", "jsp");
        // ------------------------------------

        // 톰캣 서버 시작
        tomcat.start();
        System.out.println("서버가 실행 중입니다. 접속을 기다립니다...");
        System.out.println("URL: http://YOUR_VPS_IP:" + PORT + "/index.jsp");

        tomcat.getServer().await();
    }
}
