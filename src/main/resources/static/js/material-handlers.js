

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

    try {
        if (isEditMode) {
            const formData = new FormData();
            formData.append('title', titleInput.value.trim());
            formData.append('description', descriptionInput.value.trim());

            // 파일이 선택된 경우에만 추가
            if (fileInput.files[0]) {
                formData.append('file', fileInput.files[0]);
            }

            const response = await fetch(`/api/study-mate/materials/${materialId}`, {
                method: 'PUT',
                body: formData
            });

            if (!response.ok) throw new Error('자료 수정에 실패했습니다.');
            alert('자료가 수정되었습니다.');
        } else {
            if (!fileInput.files[0]) {
                alert('파일을 선택해주세요.');
                return;
            }

            const formData = new FormData();
            formData.append('title', titleInput.value.trim());
            formData.append('description', descriptionInput.value.trim());
            formData.append('file', fileInput.files[0]);
            formData.append('studygroupId', studygroupId);
            formData.append('creatorId', creatorId);

            const response = await fetch('/api/study-mate/materials/upload', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) throw new Error('자료 업로드에 실패했습니다.');
            alert('자료가 성공적으로 업로드되었습니다.');
        }

        closeUploadModal();
        loadMaterials();
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}



function updateMaterialList(materials) {
    const materialContainer = document.querySelector('.right-section .content-box:first-child');
    if (!materialContainer) return;

    const currentUserId = document.querySelector('input[name="userId"]').value;
    console.log('Current User ID:', currentUserId);

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
        materials.map(material => {
            // 디버깅을 위한 로그
            console.log('Material:', material);
            console.log('Material Creator ID:', material.creatorId);

            return `
                    <div class="material-item">
                        <div class="material-info">
                            <div>
                                <h3 class="material-title">${escapeHtml(material.title)}</h3>
                                <p class="material-description">${escapeHtml(material.description || '')}</p>
                                <div class="material-meta">
                                    <span>${formatDateTime(material.uploadedAt)}</span>
                                    <span>•</span>
                                    <span>${material.creatorName || '익명'}</span>
                                </div>
                            </div>
                        </div>
                        <div class="material-actions">
                            <a href="/api/study-mate/materials/download/${material.id}" 
                               class="download-link">
                                <i class="fas fa-download"></i>
                            </a>
                            ${String(currentUserId) === String(material.creatorId) ? `
                                <button class="edit-btn" onclick="editMaterial(${material.id})">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="delete-btn" onclick="deleteMaterial(${material.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            ` : ''}
                        </div>
                    </div>
                `}).join('')}
        </div>
    `;

    materialContainer.innerHTML = content;
}

// 자료 수정 함수 수정
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
        modal.dataset.mode = 'edit';
        modal.dataset.materialId = materialId;
        modal.querySelector('.modal-title').textContent = '자료 수정';

        document.getElementById('fileTitle').value = material.title;
        document.getElementById('fileDescription').value = material.description || '';

        // 파일 입력 필드 표시 (이전에는 숨겼었음)
        const fileInput = document.getElementById('fileUpload');
        if (fileInput) {
            fileInput.required = false; // 파일 업로드를 선택사항으로 변경
        }

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

        alert('자료가 삭제되었습니다.');
        loadMaterials();
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
