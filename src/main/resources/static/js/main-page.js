async function joinStudyGroup(groupId, currentMembers, maxMembers) {
    if (currentMembers >= maxMembers) {
        alert('이 스터디 그룹은 이미 인원이 가득 찼습니다.');
        return;
    }

    const confirmed = confirm('이 스터디 그룹에 입장하시겠습니까?');
    if (!confirmed) return;

    const userIdElement = document.querySelector('input[name="userId"]');
    if (!userIdElement) {
        alert('로그인이 필요한 기능입니다.');
        return;
    }

    try {
        // URL 수정
        const response = await fetch(`/api/study-mate/studygroups/${groupId}/join?userId=${userIdElement.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || '입장에 실패했습니다.');
        }

        alert('스터디 그룹에 성공적으로 참여했습니다.');
        window.location.href = `/study-mate/studyroom/${groupId}`;
    } catch (error) {
        console.error('Error:', error);
        alert(error.message || '스터디 그룹 참여 중 오류가 발생했습니다.');
    }
}