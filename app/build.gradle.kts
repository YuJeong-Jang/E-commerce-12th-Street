plugins {
    // ./gradlew run 을 실행하기 위한 플러그인
    application
}

repositories {
    mavenCentral() // 라이브러리를 다운로드할 저장소
}

// 이 프로젝트가 의존하는 라이브러리 목록
dependencies {
    // === 내장 톰캣 (Tier 1/2) ===
    // 1. 내장 톰캣 코어 (서버 본체)
    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.83")
    
    // 2. 내장 톰캣 JSP 실행 엔진 (JSP 파일을 서블릿으로 변환)
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:9.0.83")
    
    // 3. JSTL (JSP에서 <c:forEach>, <c:if> 같은 태그 사용)
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:2.0.0")

    // === 데이터베이스 (Tier 3) ===
    // 4. MySQL 커넥터 (DB와 통신하는 드라이버)
    implementation("com.mysql:mysql-connector-j:8.0.33")
    
    // 5. Bcrypt (비밀번호 암호화)
    implementation("org.mindrot:jbcrypt:0.4")
}

// 'application' 플러그인 설정
application {
    // ./gradlew run 명령어 입력 시 실행할 Java 클래스 지정
    mainClass.set("com.commerce.member.Main")
}

// Java 17 버전 사용 명시
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// Gradle이 src/main/webapp 폴더(JSP 위치)를 빌드에 포함하도록 설정
sourceSets {
    main {
        resources {
            srcDirs("src/main/webapp")
        }
    }
}


