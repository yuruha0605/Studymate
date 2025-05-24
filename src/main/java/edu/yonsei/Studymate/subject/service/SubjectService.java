package edu.yonsei.Studymate.subject.service;


import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.dto.SubjectRequest;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import edu.yonsei.Studymate.subject.entity.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectConverter subjectConverter;

    // 과목 추가에 대한 서비스
    public SubjectDto create(
            SubjectRequest subjectRequest
    ){

        // 중복 과목 체크
        if (subjectRepository.existsBySubjectName(subjectRequest.getSubjectName())) {
            throw new RuntimeException("Subject already exists: " + subjectRequest.getSubjectName());
        }


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
        return subjectRepository.findById(id)
                .map(subjectConverter::toDto)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));

    }

    public List<SubjectDto> searchBySubjectName(String keyword) {
        var entities = subjectRepository.findBySubjectNameContainingIgnoreCase(keyword);
        return entities.stream()
                .map(subjectConverter::toDto)
                .collect(Collectors.toList());
    }


    // 교수명으로 과목 검색
    public List<SubjectDto> searchByProfessor(String keyword) {
        var entities = subjectRepository.findByProfessorNameContainingIgnoreCase(keyword);
        return entities.stream()
                .map(subjectConverter::toDto)
                .collect(Collectors.toList());
    }

    // 전체 과목 목록 조회
    public List<SubjectDto> getAllSubjects() {
        var entities = subjectRepository.findAll();
        return entities.stream()
                .map(subjectConverter::toDto)
                .collect(Collectors.toList());
    }





}
