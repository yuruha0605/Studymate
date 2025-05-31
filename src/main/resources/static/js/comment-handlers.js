


async function loadComments(postId) {
    try {
        const response = await fetch(`/api/study-mate/replies/post/${postId}`);
        if (!response.ok) throw new Error('댓글을 불러오는데 실패했습니다.');
        const comments = await response.json();
        displayComments(comments);
    } catch (error) {
        console.error('Error loading comments:', error);
        const commentsList = document.querySelector('.comments-list');
        commentsList.innerHTML = '<p class="error-message">댓글을 불러오는데 실패했습니다.</p>';
    }
}


function displayComments(comments) {
    const commentsListElement = document.querySelector('.comments-list');

    if (!comments || comments.length === 0) {
        commentsListElement.innerHTML = '<p>등록된 댓글이 없습니다.</p>';
        return;
    }

    const commentHtml = comments.map(comment => `
        <div class="comment" data-comment-id="${comment.id}">
            <div class="comment-header">
                <div class="comment-info">
                    <span class="comment-author">${maskEmail(comment.author_email || '익명')}</span>
                    <span class="comment-date">${formatDateTime(comment.created_at)}</span>
                </div>
                <div class="comment-actions">
                    <button class="comment-btn" onclick="editComment(${comment.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="comment-btn" onclick="deleteComment(${comment.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <div class="comment-content" id="comment-content-${comment.id}">${escapeHtml(comment.content)}</div>
            <div class="comment-edit-form" id="comment-edit-${comment.id}" style="display: none;">
                <textarea class="edit-comment-text">${escapeHtml(comment.content)}</textarea>
                <div class="edit-actions">
                    <button onclick="updateComment(${comment.id})" class="save-btn">저장</button>
                    <button onclick="cancelEdit(${comment.id})" class="cancel-btn">취소</button>
                </div>
            </div>
        </div>
    `).join('');

    commentsListElement.innerHTML = commentHtml;
}





async function addComment() {
    const commentText = document.getElementById('commentText').value.trim();
    const selectedPost = document.getElementById('selectedPost');
    const postId = selectedPost.dataset.postId;

    if (!commentText) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    try {
        const response = await fetch('/api/study-mate/replies/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: commentText,
                post_id: parseInt(postId)
            })
        });

        if (!response.ok) {
            throw new Error('댓글 등록에 실패했습니다.');
        }

        // 댓글 입력창 초기화
        document.getElementById('commentText').value = '';

        // 댓글 목록 새로고침
        await loadComments(postId);
    } catch (error) {
        console.error('Error:', error);
        alert('댓글 등록에 실패했습니다: ' + error.message);
    }
}

function updateCommentsList(comments) {
    const commentsContainer = document.querySelector('.comments-list');
    if (!comments || comments.length === 0) {
        commentsContainer.innerHTML = '<div class="no-content">첫 번째 댓글을 작성해보세요.</div>';
        return;
    }

    const commentItems = comments.map(comment => `
        <div class="comment-item">
            <div class="comment-header">
                <span class="comment-author">${maskEmail(comment.authorEmail)}</span>
                <span class="comment-date">${formatDate(comment.createdAt)}</span>
            </div>
            <div class="comment-text">${escapeHtml(comment.content)}</div>
        </div>
    `).join('');

    commentsContainer.innerHTML = commentItems;
}

function getCurrentUserEmail() {
    // 현재 로그인한 사용자의 이메일을 반환하는 함수
    // 실제 구현에서는 적절한 방식으로 현재 사용자 정보를 가져와야 함
    return document.querySelector('[data-user-email]')?.dataset.userEmail;
}


async function deleteComment(commentId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/replies/${commentId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('댓글 삭제에 실패했습니다.');

        // 현재 게시글의 댓글 목록 새로고침
        const postId = document.getElementById('selectedPost').dataset.postId;
        await loadComments(postId);
    } catch (error) {
        console.error('Error:', error);
        alert('댓글 삭제에 실패했습니다.');
    }
}



function editComment(commentId) {
    const contentDiv = document.getElementById(`comment-content-${commentId}`);
    const editForm = document.getElementById(`comment-edit-${commentId}`);

    if (contentDiv && editForm) {
        contentDiv.style.display = 'none';
        editForm.style.display = 'block';
    }
}


function cancelEdit(commentId) {
    const contentDiv = document.getElementById(`comment-content-${commentId}`);
    const editForm = document.getElementById(`comment-edit-${commentId}`);

    if (contentDiv && editForm) {
        contentDiv.style.display = 'block';
        editForm.style.display = 'none';
    }
}


async function updateComment(commentId) {
    const editForm = document.getElementById(`comment-edit-${commentId}`);
    const content = editForm.querySelector('.edit-comment-text').value.trim();
    const postId = document.getElementById('selectedPost').dataset.postId;

    if (!content) {
        alert('댓글 내용을 입력해주세요.');
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/replies/${commentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: content,
                post_id: postId  // postId 추가
            })
        });

        if (!response.ok) throw new Error('댓글 수정에 실패했습니다.');

        // 댓글 목록 새로고침
        await loadComments(postId);
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}


function createCommentElement(comment) {
    const currentUserId = document.querySelector('input[name="userId"]').value;
    const isAuthor = comment.authorId.toString() === currentUserId;

    const commentElement = document.createElement('div');
    commentElement.className = 'comment';
    commentElement.id = `comment-${comment.id}`;

    commentElement.innerHTML = `
        <div class="comment-header">
            <div class="comment-info">
                <span class="comment-author">${comment.authorEmail || '익명'}</span>
                <span class="comment-date">${formatDateTime(comment.createdAt)}</span>
            </div>
            ${isAuthor ? `
            <div class="comment-actions">
                <button class="edit-comment-btn" onclick="startEditComment(${comment.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="delete-comment-btn" onclick="deleteComment(${comment.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
            ` : ''}
        </div>
        <div class="comment-content" id="comment-content-${comment.id}">
            ${comment.content}
        </div>
        <div class="comment-edit-form" id="comment-edit-${comment.id}" style="display: none;">
            <textarea class="edit-comment-text">${comment.content}</textarea>
            <div class="edit-actions">
                <button onclick="updateComment(${comment.id})">저장</button>
                <button onclick="cancelEdit(${comment.id})">취소</button>
            </div>
        </div>
    `;

    return commentElement;
}
