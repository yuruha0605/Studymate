// mate-activity.js

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

    // 초기 실행 및 5초마다 실행
    updateActivity();
    setInterval(updateActivity, 5000);
});



function updateMatesList(userId) {
    fetch(`/api/study-mate/studygroups/study-mates?userId=${userId}&limit=4`)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(mates => {
            const mateDisplay = document.querySelector('.mate-display');
            if (mateDisplay && mates) {
                mateDisplay.innerHTML = mates.map(mate => `
                    <div class="mate-entry">
                        <div class="mate-avatar">
                            <div class="status-indicator ${mate.online ? 'online' : 'offline'}"></div>
                            <div class="circle"></div>
                        </div>
                        <p>${mate.user.name}</p>
                    </div>
                `).join('');
            }
        })
        .catch(error => console.error('Error updating mates list:', error));
}


function isOnline(lastActiveTime) {
    if (!lastActiveTime) return false;
    const lastActive = new Date(lastActiveTime);
    const fiveMinutesAgo = new Date(Date.now() - 5 * 60 * 1000);
    return lastActive > fiveMinutesAgo;
}
