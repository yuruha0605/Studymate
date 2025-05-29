package edu.yonsei.Studymate.reply.service;

import edu.yonsei.Studymate.reply.dto.ReplyDto;
import edu.yonsei.Studymate.reply.entity.ReplyEntity;
import org.springframework.stereotype.Service;

@Service
public class ReplyConverter {

    public ReplyDto toDto(ReplyEntity replyEntity){
        return ReplyDto.builder()
                .id(replyEntity.getId())
                .content(replyEntity.getContent())
                .build()
                ;
    }
}