


let currentPage = 0;
const pageSize = 10;
let currentPostId = null;

function initializePostHandlers() {
    const questionBtn = document.querySelector('.content-box .action-btn');
    if (questionBtn) {
        questionBtn.addEventListener('click', openQuestionModal);
    }
    loadPosts(0); // 초기 페이지 로드
}


async function loadBoardInfo() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/boards/studygroup/${studygroupId}`);
        if (!response.ok) throw new Error('Failed to load board info');
        const boardData = await response.json();

        let boardIdInput = document.getElementById('boardId');
        if (!boardIdInput) {
            boardIdInput = document.createElement('input');
            boardIdInput.type = 'hidden';
            boardIdInput.id = 'boardId';
            document.getElementById('questionForm').appendChild(boardIdInput);
        }
        boardIdInput.value = boardData.id;
        return boardData.id;
    } catch (error) {
        console.error('Error loading board info:', error);
        throw error;
    }
}

async function handleQuestionSubmit(event) {
    event.preventDefault();

    const modal = document.getElementById('questionModal');
    const isEdit = modal.dataset.mode === 'edit';
    const postId = modal.dataset.postId;

    const titleInput = document.getElementById('title');
    const contentInput = document.getElementById('content');
    const boardIdInput = document.getElementById('boardId');
    const studygroupIdInput = document.getElementById('studygroupId');

    if (!titleInput.value.trim() || !contentInput.value.trim()) {
        alert('제목과 내용을 모두 입력해주세요.');
        return;
    }

    const postData = {
        title: titleInput.value.trim(),
        content: contentInput.value.trim(),
        board_id: parseInt(boardIdInput.value),
        studygroup_id: parseInt(studygroupIdInput.value)
    };

    try {
        const url = isEdit ? `/api/study-mate/posts/${postId}` : '/api/study-mate/posts/create';
        const response = await fetch(url, {
            method: isEdit ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Network response was not ok');
        }

        alert(isEdit ? '게시글이 수정되었습니다.' : '게시글이 등록되었습니다.');
        closeQuestionModal();
        loadPosts();
        event.target.reset();

        if (isEdit) {
            modal.dataset.mode = 'create';
            modal.dataset.postId = '';
            modal.querySelector('h2').textContent = '질문하기';
            modal.querySelector('.submit-btn').textContent = '작성완료';
            hideSelectedPost();
        }
    } catch (error) {
        console.error('Error:', error);
        alert((isEdit ? '게시글 수정' : '게시글 등록') + '에 실패했습니다: ' + error.message);
    }
}

async function loadPosts(page = 0) {
    try {
        const pathParts = window.location.pathname.split('/');
        const studygroupId = pathParts[pathParts.length - 1];

        const response = await fetch(`/api/study-mate/posts/${studygroupId}/list?page=${page}&size=${pageSize}`);

        if (!response.ok) {
            throw new Error('게시글을 불러오는데 실패했습니다.');
        }

        const data = await response.json();

        if (data && data.body) {
            updatePostList(data.body);
            if (data.pagination) {
                updatePagination(data.pagination);
            }
        } else {
            showNoContentMessage(document.querySelector('.post-list'));
        }
    } catch (error) {
        console.error('Error loading posts:', error);
        showErrorMessage(document.querySelector('.post-list'));
    }
}


function updatePostList(posts) {
    const postListContainer = document.querySelector('.post-list');
    if (!posts || posts.length === 0) {
        postListContainer.innerHTML = '<div class="no-content"><p>등록된 게시글이 없습니다.</p></div>';
        return;
    }

    const postsHtml = posts.map(post => `
        <div class="post-item" data-post-id="${post.id}" onclick="showPost(${post.id})">
            <div class="post-title">${escapeHtml(post.title)}</div>
            <div class="post-author">${post.authorEmail || '익명'}</div>
            <div class="post-date">${formatDateTime(post.written)}</div>
            <div class="post-arrow">
                <i class="fas fa-chevron-right"></i>
            </div>
        </div>
    `).join('');

    postListContainer.innerHTML = postsHtml;
}

async function showPost(postId) {
    try {
        const response = await fetch(`/api/study-mate/posts/${postId}`);
        if (!response.ok) throw new Error('게시글을 불러오는데 실패했습니다.');

        const post = await response.json();
        const currentUserId = document.querySelector('input[name="userId"]').value;

        const selectedPost = document.getElementById('selectedPost');
        selectedPost.style.display = 'block'; // 이 부분이 중요합니다
        selectedPost.classList.add('show');
        selectedPost.dataset.postId = post.id;

        // 각 요소 업데이트
        const titleElement = selectedPost.querySelector('.selected-title');
        const contentElement = selectedPost.querySelector('.post-content');
        const authorElement = selectedPost.querySelector('.selected-author');

        if (titleElement) titleElement.textContent = post.title;
        if (contentElement) contentElement.textContent = post.content;
        if (authorElement) authorElement.textContent = `작성자: ${maskEmail(post.author_email)}`;

        // 수정/삭제 버튼 표시 여부
        const editBtn = selectedPost.querySelector('.edit-btn');
        const deleteBtn = selectedPost.querySelector('.delete-btn');

        if (editBtn && deleteBtn) {
            const isAuthor = post.author_id && currentUserId === post.author_id.toString();
            editBtn.style.display = isAuthor ? 'block' : 'none';
            deleteBtn.style.display = isAuthor ? 'block' : 'none';
        }

        // 댓글 로드
        await loadComments(postId);
        
        // z-index 설정 추가
        selectedPost.style.zIndex = '1000';
        
    } catch (error) {
        console.error('Error in showPost:', error);
        alert(error.message);
    }
}

async function editPost() {
    const selectedPost = document.getElementById('selectedPost');
    const postId = selectedPost.dataset.postId;

    try {
        const response = await fetch(`/api/study-mate/posts/${postId}`);
        if (!response.ok) throw new Error('게시글 정보를 불러오는데 실패했습니다.');

        const post = await response.json();
        const modal = document.getElementById('questionModal');

        modal.dataset.mode = 'edit';
        modal.dataset.postId = postId;

        document.getElementById('title').value = post.title;
        document.getElementById('content').value = post.content;

        modal.querySelector('h2').textContent = '게시글 수정';
        modal.querySelector('.submit-btn').textContent = '수정완료';

        openQuestionModal();
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

async function deletePost() {
    const selectedPost = document.getElementById('selectedPost');
    const postId = selectedPost.dataset.postId;

    if (!confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/posts/${postId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('게시글 삭제에 실패했습니다.');

        alert('게시글이 삭제되었습니다.');
        hideSelectedPost();
        loadPosts();
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

function hideSelectedPost() {
    const selectedPost = document.getElementById('selectedPost');
    if (selectedPost) {
        selectedPost.style.display = 'none';
        selectedPost.dataset.postId = '';
    }
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

function updatePagination(pagination) {
    const paginationContainer = document.querySelector('.pagination');
    if (!paginationContainer) return;

    const prevBtn = paginationContainer.querySelector('.prev');
    const nextBtn = paginationContainer.querySelector('.next');
    const pageNumbers = paginationContainer.querySelector('.page-numbers');

    // 이벤트 리스너 추가
    prevBtn.onclick = () => changePage(currentPage - 1);
    nextBtn.onclick = () => changePage(currentPage + 1);

    if (pagination.totalPage > 0) {
        pageNumbers.innerHTML = '';
        for (let i = 0; i < pagination.totalPage; i++) {
            const button = document.createElement('button');
            button.className = `page-btn number ${i === pagination.page ? 'active' : ''}`;
            button.textContent = i + 1;
            button.onclick = () => changePage(i);
            pageNumbers.appendChild(button);
        }

        prevBtn.disabled = pagination.page === 0;
        nextBtn.disabled = pagination.page === pagination.totalPage - 1;
        currentPage = pagination.page;
    }
}


async function changePage(page) {
    await loadPosts(page);
    scrollToTop();
}

function scrollToTop() {
    document.querySelector('.post-list').scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

document.addEventListener('DOMContentLoaded', function() {
    initializePostHandlers();

    // 폼 제출 이벤트 리스너 추가
    const questionForm = document.getElementById('questionForm');
    if (questionForm) {
        questionForm.addEventListener('submit', handleQuestionSubmit);
    }
});


function openQuestionModal() {
    const modal = document.getElementById('questionModal');
    if (modal) {
        modal.style.display = 'block';
        // 모달 초기화
        const form = document.getElementById('questionForm');
        if (form) {
            form.reset();
        }
    }
}

function closeQuestionModal() {
    const modal = document.getElementById('questionModal');
    if (modal) {
        modal.style.display = 'none';
    }
}