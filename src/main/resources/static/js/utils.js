// 유틸리티 함수들
function formatDate(dateString) {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
}

function formatDateTime(dateTimeStr) {
    try {
        if (!dateTimeStr) {
            return '날짜 미정';
        }

        const date = new Date(dateTimeStr);
        if (isNaN(date.getTime())) {
            return '날짜 형식 오류';
        }

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}`;
    } catch (error) {
        console.error('Error formatting date:', error, 'Input:', dateTimeStr);
        return '날짜 형식 오류';
    }
}



function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function maskEmail(email) {
    if (!email) return '익명';
    const [username, domain] = email.split('@');
    if (!domain) return email;
    return `${username.substring(0, 3)}***@${domain}`;
}

