
// 모달 초기화 함수 추가
function initializeModal() {
    const uploadModal = document.getElementById('uploadModal');
    const closeBtn = uploadModal.querySelector('.close');
    const cancelBtn = uploadModal.querySelector('.cancel-btn');
    const uploadForm = uploadModal.querySelector('form');

    // x 버튼 클릭 이벤트
    closeBtn.onclick = function() {
        resetAndCloseModal();
    };

    // 취소 버튼 클릭 이벤트
    cancelBtn.onclick = function() {
        resetAndCloseModal();
    };

    // 모달 외부 클릭 이벤트
    window.onclick = function(event) {
        if (event.target == uploadModal) {
            resetAndCloseModal();
        }
    };

    // 폼 제출 이벤트를 addEventListener로 변경하고 이전 핸들러 제거
    uploadForm.onsubmit = null; // 기존 이벤트 핸들러 제거
    uploadForm.removeEventListener('submit', handleUploadSubmit); // 혹시 있을 수 있는 이벤트 리스너 제거
    uploadForm.addEventListener('submit', handleUploadSubmit, { once: true }); // once 옵션으로 한 번만 실행되도록 설정
}



// 모달 초기화 및 닫기 함수
// 모달 초기화 및 닫기 함수
function resetAndCloseModal() {
    const modal = document.getElementById('uploadModal');
    if (modal) {
        // 모달 상태 초기화
        modal.dataset.mode = 'create';
        modal.dataset.materialId = '';

        // 폼 초기화
        const form = modal.querySelector('form');
        if (form) form.reset();

        // 파일 입력 필드 필수값으로 설정
        const fileInput = document.getElementById('fileUpload');
        if (fileInput) fileInput.required = true;

        // 기존 파일 정보 제거
        const currentFileInfo = modal.querySelector('.current-file-info');
        if (currentFileInfo) {
            currentFileInfo.remove();
        }

        // 제목 초기화
        modal.querySelector('.modal-title').textContent = '자료 업로드';

        // 모달 닫기
        modal.style.display = 'none';
    }
}

// 업로드 모달 열기 함수 수정
// 업로드 모달 열기 함수 수정
function openUploadModal() {
    const modal = document.getElementById('uploadModal');

    // 모달 상태 초기화
    modal.dataset.mode = 'create';
    modal.dataset.materialId = '';

    // 폼 초기화
    const form = modal.querySelector('form');
    if (form) form.reset();

    // 파일 입력 필드 필수값으로 설정
    const fileInput = document.getElementById('fileUpload');
    if (fileInput) fileInput.required = true;

    // 제목 초기화
    modal.querySelector('.modal-title').textContent = '자료 업로드';

    // 기존 파일 정보 제거
    const currentFileInfo = modal.querySelector('.current-file-info');
    if (currentFileInfo) {
        currentFileInfo.remove();
    }

    // 모달 표시
    modal.style.display = 'block';
}

