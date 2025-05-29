



function initializeModalHandlers() {
    window.openQuestionModal = () => {
        if (!document.getElementById('boardId').value) {
            loadBoardInfo();
        }
        toggleModal('questionModal', true);
    };
    window.openUploadModal = () => toggleModal('uploadModal', true);
    window.openScheduleModal = () => toggleModal('scheduleModal', true);
    window.openSettingsModal = () => toggleModal('settingsModal', true);

    window.closeQuestionModal = () => toggleModal('questionModal', false);
    window.closeUploadModal = () => toggleModal('uploadModal', false);
    window.closeScheduleModal = () => toggleModal('scheduleModal', false);
    window.closeSettingsModal = () => toggleModal('settingsModal', false);

    window.onclick = (event) => {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    };

    const questionForm = document.getElementById('questionForm');
    if (questionForm) {
        questionForm.addEventListener('submit', handleQuestionSubmit);
    }


}


function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = show ? 'block' : 'none';
    }
}
