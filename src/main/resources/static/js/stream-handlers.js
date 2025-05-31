
let localStream = null;
let isAudioMuted = true;  // 초기 상태: 음소거
let isVideoOff = true;    // 초기 상태: 비디오 꺼짐
let isScreenSharing = false;

async function startLocalStream() {
    try {
        localStream = await navigator.mediaDevices.getUserMedia({
            video: true,
            audio: true
        });

        const videoElement = document.getElementById('localVideo');
        videoElement.srcObject = localStream;
        videoElement.style.display = 'block';

        // 초기 상태 설정
        localStream.getAudioTracks().forEach(track => track.enabled = false);
        localStream.getVideoTracks().forEach(track => track.enabled = false);

        // 초기 버튼 상태 설정
        const muteButton = document.getElementById('muteButton');
        const videoButton = document.getElementById('videoButton');

        muteButton.innerHTML = '<i class="fas fa-microphone-slash"></i>';
        videoButton.innerHTML = '<i class="fas fa-video-slash"></i>';
    } catch (error) {
        console.error('미디어 스트림 시작 실패:', error);
        alert('카메라나 마이크 접근에 실패했습니다.');
    }
}

function initializeStreamHandlers() {
    const muteButton = document.getElementById('muteButton');
    const videoButton = document.getElementById('videoButton');
    const screenShareButton = document.getElementById('screenShareButton');

    muteButton.addEventListener('click', toggleAudio);
    videoButton.addEventListener('click', toggleVideo);
    screenShareButton.addEventListener('click', toggleScreenShare);

    startLocalStream();
}

function toggleAudio() {
    if (!localStream) return;

    const audioTracks = localStream.getAudioTracks();
    const muteButton = document.getElementById('muteButton');

    if (audioTracks.length > 0) {
        isAudioMuted = !isAudioMuted;
        audioTracks[0].enabled = !isAudioMuted;
        muteButton.innerHTML = isAudioMuted ?
            '<i class="fas fa-microphone-slash"></i>' :
            '<i class="fas fa-microphone"></i>';
    }
}

function toggleVideo() {
    if (!localStream) return;

    const videoTracks = localStream.getVideoTracks();
    const videoButton = document.getElementById('videoButton');

    if (videoTracks.length > 0) {
        isVideoOff = !isVideoOff;
        videoTracks[0].enabled = !isVideoOff;
        videoButton.innerHTML = isVideoOff ?
            '<i class="fas fa-video-slash"></i>' :
            '<i class="fas fa-video"></i>';
    }
}

async function toggleScreenShare() {
    const videoElement = document.getElementById('localVideo');
    const screenShareButton = document.getElementById('screenShareButton');

    if (isScreenSharing) {
        if (localStream) {
            localStream.getTracks().forEach(track => track.stop());
        }
        await startLocalStream();
        isScreenSharing = false;
        screenShareButton.innerHTML = '<i class="fas fa-desktop"></i>';
    } else {
        try {
            const screenStream = await navigator.mediaDevices.getDisplayMedia({
                video: true,
                audio: true
            });

            if (localStream) {
                localStream.getTracks().forEach(track => track.stop());
            }

            localStream = screenStream;
            videoElement.srcObject = screenStream;
            isScreenSharing = true;
            screenShareButton.innerHTML = '<i class="fas fa-camera"></i>';

            screenStream.getVideoTracks()[0].onended = async () => {
                await startLocalStream();
                isScreenSharing = false;
                screenShareButton.innerHTML = '<i class="fas fa-desktop"></i>';
            };
        } catch (error) {
            console.error('화면 공유 실패:', error);
            alert('화면 공유를 시작할 수 없습니다.');
        }
    }
}