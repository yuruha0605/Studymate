document.addEventListener('DOMContentLoaded', function() {
    // 스트리밍 관련 함수들
    async function startStream() {  // async 키워드 추가
        try {
            const stream = await navigator.mediaDevices.getUserMedia({
                video: true,
                audio: false
            });

            const videoElement = document.getElementById('localVideo');
            videoElement.srcObject = stream;
            videoElement.style.display = 'block';

            const button = document.querySelector('.stream-button');
            button.textContent = '스트림 종료';
            button.onclick = stopStream;

        } catch (error) {
            console.error('스트림 시작 실패:', error);
            alert('카메라 접근에 실패했습니다.');
        }
    }

    function stopStream() {
        const videoElement = document.getElementById('localVideo');
        const stream = videoElement.srcObject;
        if (!stream) return;  // stream이 없을 경우 처리 추가

        const tracks = stream.getTracks();
        tracks.forEach(track => track.stop());
        videoElement.srcObject = null;
        videoElement.style.display = 'none';

        const button = document.querySelector('.stream-button');
        button.textContent = '스트림 시작';
        button.onclick = startStream;
    }

    // 게시글 관련 함수들
    function loadPosts() {
        const studygroupId = document.querySelector('input[name="studygroupId"]')?.value;
        if (!studygroupId) {
            console.error('스터디 그룹 ID를 찾을 수 없습니다.');
            return;
        }

        fetch(`/api/study-mate/post/articles/${studygroupId}?page=0&size=10`)
            .then(response => response.json())
            .then(data => {
                const postList = document.querySelector('.post-list');
                if (postList) {
                    if (!data || !data.body) {
                        postList.innerHTML = '<p>등록된 게시글이 없습니다.</p>';
                        return;
                    }
                    const posts = data.body;
                    postList.innerHTML = posts.map(post => `
                        <div class="post-item">
                            <p>${post.title}</p>
                        </div>
                    `).join('');
                }
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                const postList = document.querySelector('.post-list');
                if (postList) {
                    postList.innerHTML = '<p>게시글을 불러오는데 실패했습니다.</p>';
                }
            });
    }

    function openQuestionModal() {
        document.getElementById('questionModal').style.display = 'block';
    }

    function closeQuestionModal() {
        document.getElementById('questionModal').style.display = 'none';
    }

    function submitQuestion() {
        const form = document.getElementById('questionForm');
        const formData = new FormData();

        const boardIdInput = document.querySelector('input[name="boardId"]');
        const titleInput = document.getElementById('title');
        const contentInput = document.getElementById('content');
        const studygroupId = document.querySelector('input[name="studygroupId"]')?.value;

        if (!boardIdInput || !titleInput || !contentInput || !studygroupId) {
            console.error('Required form fields not found');
            alert('필수 입력 항목이 누락되었습니다.');
            return;
        }

        formData.append('boardId', boardIdInput.value);
        formData.append('title', titleInput.value);
        formData.append('content', contentInput.value);
        formData.append('studygroupId', studygroupId);

        fetch('/api/study-mate/post/create', {  // API 경로 수정
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                alert('질문이 등록되었습니다.');
                closeQuestionModal();
                loadPosts();  // 게시글 목록 새로고침
            })
            .catch(error => {
                console.error('Error:', error);
                alert('질문 등록에 실패했습니다.');
            });
    }

    // 이벤트 리스너 등록
    const streamButton = document.querySelector('.stream-button');
    if (streamButton) {
        streamButton.addEventListener('click', startStream);
    }

    // 모달 관련 이벤트 리스너
    const modal = document.getElementById('questionModal');
    window.onclick = function(event) {
        if (event.target == modal) {
            closeQuestionModal();
        }
    }

    // 함수들을 전역 스코프에 추가
    window.openQuestionModal = openQuestionModal;
    window.closeQuestionModal = closeQuestionModal;
    window.submitQuestion = submitQuestion;

    // 초기 게시글 로드
    loadPosts();
});