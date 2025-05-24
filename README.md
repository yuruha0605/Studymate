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
- StudymateApplication : 메인함수.


### 5/25 수정사항
- search merged with "Subject file" : search file에 readme file 남겨둠
- subject controller 에서 검색 처리 하도록 변경
- 

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





