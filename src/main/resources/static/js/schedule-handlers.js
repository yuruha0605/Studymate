
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


function displaySchedule(schedule) {
    const scheduleItem = document.createElement('div');
    scheduleItem.className = 'schedule-item';
    scheduleItem.setAttribute('data-id', schedule.id);

    const scheduleContent = `
        <div class="schedule-info" onclick="showScheduleDetail(${JSON.stringify(schedule).replace(/"/g, '&quot;')})">
            <h3>${schedule.title}</h3>
            <p class="schedule-datetime">${formatDateTime(schedule.scheduleDateTime)}</p>
        </div>
    `;

    scheduleItem.innerHTML = scheduleContent;
    return scheduleItem;
}

// function showScheduleDetail(schedule) {
//     // 기존 모달이 있다면 제거
//     const existingModal = document.querySelector('.schedule-detail-modal');
//     if (existingModal) {
//         existingModal.remove();
//     }
//
//     const currentUserId = document.querySelector('input[name="userId"]').value;
//     const modal = document.createElement('div');
//     modal.className = 'schedule-detail-modal modal';
//
//     modal.innerHTML = `
//         <div class="modal-content">
//             <span class="close" onclick="this.parentElement.parentElement.remove()">&times;</span>
//             <h2>${schedule.title}</h2>
//             <div class="schedule-detail-content">
//                 <p class="schedule-description">${schedule.description || '설명 없음'}</p>
//                 <p class="schedule-time">일정: ${formatDateTime(schedule.scheduleDateTime)}</p>
//             </div>
//             ${currentUserId == schedule.creatorId ? `
//                 <div class="schedule-actions">
//                     <button onclick="editSchedule(${schedule.id})" class="edit-btn">
//                         <i class="fas fa-edit"></i> 수정
//                     </button>
//                     <button onclick="deleteSchedule(${schedule.id})" class="delete-btn">
//                         <i class="fas fa-trash-alt"></i> 삭제
//                     </button>
//                 </div>
//             ` : ''}
//         </div>
//     `;
//
//     document.body.appendChild(modal);
//     modal.style.display = 'block';
//
//     // 모달 외부 클릭 시 닫기
//     modal.onclick = function(event) {
//         if (event.target === modal) {
//             modal.remove();
//         }
//     };
// }

function showScheduleDetail(schedule) {
    // 기존 모달이 있다면 제거
    const existingModal = document.querySelector('.schedule-detail-modal');
    if (existingModal) {
        existingModal.remove();
    }

    const currentUserId = document.querySelector('input[name="userId"]').value;
    const modal = document.createElement('div');
    modal.className = 'schedule-detail-modal modal';

    modal.innerHTML = `
        <div class="modal-content">
            <span class="close" onclick="this.parentElement.parentElement.remove()">&times;</span>
            <h2>${schedule.title}</h2>
            <div class="schedule-detail-content">
                <p class="schedule-description">${schedule.description || '설명 없음'}</p>
                <p class="schedule-time">일정: ${formatDateTime(schedule.scheduleDateTime)}</p>
            </div>
            ${currentUserId == schedule.creatorId ? `
                <div class="schedule-actions">
                    <button onclick="editSchedule(${schedule.id}); this.parentElement.parentElement.parentElement.remove();" class="edit-btn">
                        <i class="fas fa-edit"></i> 수정
                    </button>
                    <button onclick="deleteSchedule(${schedule.id})" class="delete-btn">
                        <i class="fas fa-trash-alt"></i> 삭제
                    </button>
                </div>
            ` : ''}
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'block';

    // 모달 외부 클릭 시 닫기
    modal.onclick = function(event) {
        if (event.target === modal) {
            modal.remove();
        }
    };
}

// handleScheduleSubmit 함수도 같은 방식으로 수정
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

    // LocalDateTime 형식에 맞춰 날짜와 시간 설정
    const scheduleDateTime = `${dateInput.value}T${timeInput.value}:00+09:00`;

    const scheduleRequest = {
        title: titleInput.value.trim(),
        description: descriptionInput.value.trim(),
        schedule_date_time: scheduleDateTime,  // snake_case로 변경
        studygroup_id: parseInt(studygroupId)  // snake_case로 변경
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
            body: JSON.stringify(scheduleRequest)
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
        schedules.map(schedule => {
            const scheduleHtml = `
                        <div class="schedule-item" data-id="${schedule.id}">
                            <div class="schedule-info" onclick="showScheduleDetail(${JSON.stringify(schedule).replace(/"/g, '&quot;')})">
                                <div class="schedule-header">
                                    <h3>${schedule.title}</h3>
                                    <span class="schedule-datetime">${formatDateTime(schedule.scheduleDateTime)}</span>
                                </div>
                            </div>
                        </div>
                    `;
            return scheduleHtml;
        }).join('')
    }
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
    // 기존에 열려있는 모달들 모두 제거
    const existingModals = document.querySelectorAll('.modal');
    existingModals.forEach(modal => modal.remove());

    // 수정 모달 HTML 생성 및 추가
    const editModal = document.createElement('div');
    editModal.className = 'modal';
    editModal.id = 'scheduleModal';

    const dateTime = new Date(schedule.scheduleDateTime);
    const dateStr = dateTime.toISOString().split('T')[0];
    const hours = String(dateTime.getHours()).padStart(2, '0');
    const minutes = String(dateTime.getMinutes()).padStart(2, '0');

    editModal.innerHTML = `
        <div class="modal-content">
            <span class="close" onclick="this.closest('.modal').remove()">&times;</span>
            <h2 class="modal-title">일정 수정</h2>
            <form id="scheduleForm">
                <div class="form-group">
                    <label for="scheduleTitle">제목</label>
                    <input type="text" id="scheduleTitle" name="title" value="${schedule.title || ''}" required>
                </div>
                <div class="form-group">
                    <label for="scheduleDescription">설명</label>
                    <textarea id="scheduleDescription" name="description">${schedule.description || ''}</textarea>
                </div>
                <div class="form-group">
                    <label for="scheduleDate">날짜</label>
                    <input type="date" id="scheduleDate" name="date" value="${dateStr}" required>
                </div>
                <div class="form-group">
                    <label for="scheduleTime">시간</label>
                    <input type="time" id="scheduleTime" name="time" value="${hours}:${minutes}" required>
                </div>
                <div class="form-actions">
                    <button type="submit" class="submit-btn">수정하기</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(editModal);
    editModal.style.display = 'block';

    // 수정 폼에 이벤트 리스너 추가
    const form = editModal.querySelector('#scheduleForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const pathParts = window.location.pathname.split('/');
        const studygroupId = pathParts[pathParts.length - 1];

        // 날짜와 시간 형식 맞추기
        const date = document.getElementById('scheduleDate').value;
        const time = document.getElementById('scheduleTime').value;

        const scheduleRequest = {
            title: document.getElementById('scheduleTitle').value,
            description: document.getElementById('scheduleDescription').value,
            schedule_date_time: `${date}T${time}:00+09:00`, // snake_case로 변경
            studygroup_id: parseInt(studygroupId) // snake_case로 변경
        };

        try {
            const response = await fetch(`/api/study-mate/schedules/${schedule.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(scheduleRequest)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || '일정 수정에 실패했습니다.');
            }

            alert('일정이 성공적으로 수정되었습니다.');
            editModal.remove();
            loadSchedules();
        } catch (error) {
            console.error('Error:', error);
            alert(error.message);
        }
    });

    // 모달 외부 클릭 시 닫기
    editModal.onclick = function(event) {
        if (event.target === editModal) {
            editModal.remove();
        }
    };
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

