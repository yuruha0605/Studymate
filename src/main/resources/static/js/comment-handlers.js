


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
        <div class="comment">
            <div class="comment-header">
                <span class="comment-author">${maskEmail(comment.author_email || '익명')}</span>
                <span class="comment-date">${formatDateTime(comment.created_at)}</span>
            </div>
            <div class="comment-content">${escapeHtml(comment.content)}</div>
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