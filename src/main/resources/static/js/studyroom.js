document.addEventListener('DOMContentLoaded', function() {
    initializeModalHandlers();
    initializeStreamHandlers();
    initializePostHandlers();
    loadPosts();
    loadBoardInfo();
});

// 모달 관련 초기화
function initializeModalHandlers() {
    // 모달 열기 함수들
    window.openQuestionModal = () => {
        if (!document.getElementById('boardId').value) {
            loadBoardInfo();
        }
        toggleModal('questionModal', true);
    };
    window.openUploadModal = () => toggleModal('uploadModal', true);
    window.openScheduleModal = () => toggleModal('scheduleModal', true);
    window.openSettingsModal = () => toggleModal('settingsModal', true);

    // 모달 닫기 함수들
    window.closeQuestionModal = () => toggleModal('questionModal', false);
    window.closeUploadModal = () => toggleModal('uploadModal', false);
    window.closeScheduleModal = () => toggleModal('scheduleModal', false);
    window.closeSettingsModal = () => toggleModal('settingsModal', false);

    // 모달 외부 클릭시 닫기
    window.onclick = (event) => {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    };

    // 폼 제출 이벤트 리스너 등록
    const questionForm = document.getElementById('questionForm');
    if (questionForm) {
        questionForm.addEventListener('submit', handleQuestionSubmit);
    }
}

// 게시글 관련 초기화
function initializePostHandlers() {
    // 질문하기 버튼 이벤트 리스너
    const questionBtn = document.querySelector('.content-box .action-btn');
    if (questionBtn) {
        questionBtn.addEventListener('click', openQuestionModal);
    }
}

// 스트리밍 관련 초기화
function initializeStreamHandlers() {
    const streamButton = document.querySelector('.stream-button');
    if (streamButton) {
        streamButton.addEventListener('click', startStream);
    }
}

// 모달 토글 함수
function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = show ? 'block' : 'none';
    }
}