async function loadMaterials() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/materials/studygroup/${studygroupId}`);
        if (!response.ok) throw new Error('자료를 불러오는데 실패했습니다.');
        const materials = await response.json();

        // 최신순으로 정렬
        materials.sort((a, b) => new Date(b.uploadedAt) - new Date(a.uploadedAt));

        updateMaterialList(materials);
    } catch (error) {
        console.error('Error:', error);
        showMaterialError();
    }
}

async function handleUploadSubmit(event) {
    event.preventDefault();
    event.stopPropagation(); // 이벤트 전파 중지

    // 중복 제출 방지를 위한 플래그
    if (event.target.dataset.isSubmitting === 'true') {
        return;
    }

    const submitButton = event.target.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = true;
    }

    try {
        event.target.dataset.isSubmitting = 'true'; // 제출 진행 중 표시

        const modal = document.getElementById('uploadModal');
        const isEditMode = modal.dataset.mode === 'edit';
        const materialId = modal.dataset.materialId;

        const titleInput = document.getElementById('fileTitle');
        const descriptionInput = document.getElementById('fileDescription');
        const fileInput = document.getElementById('fileUpload');
        const pathParts = window.location.pathname.split('/');
        const studygroupId = pathParts[pathParts.length - 1];
        const creatorId = document.querySelector('input[name="userId"]').value;

        if (!titleInput.value.trim()) {
            alert('제목은 필수 입력 항목입니다.');
            return;
        }

        let response;
        const formData = new FormData();
        formData.append('title', titleInput.value.trim());
        formData.append('description', descriptionInput.value.trim());

        if (isEditMode) {
            if (fileInput.files[0]) {
                formData.append('file', fileInput.files[0]);
            }
            response = await fetch(`/api/study-mate/materials/${materialId}`, {
                method: 'PUT',
                body: formData
            });
        } else {
            if (!fileInput.files[0]) {
                alert('파일을 선택해주세요.');
                return;
            }
            formData.append('file', fileInput.files[0]);
            formData.append('studygroupId', studygroupId);
            formData.append('creatorId', creatorId);

            response = await fetch('/api/study-mate/materials/upload', {
                method: 'POST',
                body: formData
            });
        }

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || (isEditMode ? '자료 수정에 실패했습니다.' : '자료 업로드에 실패했습니다.'));
        }

        // 성공 시 처리
        resetAndCloseModal();
        await loadMaterials(); // 목록 새로고침
        alert(isEditMode ? '자료가 성공적으로 수정되었습니다.' : '자료가 성공적으로 업로드되었습니다.');

    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    } finally {
        if (submitButton) {
            submitButton.disabled = false;
        }
        event.target.dataset.isSubmitting = 'false'; // 제출 완료 표시

        // 이벤트 리스너 재설정
        const uploadForm = document.getElementById('uploadForm');
        if (uploadForm) {
            uploadForm.removeEventListener('submit', handleUploadSubmit);
            uploadForm.addEventListener('submit', handleUploadSubmit, { once: true });
        }
    }
}

async function editMaterial(materialId) {
    try {
        const userIdElement = document.querySelector('input[name="userId"]');
        if (!userIdElement) {
            throw new Error('사용자 정보를 찾을 수 없습니다.');
        }

        const response = await fetch(`/api/study-mate/materials/${materialId}`);
        if (!response.ok) {
            throw new Error('자료 정보를 불러오는데 실패했습니다.');
        }

        const material = await response.json();

        if (String(material.creatorId) !== String(userIdElement.value)) {
            throw new Error('자료를 수정할 권한이 없습니다.');
        }

        const modal = document.getElementById('uploadModal');
        // 기존 파일 정보 제거
        const currentFileInfo = modal.querySelector('.current-file-info');
        if (currentFileInfo) {
            currentFileInfo.remove();
        }

        modal.dataset.mode = 'edit';
        modal.dataset.materialId = materialId;
        modal.querySelector('.modal-title').textContent = '자료 수정';

        document.getElementById('fileTitle').value = material.title;
        document.getElementById('fileDescription').value = material.description || '';

        const fileInput = document.getElementById('fileUpload');
        fileInput.value = '';
        fileInput.required = false;

        // 현재 파일 정보 표시 (fileName 대신 originalFileName 사용)
        const fileInfoDiv = document.createElement('div');
        fileInfoDiv.className = 'current-file-info';
        fileInfoDiv.innerHTML = `
            <p>현재 파일: ${material.fileName || '파일 정보 없음'}</p>
            <small>새 파일을 선택하지 않으면 기존 파일이 유지됩니다.</small>
        `;
        fileInput.parentNode.insertBefore(fileInfoDiv, fileInput.nextSibling);

        modal.style.display = 'block';
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

async function deleteMaterial(materialId) {
    try {
        const userIdElement = document.querySelector('input[name="userId"]');
        if (!userIdElement) {
            throw new Error('사용자 정보를 찾을 수 없습니다.');
        }

        const response = await fetch(`/api/study-mate/materials/${materialId}`);
        if (!response.ok) {
            throw new Error('자료 정보를 불러오는데 실패했습니다.');
        }

        const material = await response.json();

        if (String(material.creatorId) !== String(userIdElement.value)) {
            throw new Error('자료를 삭제할 권한이 없습니다.');
        }

        if (!confirm('정말로 이 자료를 삭제하시겠습니까?')) {
            return;
        }

        const deleteResponse = await fetch(`/api/study-mate/materials/${materialId}`, {
            method: 'DELETE'
        });

        if (!deleteResponse.ok) {
            throw new Error('자료 삭제에 실패했습니다.');
        }

        // 삭제 성공 시 열려있는 상세 팝업 찾아서 닫기
        const detailModal = document.querySelector('.material-detail-modal');
        if (detailModal) {
            detailModal.remove();
        }

        alert('자료가 삭제되었습니다.');
        await loadMaterials(); // 목록 새로고침
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

function showMaterialError() {
    const materialContainer = document.querySelector('.right-section .content-box:first-child');
    if (materialContainer) {
        const content = `
            <h2>학습 자료실</h2>
            <div class="material-list">
                <div class="error-message">자료를 불러오는데 실패했습니다.</div>
            </div>
            <button class="action-btn" onclick="openUploadModal()">자료 업로드</button>
        `;
        materialContainer.innerHTML = content;
    }
}

async function loadMaterialById(materialId) {
    try {
        const userIdElement = document.querySelector('input[name="userId"]');
        if (!userIdElement) {
            throw new Error('사용자 정보를 찾을 수 없습니다.');
        }

        const groupId = window.location.pathname.split('/').pop();
        const response = await fetch(`/api/study-mate/materials/${materialId}?userId=${userIdElement.value}&groupId=${groupId}`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || '자료를 불러오는데 실패했습니다.');
        }

        return await response.json();
    } catch (error) {
        console.error('Error loading material:', error);
        throw error;
    }
}

function openMaterialDetail(material) {
    const currentUserId = document.querySelector('input[name="userId"]').value;
    const isCreator = String(currentUserId) === String(material.creatorId);

    const modal = document.createElement('div');
    modal.className = 'material-detail-modal';
    modal.innerHTML = `
        <div class="material-detail-content">
            <span class="material-detail-close">&times;</span>
            <div class="material-detail-header">
                <h3 class="material-detail-title">${escapeHtml(material.title)}</h3>
                <div class="material-detail-meta">
                    작성자: ${material.creatorName || '익명'} | 
                    업로드: ${formatDateTime(material.uploadedAt)}
                </div>
            </div>
            <div class="material-detail-description">
                ${escapeHtml(material.description || '설명이 없습니다.')}
            </div>
            <div class="material-detail-actions">
                <button class="download-btn" onclick="window.location.href='/api/study-mate/materials/download/${material.id}'">
                    <i class="fas fa-download"></i> 다운로드
                </button>
                ${isCreator ? `
                    <button class="edit-btn" onclick="handleEditClick(${material.id}, this)">
                        <i class="fas fa-edit"></i> 수정
                    </button>
                    <button class="delete-btn" onclick="deleteMaterial(${material.id})">
                        <i class="fas fa-trash"></i> 삭제
                    </button>
                ` : ''}
            </div>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'block';

    // 닫기 버튼과 모달 외부 클릭 처리
    const closeBtn = modal.querySelector('.material-detail-close');
    closeBtn.onclick = () => {
        modal.remove();
    };

    modal.onclick = (e) => {
        if (e.target === modal) {
            modal.remove();
        }
    };
}

