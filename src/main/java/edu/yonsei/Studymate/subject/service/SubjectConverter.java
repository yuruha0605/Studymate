package edu.yonsei.Studymate.subject.service;

import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.entity.SubjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SubjectConverter {

    private final StudygroupConverter studygroupConverter;              // subject랑 1-N 관계

    // 받아 온 subject entity를 DTO로 넘겨 주기 위한 함수
    public SubjectDto toDto(SubjectEntity subjectEntity){

        var groupList = subjectEntity.getGroupList()                    // 반환된 List 형태의 study group 을 가져 온다
                .stream()
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());

        return SubjectDto.builder()                                     // Builder 방식을 넘겨 준다.
                .id(subjectEntity.getId())                              // id
                .subjectName(subjectEntity.getSubjectName())            // subjectName
                .professorName(subjectEntity.getProfessorName())        // professorName
                .groupList(groupList)                                   // study group 정보도 함께 담아서 보낸다
                .build()
                ;

    }
}