// 게시판 정보 로드
async function loadBoardInfo() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/boards/studygroup/${studygroupId}`);
        if (!response.ok) {
            throw new Error('Failed to load board info');
        }
        const boardData = await response.json();

        // hidden input이 없으면 생성
        let boardIdInput = document.getElementById('boardId');
        if (!boardIdInput) {
            boardIdInput = document.createElement('input');
            boardIdInput.type = 'hidden';
            boardIdInput.id = 'boardId';
            document.getElementById('questionForm').appendChild(boardIdInput);
        }
        boardIdInput.value = boardData.id;
    } catch (error) {
        console.error('Error loading board info:', error);
    }
}

async function handleQuestionSubmit(event) {
    event.preventDefault();

    const titleInput = document.getElementById('title');
    const contentInput = document.getElementById('content');
    const boardIdInput = document.getElementById('boardId');
    const studygroupIdInput = document.getElementById('studygroupId');

    if (!titleInput.value.trim()) {
        alert('제목을 입력해주세요.');
        return;
    }

    if (!contentInput.value.trim()) {
        alert('내용을 입력해주세요.');
        return;
    }

    if (!boardIdInput.value) {
        alert('게시판 정보를 찾을 수 없습니다.');
        return;
    }

    const postData = {
        title: titleInput.value.trim(),
        content: contentInput.value.trim(),
        board_id: parseInt(boardIdInput.value),
        studygroup_id: parseInt(studygroupIdInput.value)
    };

    try {
        const response = await fetch('/api/study-mate/posts/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Network response was not ok');
        }

        alert('질문이 등록되었습니다.');
        closeQuestionModal();
        loadPosts();  // 게시글 목록 새로고침
        event.target.reset();
    } catch (error) {
        console.error('Error:', error);
        alert('질문 등록에 실패했습니다: ' + error.message);
    }
}

async function loadPosts() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];
    console.log('Loading posts for studygroup:', studygroupId);

    try {
        // 먼저 해당 스터디그룹의 게시판 정보를 가져옵니다
        const boardResponse = await fetch(`/api/study-mate/boards/studygroup/${studygroupId}`);
        console.log('Board response status:', boardResponse.status);
        if (!boardResponse.ok) {
            throw new Error('게시판을 찾을 수 없습니다.');
        }
        const boardData = await boardResponse.json();
        console.log('Board data:', boardData);

        // 게시판 ID를 hidden input에 저장
        const boardIdInput = document.getElementById('boardId');
        if (boardIdInput) {
            boardIdInput.value = boardData.id;
        }

        // 해당 게시판의 게시글들을 가져옵니다
        const postsResponse = await fetch(`/api/study-mate/posts/${boardData.id}/list?page=0&size=10`);
        console.log('Posts response status:', postsResponse.status);
        if (!postsResponse.ok) {
            throw new Error('게시글을 불러오는데 실패했습니다.');
        }

        const postsData = await postsResponse.json();
        console.log('Posts data:', postsData);

        if (postsData && postsData.body && Array.isArray(postsData.body)) {
            updatePostList(postsData.body);
        } else {
            console.log('No posts found or invalid data format');
            const postListContainer = document.querySelector('.content-box .post-list');
            if (postListContainer) {
                showNoContentMessage(postListContainer);
            }
        }
    } catch (error) {
        console.error('Error loading posts:', error);
        const postListContainer = document.querySelector('.content-box .post-list');
        if (postListContainer) {
            showErrorMessage(postListContainer);
        }
    }
}

// 날짜 포맷팅 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
}

// 스트리밍 시작
async function startStream() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({
            video: true,
            audio: false
        });

        const videoElement = document.getElementById('localVideo');
        videoElement.srcObject = stream;
        videoElement.style.display = 'block';
    } catch (error) {
        console.error('스트림 시작 실패:', error);
        alert('카메라 접근에 실패했습니다.');
    }
}

// 스트리밍 종료
function stopStream() {
    const videoElement = document.getElementById('localVideo');
    const stream = videoElement.srcObject;
    if (!stream) return;

    stream.getTracks().forEach(track => track.stop());
    videoElement.srcObject = null;
    videoElement.style.display = 'none';
}

function updatePostList(posts) {
    const postListContainer = document.querySelector('.content-box .post-list');
    if (!postListContainer) {
        console.error('Post list container not found');
        return;
    }

    if (!posts || posts.length === 0) {
        showNoContentMessage(postListContainer);
        return;
    }

    const postItems = posts.map(post => `
        <div class="post-item">
            <h3>${escapeHtml(post.title)}</h3>
            <p class="post-info">
                <span>${formatDate(post.written || new Date())}</span>
            </p>
            <p class="post-content">${escapeHtml(post.content)}</p>
        </div>
    `).join('');

    postListContainer.innerHTML = postItems;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showNoContentMessage(container) {
    if (container) {
        container.innerHTML = '<div class="no-content"><p>등록된 게시글이 없습니다.</p></div>';
    }
}

function showErrorMessage(container) {
    if (container) {
        container.innerHTML = '<div class="error-message">게시글을 불러오는데 실패했습니다.</div>';
    }
}

async function handleScheduleSubmit(event) {
    event.preventDefault();

    const modal = document.getElementById('scheduleModal');
    const isEdit = modal.dataset.mode === 'edit';
    const scheduleId = modal.dataset.scheduleId;

    const titleInput = document.getElementById('scheduleTitle');
    const dateInput = document.getElementById('scheduleDate');
    const timeInput = document.getElementById('scheduleTime');
    const descriptionInput = document.getElementById('scheduleDescription');
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    if (!titleInput.value.trim() || !dateInput.value || !timeInput.value) {
        alert('필수 항목을 모두 입력해주세요.');
        return;
    }

    const scheduleDateTime = `${dateInput.value}T${timeInput.value}:00`;

    const scheduleData = {
        title: titleInput.value.trim(),
        description: descriptionInput.value.trim(),
        schedule_date_time: scheduleDateTime,
        studygroup_id: parseInt(studygroupId)
    };

    try {
        const url = isEdit
            ? `/api/study-mate/schedules/${scheduleId}`
            : '/api/study-mate/schedules/create';

        const response = await fetch(url, {
            method: isEdit ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(scheduleData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Network response was not ok');
        }

        alert(isEdit ? '일정이 수정되었습니다.' : '일정이 등록되었습니다.');
        closeScheduleModal();
        loadSchedules();
        event.target.reset();
    } catch (error) {
        console.error('Error:', error);
        alert((isEdit ? '일정 수정' : '일정 등록') + '에 실패했습니다: ' + error.message);
    }
}


async function loadSchedules() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/schedules/studygroup/${studygroupId}`);
        if (!response.ok) {
            throw new Error('일정을 불러오는데 실패했습니다.');
        }

        const schedules = await response.json();
        updateScheduleList(schedules);
    } catch (error) {
        console.error('Error:', error);
        showScheduleError();
    }
}

