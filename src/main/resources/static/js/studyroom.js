document.addEventListener('DOMContentLoaded', function() {
    initializeModalHandlers();
    initializeStreamHandlers();
    initializePostHandlers();
    loadPosts();
});

// 모달 관련 초기화
function initializeModalHandlers() {
    // 모달 열기 함수들
    window.openQuestionModal = () => toggleModal('questionModal', true);
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

// 질문 게시글 제출 처리
async function handleQuestionSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const studygroupId = formData.get('studygroupId');

    const postData = {
        title: formData.get('title'),
        content: formData.get('content'),
        boardId: 1, // 스터디룸 게시판 ID
        studygroupId: studygroupId // studygroupId 추가
    };

    try {
        const response = await fetch(`/api/study-mate/posts/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        await response.json();
        alert('질문이 등록되었습니다.');
        closeQuestionModal();
        loadPosts();
        event.target.reset(); // 폼 초기화
    } catch (error) {
        console.error('Error:', error);
        alert('질문 등록에 실패했습니다.');
    }
}

// 게시글 목록 로드
async function loadPosts() {
    const urlParams = new URLSearchParams(window.location.search);
    const studygroupId = urlParams.get('id');

    if (!studygroupId) {
        console.error('스터디 그룹 ID를 찾을 수 없습니다.');
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/posts/${studygroupId}/list?page=0&size=10`);
        if (!response.ok) {
            throw new Error('Failed to fetch posts');
        }

        const data = await response.json();
        const postList = document.querySelector('.post-list');

        if (!postList) return;

        if (!data.body || data.body.length === 0) {
            postList.innerHTML = '<div class="no-content"><p>등록된 게시글이 없습니다.</p></div>';
            return;
        }

        postList.innerHTML = data.body.map(post => `
            <div class="post-item">
                <h3>${post.title}</h3>
                <p class="post-info">
                    <span>${formatDate(post.written)}</span>
                </p>
                <p class="post-content">${post.content}</p>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading posts:', error);
        const postList = document.querySelector('.post-list');
        if (postList) {
            postList.innerHTML = '<div class="no-content"><p>게시글을 불러오는데 실패했습니다.</p></div>';
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