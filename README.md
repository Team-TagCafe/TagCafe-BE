# ☕️ TagCafe

**카공해야 되는데.. 어디 카페 가지?**

> 와이파이는 되나? 책상은 넓나? 콘센트는 있나? ...  
> 생각할 게 너무 많아 🤯  

혹시 이런 고민 한 번이라도 해본 적 있다면,  
**당신을 위한 카공 맞춤형 카페 지도 플랫폼, TagCafe!**

---
## 📝 프로젝트 소개
![image](https://github.com/user-attachments/assets/fd6a2a54-eb7a-4d7a-98e0-4da37fa2d79c)

- TagCafe는 **와이파이, 책상 크기, 콘센트, 화장실, 주차 여부** 등  
  **카공에 꼭 필요한 요소**를 기반으로 필터링하여 카페를 찾을 수 있는 플랫폼입니다.
- **사용자 리뷰와 제보**를 통해 지속적으로 데이터가 업데이트됩니다.
- **카페 저장 / 방문 여부 체크** 기능으로  
  내가 좋아하는 카페를 기억하고 다시 찾기 편리합니다.

### 🌐 배포 주소

- 웹 서비스: [https://tagcafe.site](https://tagcafe.site)

<br>

### 🔍 주요 기능

#### 🤎 홈
- 키워드 검색 (지역명, 카페명)
- **태그 필터**로 조건 검색  
  `영업시간`, `와이파이`, `책상`, `콘센트`, `화장실`, `주차`, `평점` 등  👉 *다중 선택 가능*
- 지도에서 카페 선택 → 상세 페이지로 이동
- **현재 위치로 지도 이동** 기능 지원

#### 🤎 카페 상세페이지
- 기본 정보: 사진, 카페명, 주소, 운영시간
- 태그 정보: 와이파이, 책상, 콘센트, 화장실, 주차, 평점 등
- 지도 위치, 전화번호 표시
- 사용자 리뷰 목록 조회 및 작성 가능

#### 🤎 저장한 카페
- 내가 저장한 카페 리스트  
  → 카드 형식 UI + 주요 태그 아이콘 표시 (와이파이, 콘센트)
- 태그 기반 필터링 가능
- **방문 여부 체크 & 필터링** 가능

#### 🤎 마이페이지 - 리뷰
- 내가 작성한 리뷰 **조회 / 수정 / 삭제** 가능

#### 🤎 마이페이지 - 제보
- **추천하고 싶은 카공 카페 직접 제보** 가능
- 태그 선택 + 리뷰 작성
- 관리자의 승인 후 정식 등록
- 승인 전에는 **수정 가능**

#### 🤎 사이드메뉴
- 자주 묻는 질문 (FAQ)
- 의견 및 오류 제보
- 닉네임 수정
- 로그아웃 / 회원 탈퇴

<br>

### 👩🏻‍💻 팀원 소개

| 이름 | 역할 |
|:----:|----------------------------------------------------------------|
| <div align="center"><a href="https://github.com/ghi512"><img src="https://avatars.githubusercontent.com/ghi512" width="100"/><br/>김민지</a></div> | - 카페 검색 및 지도 기반 조회 기능 구현 (카카오맵 API, 구글맵 API 연동)<br/>- 홈, 상세 페이지, 저장한 카페 UI 구현<br/>- Swagger 활용 API 문서화 |
| <div align="center"><a href="https://github.com/jjinleee"><img src="https://avatars.githubusercontent.com/jjinleee" width="100"/><br/>이진</a></div> | - 회원가입, 로그인 기능 구현 (카카오 OAuth2 연동) <br/>- 마이페이지 리뷰/제보 기능 개발 (등록/조회/수정)<br/>- 회원관리, 마이페이지, 사이드메뉴 UI 구현<br/>- CI/CD 설정 및 배포 자동화 |


<br>

### 🛠 기술 스택

> #### UI
<!-- figma -->
<img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"> 

> #### Frontend
<!--react, javascript, css-->
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black" /> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" /> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white" />

> #### Backend
<!-- Java, springboot -->
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=React-61DAFB&logoColor=white" /> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />

> #### 데이터베이스
<!-- mysql, rds -->
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white" /> <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white" />

> #### 배포 
<!-- ec2,  -->
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white" /> <img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" />


> #### 개발 환경
<!-- vscode, intellij  -->
<img src="https://img.shields.io/badge/VSCode-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=fff" /> <img src="https://img.shields.io/badge/Nintellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white" /> 


> #### 협업
<!-- notion, github-->
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white" /> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white" /> 

<br>

## 💡 설계

### 🎨 Figma
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/f3a6c6df-e9ab-4097-a556-e0e085d70629" />
<br>

### 🗄️ ERD
<img width="1000" src="https://github.com/user-attachments/assets/995ff332-a88d-4e25-b26e-b64df6b6f602" />
<br>

### 🔍 API 명세서
- Swagger API 문서: [https://tagcafe.site/swagger-ui/index.html](https://tagcafe.site/swagger-ui/index.html)

<br>

### 🏗️ System Architecture

<img width="500" src="https://github.com/user-attachments/assets/a02ebb63-74b5-425d-805c-4ed516279c43" />
