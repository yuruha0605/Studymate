

// main.js
document.addEventListener('DOMContentLoaded', function() {
    // 초기화 함수들
    initializeModalHandlers();
    initializeStreamHandlers();
    initializePostHandlers();
    initializeScheduleHandlers();  // 추가된 부분


    // 데이터 로드
    loadPosts();
    loadBoardInfo();
    loadMaterials();
    loadSchedules();

    // 폼 이벤트 리스너들
    const uploadForm = document.getElementById('uploadForm');
    if (uploadForm) {
        uploadForm.addEventListener('submit', handleUploadSubmit);
    }

    const scheduleForm = document.getElementById('scheduleForm');
    if (scheduleForm) {
        scheduleForm.addEventListener('submit', handleScheduleSubmit);
    }
});


function confirmLeave() {
    const leaveConfirmModal = document.getElementById('leaveConfirmModal');
    leaveConfirmModal.style.display = 'block';
}

function closeLeaveConfirmModal() {
    const leaveConfirmModal = document.getElementById('leaveConfirmModal');
    leaveConfirmModal.style.display = 'none';
}

async function leaveGroup() {
    const groupId = window.location.pathname.split('/').pop();
    const userIdElement = document.querySelector('input[name="userId"]');

    if (!userIdElement) {
        alert('로그인이 필요한 기능입니다.');
        return;
    }

    try {
        const response = await fetch(`/api/study-mate/studygroups/${groupId}/leave?userId=${userIdElement.value}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || '스터디 탈퇴에 실패했습니다.');
        }

        alert('스터디에서 탈퇴하였습니다.');
        window.location.href = '/study-mate/myclass';  // 마이클래스 페이지로 리다이렉션
    } catch (error) {
        console.error('Error:', error);
        alert(error.message || '스터디 탈퇴 중 오류가 발생했습니다.');
    } finally {
        closeLeaveConfirmModal();
    }
}