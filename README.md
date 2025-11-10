# 간이 게시판 (개인 프로젝트)
![Java](https://img.shields.io/badge/Java-17-007396?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Enabled-6DB33F?logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql)
![MyBatis](https://img.shields.io/badge/MyBatis-Mapper-orange)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger%20UI-85EA2D?logo=swagger)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle)

## ⚙ 개요
Spring Boot 기반의 서버 사이드 렌더링 웹 애플리케이션입니다. Java 17과 Gradle을 사용하여 개발했으며, 뷰는 JSP로 구성되어 있고 데이터 접근은 MyBatis를 통해 MySQL과 연동됩니다. 인증·인가에는 Spring Security를 사용하고 API 문서화는 SpringDoc(Swagger)을 적용했습니다.

<p align="center">
  <img src="https://github.com/user-attachments/assets/77008399-f3d3-489e-9523-5d796c5cd0bc" width="23%">
  <img src="https://github.com/user-attachments/assets/30632bf3-10a7-44d8-91e3-e4f86633edf8" width="23%">
  <img src="https://github.com/user-attachments/assets/89d4c01f-31c9-4114-a18e-3585b7663f98" width="23%">
  <img src="https://github.com/user-attachments/assets/cdb242a4-c156-433a-9aef-c779dae159e9" width="23%">
</p>

## 🔧 기술 스택
- **OS:** Windows  
- **Language & Platform:** Java 17, Spring Boot 3.3.4, Spring MVC  
- **View:** JSP, JSTL  
- **Auth:** Spring Security, BCrypt  
- **DB & Access:** MySQL, MyBatis (Annotation 기반 Mapper)  
- **Build/Docs:** Gradle (Wrapper), SpringDoc (OpenAPI / Swagger UI)


## 📂 구조 개요 (Components)

### ✅ Client
- JSP 기반 서버 사이드 렌더링  
- 정적 리소스 제공  
- REST API 호출 기반 흐름 지원  

### ✅ Server (Spring Boot)

**Controllers**  
- `PageController`  
- `MenuRestController`  
- `UserController`  

**Services**  
- `MenuRestService`  
- `UserService`  
- `UserDetailsServiceImpl` (인증 처리)  

**Mappers (MyBatis)**  
- `MenuRestMapper`  
- `UserMapper`  

**Entities**  
- `Menu`  
- `User`  
- `CustomUser`  
- `Role`  

**Configurations**  
- `SecurityConfig` (CSRF, CORS, 권한/인증 설정)  
- `SwaggerConfig`  

**Views**  
- `/WEB-INF/views/*.jsp`  

**Database Tables**  
- menu
  - `id (PK)`
  - `title`
  - `content`
  - `created_at`
  - `view_count`

- user
  - `id (PK)`
  - `username`
  - `password (BCrypt 해시)`
  - `role`

## 🔄 주요 흐름

### 1. 로그인 프로세스
1. 사용자가 `POST /login` 요청 전송  
2. Spring Security 필터 체인에서 인증 처리  
3. `UserDetailsServiceImpl` → `UserMapper` 통해 DB 사용자 조회  
4. BCrypt로 비밀번호 검증  
5. 인증 성공 시 사용자 정보/권한을 세션에 저장  
6. 메인 페이지로 리다이렉트  

### 2. 회원가입
1. `POST /register` 요청  
2. 입력한 비밀번호를 BCrypt로 해시  
3. `UserService` → `UserMapper`를 통해 DB insert  

### 3. 게시글 작성 (REST)
1. 클라이언트가 `POST /menu/add` JSON 요청 전송  
2. `MenuRestController` → `MenuRestService` → `MenuRestMapper` 순으로 DB insert  
3. 작성일 자동 설정  
4. 조회수 초기 초기화 로직 처리


## 아키텍처 다이어그램 (Mermaid)

### 컴포넌트 다이어그램
(프로젝트 구조: Controller → Service → Mapper → DB, JSP 렌더링 흐름 기반)

```mermaid
flowchart TD
  Browser["User<br/>(Browser)"]

  subgraph Server ["Spring Boot Application"]
    direction TB
    Controllers["Controllers<br/>(PageController, MenuRestController, UserController)"]
    Services["Service Layer<br/>(MenuRestService, UserService, UserDetailsServiceImpl)"]
    Mappers["MyBatis Mappers<br/>(MenuRestMapper, UserMapper)"]
    Entities["Entities / DTOs<br/>(Menu, User, CustomUser)"]
    Security["Security Layer<br/>(SecurityConfig, BCrypt, CSRF, CORS)"]
    Views["JSP Views<br/>(WEB-INF/views/*.jsp)"]
    Static["Static Resources<br/>(/resources, /static)"]
    Swagger["API Docs (SpringDoc/Swagger)"]
  end

  DB["MySQL Database"]

  Browser -->|HTTP GET/POST / REST| Controllers
  Controllers --> Services
  Services --> Mappers
  Mappers -->|SQL| DB
  Controllers --> Views
  Browser -->|static files| Static
  Browser -->|OpenAPI UI| Swagger
  Controllers --> Security
  Security --> Services
  Security -->|User lookup| Services

```

### 시퀀스 다이어그램: 로그인
- User → `/login` 요청  
- Spring Security 인증  
- UserDetailsServiceImpl → UserMapper → DB 조회  
- 인증 성공 후 세션 저장 및 리다이렉트  

```mermaid
sequenceDiagram
  participant B as Browser
  participant C as Controller (formLogin)
  participant S as SecurityFilterChain
  participant UDS as UserDetailsServiceImpl
  participant DB as MySQL
  participant APP as Application (session)

  B->>C: POST /login (username, password)
  C->>S: formLogin processing
  S->>UDS: loadUserByUsername(username)
  UDS->>DB: SELECT user by username
  DB-->>UDS: user row
  UDS-->>S: UserDetails (CustomUser)
  S->>S: password match (BCrypt)
  alt if ok
    S->>APP: create session, set attributes (username, roles)
    S-->>B: 302 Redirect to "/"
  else invalid
    S-->>B: 302 Redirect to "/loginPage?error=true"
  end
```

### 시퀀스 다이어그램: 게시글 작성 (REST)
- User → `/menu/add` 요청  
- Controller → Service → Mapper → DB insert  
- Response 반환 및 클라이언트 UI 갱신  

```mermaid
sequenceDiagram
  participant B as Browser
  participant MRC as MenuRestController
  participant MS as MenuRestService
  participant MM as MenuRestMapper
  participant DB as MySQL

  B->>MRC: POST /menu/add (JSON body)
  MRC->>MS: boardInser(menu)
  MS->>MM: boardInsert(menu)  // MyBatis mapper -> SQL
  MM->>DB: INSERT INTO menu(...)
  DB-->>MM: result
  MM-->>MS: ok
  MS-->>MRC: ok
  MRC-->>B: 200 OK (message)
```


## 주요 파일 / 확인 포인트
- `build.gradle` : 의존성 및 플러그인
- `SpringProject1Application.java` : 애플리케이션 진입점
- `config/` : 보안 설정 및 기타 환경 설정
- `controller/` : 화면/API 엔드포인트
- `mapper/` : MyBatis 매퍼
- `webapp/WEB-INF/views/` : JSP 템플릿

## Swagger / API 문서
<img width="1393" height="667" alt="Image" src="https://github.com/user-attachments/assets/cbf7da96-1b6d-4353-a098-9b1896a7323e" />

## 프로젝트 성과
- Spring Boot 기반 프로젝트 전체 흐름 이해  
- 사용자 인증 및 권한 구조(Spring Security) 학습  
- 서버사이드 렌더링(JSP) 기반 UI 개발 경험  
- Swagger(OpenAPI) 적용을 통해 API 문서화 구성  
- RESTful 구조와 MyBatis 매퍼 기반 CRUD 구현 학습
