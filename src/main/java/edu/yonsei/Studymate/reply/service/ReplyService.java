package edu.yonsei.Studymate.reply.service;

import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.post.entity.PostEntity;
import edu.yonsei.Studymate.post.entity.PostRepository;
import edu.yonsei.Studymate.reply.dto.ReplyDto;
import edu.yonsei.Studymate.reply.dto.ReplyRequest;
import edu.yonsei.Studymate.reply.entity.ReplyEntity;
import edu.yonsei.Studymate.reply.entity.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final ReplyConverter replyConverter;


    public ReplyDto create(ReplyRequest replyRequest, User currentUser) {
        var postEntity = postRepository.findById(replyRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        var saveEntity = ReplyEntity.builder()
                .postEntity(postEntity)
                .content(replyRequest.getContent())
                .user(currentUser)  // 현재 사용자 정보 저장
                .build();

        var savedEntity = replyRepository.save(saveEntity);
        return replyConverter.toDto(savedEntity);
    }


    public List<ReplyEntity> findAllByPostEntityOrderById(PostEntity postEntity) {
        return replyRepository.findAllByPostEntityOrderById(postEntity);
    }


    public void deleteReply(Long replyId, User user) {
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own replies");
        }

        replyRepository.delete(reply);
    }

    public ReplyDto updateReply(Long replyId, ReplyRequest replyRequest, User user) {
        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own replies");
        }

        reply.setContent(replyRequest.getContent());
        ReplyEntity updatedReply = replyRepository.save(reply);
        return replyConverter.toDto(updatedReply);
    }

}
