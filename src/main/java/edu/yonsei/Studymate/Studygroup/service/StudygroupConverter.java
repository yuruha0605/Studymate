package edu.yonsei.Studymate.Studygroup.service;

import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.board.service.BoardConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudygroupConverter {

    public StudygroupDto toDto(StudygroupEntity studygroupEntity){
        return StudygroupDto.builder()
                .id(studygroupEntity.getId())
                .subjectId(studygroupEntity.getSubjectEntity().getId())
                .groupName(studygroupEntity.getGroupName())
                .subjectName(studygroupEntity.getSubjectEntity().getSubjectName())
                .currentMembers(studygroupEntity.getCurrentMembers())
                .maxMembers(studygroupEntity.getMaxMembers())
                .status(studygroupEntity.getStatus().name())
                .build();


    }


}
