# TagCafe Backend

**카공에 진심인 당신을 위한 카페 플랫폼, TagCafe**

와이파이, 책상 크기, 콘센트 등 카공에 중요한 요소들을 태그 기반으로 필터링하고,  
리뷰와 제보를 통해 사용자에게 딱 맞는 카페를 찾아주는 서비스입니다.

---

## 🌐 배포 주소

- 웹 서비스: [https://tagcafe.site](https://tagcafe.site)
- Swagger API 문서: [https://tagcafe.site/swagger-ui/index.html](https://tagcafe.site/swagger-ui/index.html)

---

## 🧑‍💻 역할

| 이름 | 역할                                                             |
|:----:|----------------------------------------------------------------|
| <div align="center"><a href="https://github.com/ghi512"><img src="https://avatars.githubusercontent.com/ghi512" width="100"/><br/>김민지</a></div> | 카페 검색 및 조회, 태그 필터링, 카페 저장 기능 개발<br/>Swagger를 활용한 전체 API 문서화    |
| <div align="center"><a href="https://github.com/jjinleee"><img src="https://avatars.githubusercontent.com/jjinleee" width="100"/><br/>이진</a></div> | 회원 관리, 마이페이지(리뷰/제보) 기능 구현<br/>카카오 로그인 연동<br/>CI/CD 설정 및 배포 자동화 |

---

## 🛠 기술 스택

| 항목 | 내용 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.4.2 |
| ORM | Spring Data JPA |
| 빌드 도구 | Gradle |
| DB | MySQL 8.0 (AWS RDS) |
| 인증 | Spring Security + Kakao OAuth + JWT |
| 배포 | Docker |
| API 문서화 | Swagger (springdoc-openapi) |
| 환경 변수 | dotenv-java (.env) |

---

## 📁 프로젝트 구조

```bash
📦 TagCafe
┣ 📂 config                   # 전역 설정 (보안, Swagger, 카카오 OAuth 등) 
┣ 📂 controller               # API 컨트롤러
┣ 📂 dto                      # 요청/응답 DTO
┣ 📂 entity                   # JPA 엔티티
┣ 📂 repository               # DB 접근 (JPA 레포지토리)
┣ 📂 service                  # 비즈니스 로직 처리
┣ 📂 util                     # 공통 유틸 기능 (JWT)
┗ TagCafeApplication.java     # 메인 클래스
```
