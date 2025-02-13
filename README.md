# 우주라이크🌌

배포링크 : [링크](https://www.wyl.seoez.site/) <br>
노션페이지 : [링크](https://volcano-plutonium-0bf.notion.site/LIKELION-17d25a7b9d0c80e18460fa0087f64e91?pvs=4)
시연 영상 : [링크](https://youtu.be/KrzRz4Y0wHg)

<br>

## 프로젝트 개요 

### 프로젝트 목적
1. 사용자의 취향에 맞추어 그날의 보고 싶은 콘텐츠를 추천하기 위한 서비스
2. 사용자가 좋아하는 콘텐츠들을 빠르게 제공하는 서비스

<br>

### 배경
 프로젝트가 시작된 이유와 현재 문제점
  1. 쏟아지는 컨텐츠 중 내가 좋아할 만한, 내가 보고 싶은 콘텐츠를 찾기 어려움.
  2. 영화에 대한 리뷰를 작성하여 정보를 주고 받는 커뮤니티의 필요성을 느낌.
  3. 취향에 맞는 다른 매체의 콘텐츠도 추천 받고 싶음.

<br>

### 프로젝트 팀 구성 및 업무 분담
| [손서연(팀장)](https://github.com/seoyeonson) | [김소영](https://github.com/whale22) | [김규일](https://github.com/daehyuk1231) | [방대혁](https://github.com/System-out-gyuil)| [최제인](https://github.com/JeinChoi) | 
| --- | --- | --- | --- | --- |
| 회원 로그인, 로그아웃, 서버 배포 | 영화 시리즈 추천 | 영화 리뷰, 상세 | 채팅, 챗봇 | 영화 검색 | 
   
<br>   

### 기술스택

BackEnd
- Spring Boot 3.3.1, JAVA 23, JPA
- MySQL8.0, MongoDB 8.0.4
- Elasticsearch, logstash, kibana

<br>

FrontEnd
- React
- Tailwind

<br>

Infra
- Naver Cloud 
- vercel
- Github Action
- Docker 

<br>

Tool
- Intellij, Github, Visual Studio Code

<br>

### 시스템 아키텍처
![시스템 아키텍처](https://github.com/user-attachments/assets/f9c5aae0-ec6a-4181-bd16-161e48db940d)


### 배포 아키텍처
![배포 아키텍처](https://github.com/user-attachments/assets/8b835adb-20bf-4e54-b467-4bce6ddac35c)


### 주요 기능 

#### 회원
- 로그인/로그아웃,회원가입
  - JWT, Spring Security를 통해 기능 구현

#### 추천 
- 사용자가 선호하는 장르에 따라 영화 추천
  - Elasticsearch index와 검색문 설계

#### 검색
- 영화 제목, 감독을 키워드로 검색
  - nori, 엔그램 분석기 적용

#### 영화 상세
- 영화 상세 정보, 리뷰 작성
  - 회원만 리뷰 작성 가능하도록 구현
  - 동영상 링크를 통해 예고편 조회 가능

#### 챗봇
- 영화 장르 키워드에 따라 영화 추천
  - OpenAI 활용

<br>

### 트러블 슈팅


<br>

### 의사 결정 사항 
1. MongoDB 사용의 적합성
   이슈 발생 과정
   - 프로젝트 기획 중 DB 선택 과정에서 이슈 발생
   - 팀원 모두 NoSQL 사용 경험이 부족하여 이슈 발생
   
    학습 내용
    - NoSQL의 종류 및 장단점 ( 문서 지향, key-value store, column-fammily store, 그래프 )
    - 스프링 - MongoDB 연결 설정 및 사용
   
    선택 이유
     - userId를 통하여 사용자의 이전 대화를 제공
     - 서비스를 이용하는 이용자가 다수가 될 경우 비동기적으로 서비스가 제공되어야 된다고 생각이 들어 NoSQL을 사용
     - NoSQL 중 MongoDB(userId 인덱스)와 Redis(userId key)를 고민
     - 구현 기능중 챗봇과의 통신과정에서 TMDB API에서 가져온 JSON 데이터를 사용하고, 인메모리 형태로 Redis는 저장되기 때문에 서버가 종료되면 DB가 삭제되기 때문에 MongoDB를 선택
     - 
   
<br>
