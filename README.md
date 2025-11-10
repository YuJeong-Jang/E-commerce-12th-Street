# E-commerce-12th-Street

## 프로젝트 구조
- `src/main/java/com/commerce/board` - 게시판
- `src/main/java/com/commerce/member` - 회원관리
- `src/main/resources/application.properties` - 환경설정
- `src/main/webapp` - jsp

## 환경 설정
1. JDK 17 이상 설치
    - mac : brew install microsoftopenjdk@17
    - window : choco install openjdk17 -y (Chocolatey 패키지 사용)

2. Gradle 8.5 설치 (Gretty dependency를 사용하기 위해 버전 고정)
    - mac : brew install gradle
    - window : choco install gradle --version=8.5 -y

## 빌드
- wrapper 파일 생성을 위해 1회 실행
```
$ gradle wrapper --gradle-version 8.5
```
- 생성되는 파일 목록
```
gradlew
gradlew.bat
gradle/wrapper/gradle-wrapper.jar
gradle/wrapper/gradle-wrapper.properties
```
- 빌드
```
$ ./gradlew build
```
- 실행
```
$ ./gradlew run
```
- http://localhost:8080 확인

## 주요 그래들 명령어
### 프로젝트 빌드 (컴파일 + 테스트)
./gradlew build

### JAR 파일만 생성
./gradlew jar

### Fat JAR 생성 (모든 의존성 포함)
./gradlew fatJar

### 애플리케이션 실행
./gradlew run

### 의존성 확인
./gradlew dependencies

### 프로젝트 클린
./gradlew clean

### 클린 후 빌드
./gradlew clean build


톰캣 버전 Embedded Tomcat 10.1.20
