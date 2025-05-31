
document.addEventListener('DOMContentLoaded', function() {
    const userId = document.querySelector('input[name="userId"]').value;

    function updateActivity() {
        if (userId) {
            fetch(`/api/study-mate/mate/activity/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).catch(error => console.error('Error updating activity:', error));
        }
    }

    function updateMatesList() {
        if (userId) {
            fetch(`/api/study-mate/studygroups/study-mates?userId=${userId}&limit=4`)
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(mates => {
                    const mateDisplay = document.querySelector('.mate-display');
                    if (!mateDisplay) return;

                    if (!mates || mates.length === 0) {
                        mateDisplay.innerHTML = `
                            <div class="no-mates">
                                <p>아직 활동 중인 스터디 메이트가 없습니다.</p>
                            </div>`;
                        return;
                    }

                    // 각 메이트마다 다른 이미지를 표시하기 위한 이미지 배열
                    const images = [
                        'https://xsgames.co/randomusers/avatar.php?g=male',
                        'https://xsgames.co/randomusers/avatar.php?g=female',
                        'https://xsgames.co/randomusers/avatar.php?g=pixel',
                        'https://xsgames.co/randomusers/avatar.php?g=male&hair=short'
                    ];

                    mateDisplay.innerHTML = mates.map((mate, index) => `
                        <div class="mate-entry">
                            <div class="mate-avatar">
                                <div class="status-indicator offline"></div>
                                <div class="circle" style="background-image: url('${images[index % images.length]}')"></div>
                            </div>
                            <p class="mate-name">${mate.user ? mate.user.name : '알 수 없음'}</p>
                        </div>
                    `).join('');
                })
                .catch(error => console.error('Error updating mates list:', error));
        }
    }

    // 초기 실행 및 주기적 업데이트
    updateActivity();
    updateMatesList();

    // 활동 상태 5초마다 업데이트
    setInterval(updateActivity, 5000);
    // 메이트 리스트 10초마다 업데이트
    setInterval(updateMatesList, 10000);
});