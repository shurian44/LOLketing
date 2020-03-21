# LOLketing
롤캣팅 프로젝트 **[제작자 : 김민재, 정재규, 박송이, 조정웅]**

# 제작 기간 및 버전관리
**1. 제작 기간[초기버전 기준]** : 20.02.03 ~ 20.02.26 (24일)<br>
**2. 버전관리**<br>
(1) 1.x.x : 1-2주차 진행 [1주차 : 주제선정, Firebase 공부 / 2주차 : 각 파트 코딩]<br>
  (2) 2.x.x : 3-4주차 진행 [3주차 : 각 파트 코딩 / 4주차 : 마무리 및 오류 수정]<br>
  (3) 3.0.0 : 코드 주석 추가 (진행 중)<br>

이후 3.x.x는 추후 업데이트 추가 / 이외 버전 : 정규 버전 합치기 이전 각자의 버전

# 구조
구조 이미지가 들어갈 영역
# 화면 및 기능 설명
> <h3>로그인 및 회원가입</h3>
![1](https://user-images.githubusercontent.com/60861383/77169193-737b8880-6afc-11ea-9018-30d023473f2b.png)<br>
<table>
  <tr>
    <td><b>스플래쉬[SplashActivity]</b></td><td>앱 실행 시 최초로 보여지는 화면</td>
  </tr>
  <tr>
    <td><b>로그인[LoginActivity]</b></td><td>로그인 방식은 <b>Firebase</b>를 활용한 e-mail과 Google 로그인 두가지 방식을 지원합니다.</td>
  </tr>
  <tr>
    <td><b>회원가입[JoinActivity]</b></td><td>회원가입 시에는 이메일과 비밀번호 정보만 등록합니다.</td>
  </tr>
  <tr>
    <td><b>회원가입 상세[JoinDetailActivity]</b></td><td>닉네임, 전화번호, 주소의 정보를 추가로 등록합니다.</td>
  </tr>
</table>
<br>
 
> <h3>메인화면 및 관리자 페이지</h3>
![2](https://user-images.githubusercontent.com/60861383/77169584-10d6bc80-6afd-11ea-851b-5d5d92dc6d97.png)
<table>
  <tr>
    <td><b>메인화면[MainActivity]</b></td><td>기본적으로 각 페이지의 해당하는 9개의 버튼과 관리자 전용 관리자 페이지 버튼이 있습니다.</td>
  </tr>
  <tr>
    <td><b>관리자페이지[ManagerActivity]</b></td><td><b>팀 정보 수정</b> : 미리 등록한 JSON 파일을 파싱하여 Firebase에 데이터를 추가/수정합니다.<br>
    <b>경기 정보 수정</b> : 공식 홈페이지에 있는 경기 일정을 파싱하여 Firebase에 추가/수정합니다.<br>
    <b>상품 추가</b> : 추후 업데이트 예정입니다.<br>
    <b>샵 이벤트 추가</b> : Firebase에 있는 상품을 검색한 뒤, 베너 이미지를 추가하여 Firebase에 이벤트를 등록합니다.</td>
  </tr>
</table>
<br>

> <h3>게시판</h3>
![13](https://user-images.githubusercontent.com/60861383/77169696-4bd8f000-6afd-11ea-8dbd-124ba1cc1518.png)
게시판 설명

> <h3>이벤트</h3>
![7](https://user-images.githubusercontent.com/60861383/77169747-5e532980-6afd-11ea-8bf4-33a28aad8ba3.png)
이벤트 설명

> <h3>내 정보</h3>
![12](https://user-images.githubusercontent.com/60861383/77169851-893d7d80-6afd-11ea-8a81-e1268ab10fee.png)
내 정보 설명

> <h3>리그 정보</h3>
![9](https://user-images.githubusercontent.com/60861383/77169879-93f81280-6afd-11ea-9336-12ed6c91c35c.png)
리그 정보 설명

> <h3>티켓 예매</h3>
![3](https://user-images.githubusercontent.com/60861383/77169984-bd18a300-6afd-11ea-994c-23c4e413d58f.png)
티켓 예매 설명

> <h3>샵</h3>
![10](https://user-images.githubusercontent.com/60861383/77170022-cace2880-6afd-11ea-9791-e73095a1285b.png)
샵 설명

> <h3>롤알못</h3>
![6](https://user-images.githubusercontent.com/60861383/77170059-d588bd80-6afd-11ea-9b95-95d6db90adcc.png)
롤알못 설명

> <h3>뉴스</h3>
![5](https://user-images.githubusercontent.com/60861383/77170287-28fb0b80-6afe-11ea-9890-2d101645b1e7.png)
뉴스 설명

> <h3>채팅</h3>
![11](https://user-images.githubusercontent.com/60861383/77170315-357f6400-6afe-11ea-9ace-d1d1bcc5a441.png)
채팅 설명
