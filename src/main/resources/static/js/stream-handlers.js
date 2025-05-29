

function initializeStreamHandlers() {
    const streamButton = document.querySelector('.stream-button');
    if (streamButton) {
        streamButton.addEventListener('click', startStream);
    }
}


// 스트리밍 시작
async function startStream() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({
            video: true,
            audio: false
        });

        const videoElement = document.getElementById('localVideo');
        videoElement.srcObject = stream;
        videoElement.style.display = 'block';
    } catch (error) {
        console.error('스트림 시작 실패:', error);
        alert('카메라 접근에 실패했습니다.');
    }
}

// 스트리밍 종료
function stopStream() {
    const videoElement = document.getElementById('localVideo');
    const stream = videoElement.srcObject;
    if (!stream) return;

    stream.getTracks().forEach(track => track.stop());
    videoElement.srcObject = null;
    videoElement.style.display = 'none';
}