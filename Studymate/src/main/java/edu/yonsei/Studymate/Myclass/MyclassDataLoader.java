package edu.yonsei.Studymate.Myclass;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.repository.MyclassRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyclassDataLoader {

    @Bean
    public ApplicationRunner loadSampleData(MyclassRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(MyclassEntity.builder().name("데이터베이스").maxParticipants(50).currentParticipants(12).build());
                repo.save(MyclassEntity.builder().name("알고리즘 기초").maxParticipants(40).currentParticipants(8).build());
                repo.save(MyclassEntity.builder().name("운영 체제").maxParticipants(50).currentParticipants(20).build());
                repo.save(MyclassEntity.builder().name("데이터 마이닝").maxParticipants(30).currentParticipants(11).build());
            }
        };
    }
}
