function searchKeyword() {
    const keyword = document.querySelector('.search-input').value;
    const type = document.getElementById('searchType').value;

    if (!keyword.trim()) {
        alert('검색어를 입력해주세요.');
        return;
    }

    // URL을 StudyGroup의 search 엔드포인트로 변경
    const searchUrl = `/api/study-mate/studygroups/search?keyword=${encodeURIComponent(keyword)}&type=${type}`;
    console.log('Requesting URL:', searchUrl);

    fetch(searchUrl)
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            console.log('Search results:', data);
            const results = document.getElementById('results');
            results.innerHTML = '';

            if (data.length === 0) {
                results.innerHTML = '<p>검색 결과가 없습니다.</p>';
                return;
            }

            // 검색 결과를 화면에 표시
            data.forEach(item => {
                const resultDiv = document.createElement('div');
                resultDiv.className = 'result-line';
                resultDiv.innerHTML = `
                            <h3>${item.group_name}</h3>
                            <p>과목: ${item.subject_name}</p>
                            <p>인원: ${item.current_members}/${item.max_members}</p>
                            <p>상태: ${item.status}</p>
                            <button onclick="joinStudyGroup(${item.id})"
                                    ${item.status !== 'RECRUITING' ? 'disabled' : ''}>
                                참여하기
                            </button>
                        `;
                results.appendChild(resultDiv);
            });
        })
        .catch(err => {
            console.error('검색 실패:', err);
            alert('검색 중 오류가 발생했습니다.');
        });
}


function joinStudyGroup(groupId) {
    // 스터디 그룹 참여 로직 구현
    fetch(`[[@{/api/study-mate/}]]${groupId}/join`, {
        method: 'POST'
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert('스터디 그룹에 참여되었습니다!');
                searchKeyword(); // 목록 새로고침
            } else {
                alert(data.message || '참여에 실패했습니다.');
            }
        })
        .catch(err => {
            console.error('참여 실패:', err);
            alert('참여 처리 중 오류가 발생했습니다.');
        });
}