package edu.yonsei.Studymate.reply.entity;

import edu.yonsei.Studymate.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

    List<ReplyEntity> findAllByPostEntityOrderById(PostEntity postEntity);
    void deleteAllByPostEntity(PostEntity postEntity);

}
