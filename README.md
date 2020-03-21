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
<table>
  <tr>
    <td><b>이벤트 리스트[EventListActivity]</b></td><td>이벤트 리스트를 보여주는 페이지입니다.</td>
  </tr>
  <tr>
    <td><b>이벤트상세[EventDetailActivity]</b></td><td>신규회원 쿠폰 발급 및 이벤트 안내 페이지입니다.</td>
  </tr>
  <tr>
    <td><b>룰렛[RouletteActivity]</b></td><td>유저가 가지고 있는 횟수 만큼 룰렛을 돌려 쿠폰을 지급받습니다.</td>
  </tr>
</table>
<br>

> <h3>내 정보</h3>
![12](https://user-images.githubusercontent.com/60861383/77169851-893d7d80-6afd-11ea-8a81-e1268ab10fee.png)
내 정보 설명

> <h3>리그 정보</h3>
![9](https://user-images.githubusercontent.com/60861383/77169879-93f81280-6afd-11ea-9336-12ed6c91c35c.png)
리그 정보 설명

> <h3>티켓 예매</h3>
![3](https://user-images.githubusercontent.com/60861383/77169984-bd18a300-6afd-11ea-994c-23c4e413d58f.png)
<table>
  <tr>
    <td><b>티켓리스트[Activity]</b></td><td>오늘부터 남은 경기만큼 불러와 표시합니다.<br>표의 상태에는 [예매, 매진, 종료, 오픈 예정] 4가지가 존재합니다.<br><b>예약</b>경기일 5일전 2시 이후부터 게임시작 전이며 매진이 아닐 때<br><b>매진</b>모든 좌석이 예약이 완료된 상태<br><b>종료</b>게임 시작 된 이후<br><b>오픈 예정</b>경기일 5일 전 2시 이전</td>
  </tr>
  <tr>
    <td><b>예매 안내[Activity]</b></td><td>티켓 정보, 예매 안내, 유의사항 등 티켓 구매에 대한 정보를 안내하는 페이지입니다.</td>
  </tr>
  <tr>
    <td><b>예매[Activity]</b></td><td>인원을 선택하고 좌석을 선택하면 좌석에 대한 다이얼로그가 표시됩니다.<br>다이얼로그에서 원하는 좌석을 선택한 뒤 결제하기를 선택하면 티켓 페이지로 이동합니다.</td>
  </tr>
  <tr>
    <td><b>티켓[Activity]</b></td><td>구매한 티켓에 해당하는 QR코드 생성 및 환불을 하는 페이지입니다.</td>
  </tr>
</table>
<br>
이미지의 경우 예약으로 나와있지만 예매로 수정하였습니다.

> <h3>샵</h3>
![10](https://user-images.githubusercontent.com/60861383/77170022-cace2880-6afd-11ea-9791-e73095a1285b.png)
샵 설명

> <h3>롤알못</h3>
![6](https://user-images.githubusercontent.com/60861383/77170059-d588bd80-6afd-11ea-9b95-95d6db90adcc.png)
<table>
  <tr>
    <td><b>롤알못[LoLGuideActivity]</b></td><td>롤을 알지 못하는 사람들을 위해 롤에 대해 이해하기 좋은 글을 가져와서 보여줍니다.</td>
  </tr>
</table>
<br>ㅋ

> <h3>뉴스</h3>
![5](https://user-images.githubusercontent.com/60861383/77170287-28fb0b80-6afe-11ea-9890-2d101645b1e7.png)
뉴스 설명

> <h3>채팅</h3>
![11](https://user-images.githubusercontent.com/60861383/77170315-357f6400-6afe-11ea-9ace-d1d1bcc5a441.png)
<table>
  <tr>
    <td><b>채팅 리스트[ChattingListActivity]</b></td><td>날짜를 선택하여 해당 일의 경기를 표시합니다.<br>팀 아이콘을 선택하면 채팅 액티비티로 넘어가게 됩니다.</td>
  </tr>
  <tr>
    <td><b>채팅[ChattingActivity]</b></td><td>선택한 팀을 응원하는 채팅 페이지입니다.<br>이전 날짜의 채팅은 입장은 가능하나 채팅 입력은 불가능 합니다.<br>게임 시작 전 30분에 채팅방이 오픈 되며 그 이후 게임의 경우 채팅방 입장이 불가능합니다.</td>
  </tr>
</table>
<br>