// updateMaterialList 함수에서 수정/삭제 버튼 제거

function updateMaterialList(materials) {
    const materialContainer = document.querySelector('.right-section .content-box:first-child');
    if (!materialContainer) return;

    const content = `
        <h2>
            학습 자료실
            <button class="action-btn" onclick="openUploadModal()">
                <i class="fas fa-upload"></i> 자료 업로드
            </button>
        </h2>
        <div class="material-list">
            ${!materials || materials.length === 0 ?
        '<div class="no-content">등록된 학습자료가 없습니다.</div>' :
        materials.map(material => `
                <div class="material-item" onclick="openMaterialDetail(${JSON.stringify(material).replace(/"/g, '&quot;')})">
                    <div class="material-info">
                        <h3 class="material-title">${escapeHtml(material.title)}</h3>
                        <p class="material-filename">${escapeHtml(material.fileName || '')}</p>
                    </div>
                </div>
            `).join('')}
        </div>
    `;

    materialContainer.innerHTML = content;
}

// 수정 버튼 클릭 핸들러 추가

async function handleEditClick(materialId, button) {
    // 먼저 상세 모달 찾아서 닫기
    const detailModal = button.closest('.material-detail-modal');
    if (detailModal) {
        detailModal.remove();
    }

    // 수정 모달 열기
    try {
        await editMaterial(materialId);
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

// DOMContentLoaded 이벤트에서 모달 초기화
document.addEventListener('DOMContentLoaded', function() {
    initializeModal();
    loadMaterials();
});



function formatDateTime(dateTime) {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}


