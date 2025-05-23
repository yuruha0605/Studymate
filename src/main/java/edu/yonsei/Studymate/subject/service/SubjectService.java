package edu.yonsei.Studymate.subject.service;


import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.dto.SubjectRequest;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import edu.yonsei.Studymate.subject.entity.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectConverter subjectConverter;

    // 과목 추가에 대한 서비스
    public SubjectDto create(
            SubjectRequest subjectRequest
    ){
        var entity = SubjectEntity.builder()                        // SubjectEntity를 builder로 담는다.
                .subjectName(subjectRequest.getSubjectName())       // 과목명 받아 오고
                .professorName(subjectRequest.getProfessorName())   // 교수명 받아 온다
                .build()
                ;
        var saveEntity = subjectRepository.save(entity);            // 해당 entity 들을 repository에 저장

        return subjectConverter.toDto(saveEntity);                  // 그리고 그 entity 를 subject converter에 있는 toDto로 넘김
    }

    // 특정 과목 검색에 대한 서비스
    public SubjectDto article(Long id){
        var entity = subjectRepository.findById(id).get();
        return subjectConverter.toDto(entity);
    }

}
