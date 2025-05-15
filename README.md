# 📘 Spring 게시판 프로젝트

Spring Boot 기반의 간단한 게시판 CRUD 애플리케이션입니다.  
Jenkins를 이용해 CI/CD 파이프라인을 구축하여 GitHub 커밋 → 빌드 → Docker 이미지 빌드 및 푸시 → EC2 서버 배포까지 자동화되어 있습니다.

---

## ✨ 주요 기능

- 게시글 목록 확인, 검색 (페이징 지원)
- 게시글 작성, 수정, 삭제
- 게시글 상세 보기
- 게시글 작성 시 유효성 검증
- 회원가입, 로그인, 마이페이지
- 사용자별 작성 게시글 확인
- 사용자 정보 확인 및 수정 기능 (마이페이지)

---

## 🛠️ 기술 스택

### Backend
- Java 17  
- Spring Boot 3.x  
- Spring Data JPA
- Spring Security  
- MariaDB  

### DevOps
- Jenkins  
- Docker, Docker Compose  
- Nginx (Reverse Proxy)  
- AWS EC2 (Ubuntu)  
- GitHub Webhook  

---

## 📦 프로젝트 구조

```
📁 spring-board
 ┣ 📁 src
 ┣ 📄 build.gradle
 ┣ 📄 Dockerfile
 ┣ 📄 Jenkinsfile
 ┗ 📄 README.md
```

---

## 🚀 CI/CD 파이프라인 구성


### 전체 흐름

```plaintext
[GitHub]
  └─ Push (main branch)
      ↓ Webhook
[Jenkins (Local Controller)]
  └─ Gradle Build & Test
      ↓
  └─ Docker Image Build
      ↓
  └─ DockerHub Push
      ↓
[EC2 (Jenkins Agent)]
  └─ SSH로 접속
  └─ .env 파일 동적 생성
  └─ docker-compose pull
  └─ docker-compose down && up -d
      ↓
[Nginx]
  └─ Reverse Proxy to Spring Container
```

### 상세 단계

1. **소스 코드 Push**  
   GitHub에 push하면 webhook이 Jenkins 트리거

2. **Jenkins Pipeline 실행**
   - Docker Container 환경의 Jenkins Controller
   - `Jenkinsfile` 기반으로 실행  
   - `./gradlew clean build`로 테스트 및 빌드 수행  
   - `Dockerfile`로 이미지 빌드  
   - DockerHub에 푸시  

3. **EC2 원격 배포**
   - Docker Container 환경의 Jenkins Agent
   - Jenkins에서 EC2에 SSH 접속  
   - `.env` 파일 동적 생성 (DB 정보 포함)  
   - `docker-compose`로 기존 컨테이너 종료 및 재기동  

---

## 🌐 배포 환경

- EC2 Ubuntu 22.04  
- 포트 구성:  
  - `80`: nginx  
  - `8080`: spring app (내부)  
- nginx를 통해 외부 요청을 spring 컨테이너로 프록시 처리  

---

## 📌 기타

- DB는 외부 DB(MariaDB)를 사용하며 `.env` 파일을 통해 환경변수로 주입

---
