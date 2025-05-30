
function initializeScheduleHandlers() {
    // 일정 등록 버튼은 HTML에 직접 onclick이 있으므로 제거

    // 일정 폼 제출 핸들러
    const scheduleForm = document.getElementById('scheduleForm');
    if (scheduleForm) {
        scheduleForm.addEventListener('submit', handleScheduleSubmit);
    }

    // 일정 목록 로드
    loadSchedules();
}

function openScheduleModal() {
    const modal = document.getElementById('scheduleModal');
    // 새로운 일정 추가 모드로 설정
    modal.dataset.mode = 'create';
    delete modal.dataset.scheduleId;

    // 폼 초기화
    document.getElementById('scheduleForm').reset();

    // 모달 제목 설정
    modal.querySelector('.modal-title').textContent = '일정 추가';

    modal.style.display = 'block';
}

function closeScheduleModal() {
    const modal = document.getElementById('scheduleModal');
    modal.style.display = 'none';
    document.getElementById('scheduleForm').reset();
}

async function loadSchedules() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/schedules/studygroup/${studygroupId}`);
        if (!response.ok) throw new Error('일정을 불러오는데 실패했습니다.');
        const schedules = await response.json();
        updateScheduleList(schedules);
    } catch (error) {
        console.error('Error:', error);
        showScheduleError();
    }
}

// schedule-handlers.js 파일에서 일정을 표시하는 부분 수정
function displaySchedule(schedule) {
    const scheduleItem = document.createElement('div');
    scheduleItem.className = 'schedule-item';
    scheduleItem.setAttribute('data-id', schedule.id);

    const scheduleContent = `
        <div class="schedule-info">
            <h3>${schedule.title}</h3>
            <p>${schedule.description || ''}</p>
            <p class="schedule-datetime">${formatDateTime(schedule.scheduleDateTime)}</p>
        </div>
        <div class="schedule-actions">
            <button onclick="editSchedule(${schedule.id})" class="edit-btn">
                <i class="fas fa-edit"></i> 수정
            </button>
            <button onclick="deleteSchedule(${schedule.id})" class="delete-btn">
                <i class="fas fa-trash-alt"></i> 삭제
            </button>
        </div>
    `;

    scheduleItem.innerHTML = scheduleContent;
    return scheduleItem;
}

async function handleScheduleSubmit(event) {
    event.preventDefault();

    const modal = document.getElementById('scheduleModal');
    const isEditMode = modal.dataset.mode === 'edit';
    const scheduleId = modal.dataset.scheduleId;

    const titleInput = document.getElementById('scheduleTitle');
    const dateInput = document.getElementById('scheduleDate');
    const timeInput = document.getElementById('scheduleTime');
    const descriptionInput = document.getElementById('scheduleDescription');
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    if (!titleInput.value.trim() || !dateInput.value || !timeInput.value) {
        alert('제목, 날짜, 시간은 필수 입력 항목입니다.');
        return;
    }

    const scheduleData = {
        title: titleInput.value.trim(),
        description: descriptionInput.value.trim(),
        schedule_date_time: `${dateInput.value}T${timeInput.value}:00+09:00`,
        studygroup_id: parseInt(studygroupId)
    };

    try {
        const url = isEditMode
            ? `/api/study-mate/schedules/${scheduleId}`
            : '/api/study-mate/schedules/create';

        const method = isEditMode ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(scheduleData)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || '일정 저장에 실패했습니다.');
        }

        alert(isEditMode ? '일정이 수정되었습니다.' : '일정이 등록되었습니다.');
        closeScheduleModal();
        loadSchedules();
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}


function updateScheduleList(schedules) {
    const scheduleContainer = document.querySelector('.right-section .content-box:last-child');
    if (!scheduleContainer) return;

    // 현재 로그인한 사용자의 ID 가져오기
    const currentUserId = document.querySelector('input[name="userId"]').value;

    const content = `
        <h2>
            학습 일정
            <button class="action-btn" onclick="openScheduleModal()">
                <i class="fas fa-plus"></i> 일정 추가
            </button>
        </h2>
        <div class="schedule-list">
            ${!schedules || schedules.length === 0 ?
        '<div class="no-content">등록된 일정이 없습니다.</div>' :
        schedules.map(schedule => `
                <div class="schedule-item">
                    <div class="schedule-info">
                        <h3>${schedule.title}</h3>
                        <p>${schedule.description || ''}</p>
                        <p class="schedule-datetime">${formatDateTime(schedule.scheduleDateTime)}</p>
                    </div>
                    ${currentUserId == schedule.creatorId ? `
                        <div class="schedule-actions">
                            <button onclick="editSchedule(${schedule.id})" class="edit-btn">
                                <i class="fas fa-edit"></i> 수정
                            </button>
                            <button onclick="deleteSchedule(${schedule.id})" class="delete-btn">
                                <i class="fas fa-trash-alt"></i> 삭제
                            </button>
                        </div>
                    ` : ''}
                </div>
            `).join('')}
        </div>
    `;

    scheduleContainer.innerHTML = content;
}


// 수정 함수 분리
async function editSchedule(scheduleId) {
    try {
        const response = await fetch(`/api/study-mate/schedules/${scheduleId}`);
        if (!response.ok) throw new Error('일정 정보를 불러오는데 실패했습니다.');

        const schedule = await response.json();
        openEditScheduleModal(schedule);
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}


function openEditScheduleModal(schedule) {
    const modal = document.getElementById('scheduleModal');

    // 기본 정보 설정
    document.getElementById('scheduleTitle').value = schedule.title || '';
    document.getElementById('scheduleDescription').value = schedule.description || '';

    // 날짜와 시간 설정
    if (schedule.scheduleDateTime) {
        try {
            const dateTime = new Date(schedule.scheduleDateTime);

            // 날짜 설정 (YYYY-MM-DD 형식)
            const dateStr = dateTime.toISOString().split('T')[0];
            document.getElementById('scheduleDate').value = dateStr;

            // 시간 설정 (HH:mm 형식)
            const hours = String(dateTime.getHours()).padStart(2, '0');
            const minutes = String(dateTime.getMinutes()).padStart(2, '0');
            document.getElementById('scheduleTime').value = `${hours}:${minutes}`;
        } catch (error) {
            console.error('Error parsing date:', error);
        }
    }

    // 모달 설정
    modal.dataset.mode = 'edit';
    modal.dataset.scheduleId = schedule.id;
    modal.querySelector('.modal-title').textContent = '일정 수정';
    modal.style.display = 'block';
}

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

function showScheduleError() {
    const scheduleContainer = document.querySelector('.right-section .content-box:last-child');
    if (scheduleContainer) {
        scheduleContainer.innerHTML = `
            <h2>
                학습 일정
                <button class="action-btn" onclick="openScheduleModal()">
                    <i class="fas fa-plus"></i> 일정 추가
                </button>
            </h2>
            <div class="error-message">일정을 불러오는데 실패했습니다.</div>
        `;
    }
}

