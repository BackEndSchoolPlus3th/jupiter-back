# 첫 번째 스테이지: 빌드 스테이지
# JDK 23을 기본으로 제공
FROM eclipse-temurin:23-jdk as builder

# Gradle 설치
RUN apt-get update && apt-get install -y curl unzip \
    && curl -s https://services.gradle.org/distributions/gradle-8.11.1-bin.zip -o gradle.zip \
    && unzip gradle.zip -d /opt/gradle \
    && rm gradle.zip
ENV PATH="/opt/gradle/gradle-8.11.1/bin:$PATH"

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 및 Gradle 래퍼 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Gradle 래퍼 실행 권한 부여
RUN chmod +x ./gradlew

# 종속성 설치
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew build --no-daemon

# 두 번째 스테이지: 실행 스테이지
FROM ghcr.io/graalvm/jdk-community:23

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
