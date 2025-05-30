

document.addEventListener('DOMContentLoaded', function() {
    // 모달 관련 요소들을 조건부로 초기화
    const createBtn = document.querySelector('a[href="/study-mate/create-study"]');
    if (createBtn) {  // 스터디 생성 버튼이 있는 페이지일 때만 실행
        const modal = document.getElementById('createStudyModal');
        const closeBtn = document.querySelector('.close');
        const subjectSelect = document.getElementById('subjectSelect');
        const form = document.getElementById('createStudyForm');

        // 과목 리스트 로드
        fetch('/api/study-mate/subjects/list')
            .then(response => response.json())
            .then(subjects => {
                subjects.forEach(subject => {
                    const option = document.createElement('option');
                    option.value = subject.id;
                    option.textContent = subject.subject_name;
                    subjectSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error loading subjects:', error);
                const errorOption = document.createElement('option');
                errorOption.textContent = '과목 목록을 불러오는데 실패했습니다';
                subjectSelect.appendChild(errorOption);
            });

        // 폼 제출 처리
        form.addEventListener('submit', async function(e) {
            e.preventDefault();

            const userIdElement = document.querySelector('input[name="userId"]');
            if (!userIdElement) {
                alert('로그인이 필요한 기능입니다.');
                return;
            }

            const selectedOption = subjectSelect.options[subjectSelect.selectedIndex];
            const groupName = document.getElementById('groupName').value.trim();
            const maxMembers = parseInt(document.getElementById('maxMembers').value);

            if (!groupName) {
                alert('스터디 그룹 이름을 입력해주세요.');
                return;
            }

            if (!subjectSelect.value) {
                alert('과목을 선택해주세요.');
                return;
            }

            const requestData = {
                userid: parseInt(userIdElement.value),
                subject_id: parseInt(subjectSelect.value),
                group_name: groupName,
                subject_name: selectedOption.textContent,
                max_members: maxMembers
            };

            try {
                const response = await fetch(`/api/study-mate/studygroups/create?userId=${userIdElement.value}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(requestData)
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || '스터디 생성에 실패했습니다.');
                }

                const result = await response.json();
                alert('스터디가 성공적으로 생성되었습니다.');
                window.location.href = `/study-mate/studyroom/${result.id}`;
            } catch (error) {
                console.error('Error:', error);
                alert(error.message);
            }
        });

        // 모달 제어
        createBtn.addEventListener('click', function(e) {
            e.preventDefault();
            modal.style.display = 'block';
        });

        closeBtn.addEventListener('click', function() {
            modal.style.display = 'none';
        });

        window.addEventListener('click', function(e) {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });
    }

    // 스터디 수정 폼 관련 코드 추가
    const updateForm = document.getElementById('updateStudyForm');
    if (updateForm) {  // 수정 폼이 있는 페이지일 때만 실행
        updateForm.addEventListener('submit', function(e) {
            e.preventDefault();
            updateStudyGroup();
        });
    }

});

function confirmDelete() {
    if (confirm('정말로 스터디 그룹을 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {
        const userIdElement = document.querySelector('input[name="userId"]');
        const groupId = window.location.pathname.split('/').pop();

        if (!userIdElement || !groupId) {
            alert('필요한 정보를 찾을 수 없습니다.');
            return;
        }

        fetch(`/api/study-mate/studygroups/delete/${groupId}?userId=${userIdElement.value}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(async response => {
                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({}));
                    throw new Error(errorData.message || '스터디 그룹 삭제에 실패했습니다.');
                }
                alert('스터디 그룹이 성공적으로 삭제되었습니다.');
                window.location.href = '/study-mate/myclass';
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);
            });
    }
}

function closeModal() {
    const modal = document.getElementById('createStudyModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// study-group.js에 추가
function updateStudyGroup() {
    const userIdElement = document.querySelector('input[name="userId"]');
    const groupId = window.location.pathname.split('/').pop();
    const groupNameInput = document.getElementById('groupName');
    const maxMembersSelect = document.getElementById('maxMembers');

    if (!userIdElement || !groupId || !groupNameInput || !maxMembersSelect) {
        alert('필요한 정보를 찾을 수 없습니다.');
        return;
    }

    const requestData = {
        group_name: groupNameInput.value.trim(),
        max_members: parseInt(maxMembersSelect.value)
    };

    fetch(`/api/study-mate/studygroups/${groupId}/update?userId=${userIdElement.value}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(async response => {
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || '스터디 그룹 수정에 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            alert('스터디 그룹이 성공적으로 수정되었습니다.');
            window.location.reload(); // 페이지 새로고침
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
}

// 폼 제출 이벤트 리스너 추가
