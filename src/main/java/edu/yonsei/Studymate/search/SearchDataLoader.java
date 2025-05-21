package edu.yonsei.Studymate.search;

import edu.yonsei.Studymate.search.entity.SubjectEntity;
import edu.yonsei.Studymate.search.repository.SubjectRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchDataLoader {

    @Bean("searchDataLoaderRunner")
    public ApplicationRunner searchDataLoader(SubjectRepository subjectRepository) {
        return args -> {
            saveIfNotExists(subjectRepository, "컴퓨터 구조", "박명환");
            saveIfNotExists(subjectRepository, "자료구조", "한인찬");
            saveIfNotExists(subjectRepository, "알고리즘", "박영희");
            saveIfNotExists(subjectRepository, "운영체제", "이세준");
            saveIfNotExists(subjectRepository, "데이터베이스", "공지혁");
        };
    }

    private void saveIfNotExists(SubjectRepository repo, String name, String professor) {
        boolean exists = repo.findByNameAndProfessor(name, professor).isPresent();
        if (!exists) {
            repo.save(new SubjectEntity(name, professor));
        }
    }

}

