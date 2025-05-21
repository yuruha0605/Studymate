package edu.yonsei.Studymate.Myclass.service;

import edu.yonsei.Studymate.Myclass.entity.MateEntity;
import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.entity.StudentEntity;
import edu.yonsei.Studymate.Myclass.repository.MateRepository;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import edu.yonsei.Studymate.Myclass.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateService {

    private final MateRepository mateRepository;
    private final MyclassRepository classRepository;
    private final StudentRepository studentRepository;

    public List<MateEntity> getMates(Long classId) {
        MyclassEntity myclass = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("해당 수업이 없습니다."));
        return mateRepository.findByMyclass(myclass);
    }

    public void joinClass(Long classId, String studentName) {
        MyclassEntity myclass = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("수업 없음"));

        if (myclass.getCurrentParticipants() >= myclass.getMaxParticipants()) {
            throw new IllegalStateException("정원 초과");
        }

        StudentEntity student = studentRepository.findByName(studentName)
                .orElseGet(() -> studentRepository.save(StudentEntity.builder().name(studentName).build()));

        // 이미 참여했는지 체크
        boolean alreadyJoined = mateRepository.findByMyclass(myclass).stream()
                .anyMatch(m -> m.getStudent().getName().equals(studentName));
        if (alreadyJoined) return;

        myclass.setCurrentParticipants(myclass.getCurrentParticipants() + 1);
        classRepository.save(myclass);

        mateRepository.save(
                MateEntity.builder()
                        .myclass(myclass)
                        .student(student)
                        .build()
        );
    }
}
