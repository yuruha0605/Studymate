package edu.yonsei.Studymate.login.service;

import edu.yonsei.Studymate.login.dto.AuthResponse;
import edu.yonsei.Studymate.login.dto.LoginRequest;
import edu.yonsei.Studymate.login.dto.SignupRequest;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .name(request.getName())
                .studentId(request.getStudentId())
                .major(request.getMajor())
                .selectedLearningStyles(request.getSelectedLearningStyles())
                .studyTime(request.getStudyTime())
                .selectedInterests(request.getSelectedInterests())
                .skillLevel(request.getSkillLevel())
                .build();

        userRepository.save(user);
    }


    public boolean isEmailDuplicate(String email) {
        return userRepository.findByLoginId(email).isPresent();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return AuthResponse.builder()
                .username(user.getLoginId())
                .message("로그인 성공")
                .build();
    }


    public String findIdByStudentIdAndName(String studentId, String name) {
        return userRepository.findByStudentIdAndName(studentId, name)
                .map(User::getLoginId)
                .orElse(null);
    }

    public String findPasswordByLoginIdAndStudentId(String loginId, String studentId) {
        return userRepository.findByLoginIdAndStudentId(loginId, studentId)
                .map(User::getPassword)
                .orElse(null);
    }



}

