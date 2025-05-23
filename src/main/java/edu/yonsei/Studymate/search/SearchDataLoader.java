package edu.yonsei.Studymate.search;

import edu.yonsei.Studymate.search.entity.SearchEntity;
import edu.yonsei.Studymate.search.repository.SearchRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchDataLoader {

    @Bean("searchDataLoaderRunner")
    public ApplicationRunner searchDataLoader(SearchRepository searchRepository) {
        return args -> {
            saveIfNotExists(searchRepository, "컴퓨터 구조", "박명환");
            saveIfNotExists(searchRepository, "자료구조", "한인찬");
            saveIfNotExists(searchRepository, "알고리즘", "박영희");
            saveIfNotExists(searchRepository, "운영체제", "이세준");
            saveIfNotExists(searchRepository, "데이터베이스", "공지혁");
        };
    }

    private void saveIfNotExists(SearchRepository repo, String name, String professor) {
        boolean exists = repo.findByNameAndProfessor(name, professor).isPresent();
        if (!exists) {
            repo.save(new SearchEntity(name, professor));
        }
    }

}

