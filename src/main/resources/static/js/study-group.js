// study-group.js
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('createStudyModal');
    const createBtn = document.querySelector('a[href="/study-mate/create-study"]');
    const closeBtn = document.querySelector('.close');
    const subjectSelect = document.getElementById('subjectSelect');


    // 현재 로그인한 사용자의 ID를 가져오는 함수
    function getCurrentUserId() {
        // hidden input에서 userId 값을 가져옴
        const userIdInput = document.querySelector('input[name="userId"]');
        if (!userIdInput) {
            console.error('User ID input not found');
            return null;
        }
        return userIdInput.value;
    }


    // 과목 리스트 로드
    fetch('/api/study-mate/subjects/list')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
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
    const form = document.getElementById('createStudyForm');
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const userId = getCurrentUserId();
        if (!userId) {
            alert('로그인이 필요합니다.');
            return;
        }

        const formData = {
            subjectId: subjectSelect.value,
            groupName: document.getElementById('groupName').value,
            maxMembers: parseInt(document.getElementById('maxMembers').value),
            subjectName: subjectSelect.options[subjectSelect.selectedIndex].textContent
        };

        fetch(`/api/study-mate/studygroups/create?userId=${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const successModal = confirm('생성이 완료되었습니다.');
                if (successModal) {
                    // 스터디룸 페이지로 이동
                    window.location.href = `/study-mate/studyroom/${data.id}`;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('스터디 생성에 실패했습니다. 다시 시도해주세요.');
            });
    });


    // 새 스터디 만들기 버튼 클릭 시 모달 열기
    createBtn.addEventListener('click', function(e) {
        e.preventDefault();
        modal.style.display = 'block';
    });

    // X 버튼 클릭 시 모달 닫기
    closeBtn.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });


});


