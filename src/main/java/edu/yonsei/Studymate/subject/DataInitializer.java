package edu.yonsei.Studymate.subject;

import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import edu.yonsei.Studymate.subject.entity.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    @Override
    public void run(String... args) {
        if (subjectRepository.count() == 0) {
            subjectRepository.saveAll(List.of(
                    SubjectEntity.builder()
                            .subjectName("자료구조")
                            .professorName("김교수")
                            .build(),
                    SubjectEntity.builder()
                            .subjectName("알고리즘")
                            .professorName("이교수")
                            .build()
            ));
        }
    }
}
