let currentPostId = null;

function initializePostHandlers() {
    const questionBtn = document.querySelector('.content-box .action-btn');
    if (questionBtn) {
        questionBtn.addEventListener('click', openQuestionModal);
    }
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
    } catch (error) {
        console.error('Error loading board info:', error);
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

async function loadPosts() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const boardResponse = await fetch(`/api/study-mate/boards/studygroup/${studygroupId}`);
        if (!boardResponse.ok) {
            throw new Error('게시판을 찾을 수 없습니다.');
        }
        const boardData = await boardResponse.json();

        const boardIdInput = document.getElementById('boardId');
        if (boardIdInput) {
            boardIdInput.value = boardData.id;
        }

        const postsResponse = await fetch(`/api/study-mate/posts/${boardData.id}/list?page=0&size=10`);
        if (!postsResponse.ok) {
            throw new Error('게시글을 불러오는데 실패했습니다.');
        }

        const postsData = await postsResponse.json();
        if (postsData && postsData.body && Array.isArray(postsData.body)) {
            updatePostList(postsData.body);
        } else {
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

function updatePostList(posts) {
    const postListContainer = document.querySelector('.post-list');
    if (!posts || posts.length === 0) {
        postListContainer.innerHTML = '<div class="no-content"><p>등록된 게시글이 없습니다.</p></div>';
        return;
    }

    const postsHtml = posts.map(post => `
        <div class="post-item" data-post-id="${post.id}" onclick="showPost(${post.id})">
            <div class="post-info">
                <span class="post-title">${escapeHtml(post.title)}</span>
                <span class="post-author">${post.authorEmail || '익명'}</span>
            </div>
            <div class="post-date">${formatDateTime(post.written)}</div>
        </div>
    `).join('');

    postListContainer.innerHTML = postsHtml;

    // 이벤트 리스너 추가
    document.querySelectorAll('.post-item').forEach(item => {
        item.addEventListener('click', function() {
            const postId = this.dataset.postId;
            if (postId) {
                showPost(postId);
            }
        });
    });
}

async function showPost(postId) {
    try {
        const response = await fetch(`/api/study-mate/posts/${postId}`);
        if (!response.ok) throw new Error('게시글을 불러오는데 실패했습니다.');

        const post = await response.json();
        const currentUserId = document.querySelector('input[name="userId"]').value;

        const selectedPost = document.getElementById('selectedPost');
        selectedPost.style.display = 'block';
        selectedPost.classList.add('show');
        selectedPost.dataset.postId = post.id;

        const titleElement = selectedPost.querySelector('.selected-title');
        const contentElement = selectedPost.querySelector('.post-content');
        const authorElement = selectedPost.querySelector('.selected-author');

        if (titleElement) titleElement.textContent = post.title;
        if (contentElement) contentElement.textContent = post.content;
        // 이메일 마스킹 적용
        if (authorElement) authorElement.textContent = `작성자: ${maskEmail(post.author_email)}`;

        const editBtn = selectedPost.querySelector('.edit-btn');
        const deleteBtn = selectedPost.querySelector('.delete-btn');

        if (editBtn && deleteBtn) {
            const isAuthor = post.author_id && currentUserId === post.author_id.toString();
            editBtn.style.display = isAuthor ? 'block' : 'none';
            deleteBtn.style.display = isAuthor ? 'block' : 'none';
        }

        loadComments(postId);
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