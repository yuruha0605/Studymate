# Front End ( 주 : 이채연 )
- src > main > resource > template 파일 안에다가 완료된 작업을 넣어주시면 됩니다.
- 반응형 웹을 만드는 것이 중요합니다. device는 휴대폰, 태블릿, 윈도우 정도로만 생각 중인데 어렵다고 느끼시면 말씀하세요

# Back End ( 주 : 한인찬 )
- src > main > java > edu.yonsei.Studymate 파일 내에서 작업
- Dto : 변수 저장용
- entity : table 불러와서 값 저장
- repository : service에서 사용할 함수 선언
- service : 실질적인 함수 정의 (SQL문 기준으로 작성했어야하는 거 잊지 말아주세요)
- CrudController : URL로 어떤 데이터가 들어오면 처리
- StudymateApplication : 메인함수
---

### 5/31 수정사항 (주간)
- 백엔드, 프론트 구현 완료
- 404, find_id, find_pass 구현 완료 
- 비번 찾기는 저희가 암호화해서 저장했기에 그냥 받아올 경우 Bcrypt된 비번을 돌려받습니다. 복호화는 불가능하다 하여 임시 비번 주고 사용자에게 비번 수정하라는 방향이 있지만 시간관계상 넘어갑시다

### 5/31 수정사항 (새벽)
- 백엔드 스터디메이트 상태 제외하고 구현 완료
- 자잘한 버그들 수정
- 금일 최대한 마무리해볼게요

### 5/30 수정사항
- Studyroom 핵심 기능 구현 완료
- front 부분 수정 중 / 기타 버그 수정
- 메인페이지 학습 추천 시스템 병합

### 5/29 수정사항
- schedule table 완성 <<< 지금 보니까 데이터베이스에 누가 만들어놨었네요 못봤습니다.
- 질문게시판 연동 완료
- 자잘한 버그 수정


### 5/28 수정사항
- header.html에서 Study-Mate 로고의 href 경로를 "/" → "/study-mate/main"으로 변경(로고 클릭하면 메인화면으로 링크)(수정자: 공지혁)
- Myclass.html의 "내가 만든 스터디" 부분에 t_studygroup 중 test용 스터디 그룹 나오도록 설정 (박명환)


### 5/27 수정사항
- 안한다고 한 security 구현
- 각 유저 별로 my classroom에 뜰 스터디 그룹 구현
- 본래는 내가 참여한 클래스룸만 보이게 했지만 crud 방식을 위해 create도 추가 구현
- 위 방식으로 스터디룸 생성 시 subject_entity table과 t_studygroup이 원활히 연동될 수 있도록 List 방식의 선택을 구현 
- 기타 버그 해결


### 5/26 수정사항
- 각 스터디룸에 대한 각 게시판 작성
- Studygroup repository에 대한 전편 개편
- Subject > Studygroup > board 까지는 얼추 구현, 남은건 post랑 reply 정도
- header.html의 "내 강의실", "유료 서비스" 메뉴 경로 실제 링크 수정(수정자: 공지혁)
- MyclassViewController.java 추가 → myclass.html 라우팅 처리(수정자: 공지혁)

### 5/25 수정사항
- search merged with "Subject file" : search file에 readme file 남겨둠
- subject controller 에서 검색 처리 하도록 변경


### 5/24 수정 사항
- signup page 수정 - 유저 정보 좀 더 불러옴 ( 추천 시스템용 )
- login, signup쪽 백엔드 살짝 수정
- login, signup, main, studyroom, myclass 프론트 일부 수정

### 5/23 수정 사항
- 스키마를 subject_entity > t_studygroup > t_board > t_bbs2025 > t_reply2025 에 맞춰 제작 중
- 현재 오류가 많아 git에 올려두었음

### 5/21 수정 사항
- Repository 정리
- 게시판용 board, bbs, reply 파일 추가
- a_LogTest.html의 Register를 a href -> a_SignupTest.html으로 연결하도록 수정
- Myclass의 StudentEntity가 비어있어, 간단하게 id, name만 설정





