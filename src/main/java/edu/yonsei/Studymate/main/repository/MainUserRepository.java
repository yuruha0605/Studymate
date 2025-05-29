package edu.yonsei.Studymate.main.repository;

import edu.yonsei.Studymate.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainUserRepository extends JpaRepository<User, Long> {
    User findByLoginId(String loginId); // ✅ loginId로 찾기
}

