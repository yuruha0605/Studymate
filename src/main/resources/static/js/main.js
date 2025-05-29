

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
