package edu.yonsei.Studymate.reply.service;

import edu.yonsei.Studymate.post.entity.PostRepository;
import edu.yonsei.Studymate.reply.dto.ReplyDto;
import edu.yonsei.Studymate.reply.dto.ReplyRequest;
import edu.yonsei.Studymate.reply.entity.ReplyEntity;
import edu.yonsei.Studymate.reply.entity.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final ReplyConverter replyConverter;


    public ReplyDto create(
            ReplyRequest replyRequest
    ){
        var entity = postRepository.findById(replyRequest.getPostId()).get();

        var saveEntity = ReplyEntity.builder()
                .postEntity(entity)
                .content(replyRequest.getContent())
                .build()
                ;

        var savedEntity = replyRepository.save(saveEntity);

        return replyConverter.toDto(savedEntity);
    }
}
