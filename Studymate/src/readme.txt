[구현한 주요 기능]

1. 과목 목록 전체 조회
 [GET] /api/myclass
 → 내 강의실 초기 화면 과목 목록 출력

2. 과목 참여 (Join)
 [PUT] /api/myclass/join/{classId}?student={name}
 → 참여 인원 증가 + 스터디 메이트 등록

3. 스터디 메이트 목록 조회
 [GET] /api/myclass/mates/{classId}
 → 해당 과목에 참여한 학생 목록 조회

4. 초기 과목 데이터 로딩
 → "데이터베이스", "운영체제" 등 기본 과목 4개 생성
