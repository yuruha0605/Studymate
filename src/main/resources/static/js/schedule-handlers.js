
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
    const scheduleContainer = document.querySelector('.content-box:nth-child(2) .schedule-list');
    if (!scheduleContainer) {
        console.error('Schedule container not found');
        return;
    }

    if (!schedules || schedules.length === 0) {
        scheduleContainer.innerHTML = '<div class="no-content"><p>등록된 일정이 없습니다.</p></div>';
        return;
    }

    const scheduleItems = schedules.map(schedule => {
        console.log('Schedule data:', schedule); // 디버깅용
        const formattedDate = formatDateTime(schedule.scheduleDateTime);
        return `
            <div class="schedule-item" data-schedule-id="${schedule.id}">
                <h3>${escapeHtml(schedule.title)}</h3>
                <p class="schedule-info">
                    <span>${formattedDate}</span>
                </p>
                ${schedule.description ? `<p class="schedule-description">${escapeHtml(schedule.description)}</p>` : ''}
                <div class="schedule-actions">
                    <button class="edit-schedule-btn" onclick="editSchedule(${schedule.id})">수정</button>
                    <button class="delete-schedule-btn" onclick="deleteSchedule(${schedule.id})">삭제</button>
                </div>
            </div>
        `;
    }).join('');

    scheduleContainer.innerHTML = scheduleItems;
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
    const scheduleContainer = document.querySelector('.content-box:nth-child(2) .schedule-list');
    if (scheduleContainer) {
        scheduleContainer.innerHTML = '<div class="error-message">일정을 불러오는데 실패했습니다.</div>';
    }
}
