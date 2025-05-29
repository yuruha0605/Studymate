

async function loadMaterials() {
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    try {
        const response = await fetch(`/api/study-mate/materials/studygroup/${studygroupId}`);
        if (!response.ok) throw new Error('자료를 불러오는데 실패했습니다.');
        const materials = await response.json();
        updateMaterialList(materials);
    } catch (error) {
        console.error('Error:', error);
        showMaterialError();
    }
}


async function handleUploadSubmit(event) {
    event.preventDefault();

    const titleInput = document.getElementById('fileTitle');
    const descriptionInput = document.getElementById('fileDescription');
    const fileInput = document.getElementById('fileUpload');
    const pathParts = window.location.pathname.split('/');
    const studygroupId = pathParts[pathParts.length - 1];

    if (!titleInput.value.trim() || !fileInput.files[0]) {
        alert('제목과 파일은 필수 입력 항목입니다.');
        return;
    }

    const formData = new FormData();
    formData.append('title', titleInput.value.trim());
    formData.append('description', descriptionInput.value.trim());
    formData.append('file', fileInput.files[0]);
    formData.append('studygroupId', studygroupId);

    try {
        const response = await fetch('/api/study-mate/materials/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Network response was not ok');
        }

        alert('자료가 성공적으로 업로드되었습니다.');
        closeUploadModal();
        loadMaterials(); // 자료 목록 새로고침
        event.target.reset();
    } catch (error) {
        console.error('Error:', error);
        alert('자료 업로드에 실패했습니다: ' + error.message);
    }
}


function updateMaterialList(materials) {
    const materialContainer = document.querySelector('.right-section .content-box:first-child');
    if (!materialContainer) {
        console.error('Material container not found');
        return;
    }

    // 기존 내용 유지를 위한 HTML 구조
    const content = `
        <h2>학습 자료실</h2>
        <div class="material-list">
            ${!materials || materials.length === 0 ?
        '<div class="no-content"><p>등록된 학습자료가 없습니다.</p></div>' :
        materials.map(material => `
                    <div class="material-item">
                        <h3>${escapeHtml(material.title)}</h3>
                        ${material.description ?
            `<p class="material-description">${escapeHtml(material.description)}</p>` :
            ''}
                        <div class="material-info">
                            <span>${formatDate(material.uploadDateTime)}</span>
                            <a href="/api/study-mate/materials/download/${material.id}" 
                               class="download-link">
                                <i class="fas fa-download"></i> 다운로드
                            </a>
                        </div>
                    </div>
                `).join('')
    }
        </div>
        <button class="action-btn" onclick="openUploadModal()">자료 업로드</button>
    `;

    materialContainer.innerHTML = content;
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