// 일정 목록 표시 함수 수정
function updateScheduleList(schedules) {
    const scheduleContainer = document.querySelector('.content-box:nth-child(2) .schedule-list');
    if (!scheduleContainer) {
        console.error('Schedule container not found');
        return;
    }

    if (!schedules || schedules.length === 0) {
        scheduleContainer.innerHTML = '<div class="no-content"><p>등록된 일정이 없습니다.</p></div>';
        return;
    }

    const scheduleItems = schedules.map(schedule => {
        console.log('Raw schedule:', schedule); // 디버깅용
        const formattedDate = formatDateTime(schedule.scheduleDateTime);

        return `
            <div class="schedule-item" data-id="${schedule.id}">
                <h3>${escapeHtml(schedule.title)}</h3>
                <p class="schedule-info">
                    <span>${formattedDate}</span>
                </p>
                ${schedule.description ? `<p class="schedule-description">${escapeHtml(schedule.description)}</p>` : ''}
                <div class="schedule-actions">
                    <button onclick="openEditScheduleModal(${JSON.stringify(schedule)})" class="btn btn-sm btn-primary">수정</button>
                    <button onclick="deleteSchedule(${schedule.id})" class="btn btn-sm btn-danger">삭제</button>
                </div>
            </div>
        `;
    }).join('');

    scheduleContainer.innerHTML = scheduleItems;
}


// 날짜와 시간 포맷팅 함수
function formatDateTime(dateTimeStr) {
    try {
        if (!dateTimeStr) {
            console.error('Date string is empty or null');
            return 'Invalid date';
        }

        // 날짜 문자열에서 마이크로초 부분 제거
        const cleanDateStr = dateTimeStr.split('.')[0];
        const date = new Date(cleanDateStr.replace(' ', 'T'));

        if (isNaN(date.getTime())) {
            console.error('Invalid date:', dateTimeStr);
            return 'Invalid date';
        }

        return `${date.getFullYear()}-${
            String(date.getMonth() + 1).padStart(2, '0')}-${
            String(date.getDate()).padStart(2, '0')} ${
            String(date.getHours()).padStart(2, '0')}:${
            String(date.getMinutes()).padStart(2, '0')}`;
    } catch (error) {
        console.error('Error formatting date:', error, 'for date string:', dateTimeStr);
        return 'Error formatting date';
    }
}

// 페이지 로드 시 일정 목록도 함께 로드
document.addEventListener('DOMContentLoaded', function() {
    // ... 기존 코드 ...
    loadSchedules();
});

// 일정 모달 폼 제출 이벤트 리스너
const scheduleForm = document.getElementById('scheduleForm');
if (scheduleForm) {
    scheduleForm.addEventListener('submit', handleScheduleSubmit);
}

function openEditScheduleModal(schedule) {
    const modal = document.getElementById('scheduleModal');
    document.getElementById('scheduleTitle').value = schedule.title;
    document.getElementById('scheduleDescription').value = schedule.description || '';

    // 날짜와 시간 설정
    const dateTime = new Date(schedule.scheduleDateTime);
    document.getElementById('scheduleDate').value = dateTime.toISOString().split('T')[0];
    document.getElementById('scheduleTime').value = dateTime.toTimeString().slice(0,5);

    // 수정 모드임을 표시
    modal.dataset.mode = 'edit';
    modal.dataset.scheduleId = schedule.id;

    // 모달 제목 변경
    document.querySelector('#scheduleModal .modal-title').textContent = '일정 수정';

    modal.style.display = 'block';
}

// 일정 삭제
async function deleteSchedule(id) {
    if (!confirm('정말로 이 일정을 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/schedules/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('삭제 실패');
        }

        alert('일정이 삭제되었습니다.');
        loadSchedules();  // 일정 목록 새로고침
    } catch (error) {
        console.error('Error:', error);
        alert('일정 삭제에 실패했습니다: ' + error.message);
    }
}
