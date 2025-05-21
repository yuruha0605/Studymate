package edu.yonsei.Studymate.Myclass.service;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyclassService {

    private final MyclassRepository repository;

    public List<MyclassEntity> getAllClasses() {
        return repository.findAll();
    }

    public MyclassEntity joinClass(Long id) {
        MyclassEntity cls = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다: " + id));
        if (cls.getCurrentParticipants() < cls.getMaxParticipants()) {
            cls.setCurrentParticipants(cls.getCurrentParticipants() + 1);
            return repository.save(cls);
        } else {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }
    }
}
