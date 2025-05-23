package edu.yonsei.Studymate.Studygroup.service;


import edu.yonsei.Studymate.Studygroup.dto.StudygroupRequest;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupRepository;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.common.Pagination;
import edu.yonsei.Studymate.post.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import edu.yonsei.Studymate.subject.entity.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudygroupService {

    private final StudygroupRepository studygroupRepository;
    private final SubjectRepository subjectRepository;


    // study group 을 새로 만들었을 때
    public StudygroupEntity create(
            StudygroupRequest studygroupRequest
    ){
        var subjectEntity = subjectRepository.findById(studygroupRequest.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID"));

        var entity = StudygroupEntity.builder()
                .subjectEntity(subjectEntity)
                .groupName(studygroupRequest.getGroupName())
                .build()
                ;

        return studygroupRepository.save(entity);
    }

    // 모든 스터디 그룹을 게시판으로 정렬
    public Content<List<StudygroupEntity>> articles(Pageable pageable) {
        var list = studygroupRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build()
                ;

        var response = Content.<List<StudygroupEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();

        return response;

    }


}
