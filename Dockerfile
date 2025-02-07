# 첫 번째 스테이지: 빌드 스테이지
FROM gradle:8.11.1-jdk21 as builder
# Gradle 8.11.1 + JDK 21

# Java 23 설치
# OpenJDK 23 설치
USER root
RUN apt-get update && apt-get install -y wget \
    && wget https://download.java.net/openjdk/jdk23/ri/openjdk-23_linux-x64_bin.tar.gz \
    && tar -xzf openjdk-23_linux-x64_bin.tar.gz -C /opt \
    && rm openjdk-23_linux-x64_bin.tar.gz \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
    # 캐시 정리

# 환경 변수 설정
ENV JAVA_HOME=/usr/lib/jvm/java-23-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

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
# 실행 환경은 GraalVM 23 유지

# 작업 디렉토리 설정
WORKDIR /app

# Java 버전 확인 (디버깅 용)
RUN java -version

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
