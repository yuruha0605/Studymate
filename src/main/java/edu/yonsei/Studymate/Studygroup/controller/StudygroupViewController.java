package edu.yonsei.Studymate.Studygroup.controller;

import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.Studygroup.entity.GroupMember;
import edu.yonsei.Studymate.Studygroup.entity.GroupMemberRepository;
import edu.yonsei.Studymate.Studygroup.entity.StudygroupEntity;
import edu.yonsei.Studymate.Studygroup.service.StudygroupConverter;
import edu.yonsei.Studymate.Studygroup.service.StudygroupService;
import edu.yonsei.Studymate.board.dto.BoardDto;
import edu.yonsei.Studymate.board.service.BoardService;
import edu.yonsei.Studymate.common.ApiUrls;
import edu.yonsei.Studymate.common.Content;
import edu.yonsei.Studymate.login.entity.User;
import edu.yonsei.Studymate.login.repository.UserRepository;
import edu.yonsei.Studymate.login.security.CustomUserDetails;
import edu.yonsei.Studymate.subject.dto.SubjectDto;
import edu.yonsei.Studymate.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
// ... 다른 import문

@Controller
@RequiredArgsConstructor
@RequestMapping(ApiUrls.View.BASE)
public class StudygroupViewController {
    private final StudygroupService studygroupService;
    private final StudygroupConverter studygroupConverter;
    private final SubjectService subjectService;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final BoardService boardService;

    @GetMapping(ApiUrls.View.STUDY_ROOM)
    public String studyroom(@PathVariable Long groupId, Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        StudygroupEntity group = studygroupService.getStudyGroup(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 멤버십 확인
        Optional<GroupMember> memberOpt = groupMemberRepository.findByGroupAndUser(group, user);

        if (memberOpt.isEmpty()) {
            // 멤버가 아닌 경우 메인 페이지로 리다이렉트
            return "redirect:/?error=unauthorized";
        }

        GroupMember member = memberOpt.get();
        StudygroupDto groupDto = studygroupConverter.toDto(group, user);
        groupDto.setMemberRole(member.getRole().name());

        // 스터디그룹의 게시판과 게시글 정보 가져오기
        Content<BoardDto> boardContent = boardService.getBoardWithPosts(groupId, 0, 10);

        model.addAttribute("group", groupDto);
        model.addAttribute("board", boardContent.getBody());

        return "studyroom";
    }



    // 마이 클래스 페이지
    @GetMapping(ApiUrls.View.MY_CLASS)
    public String myClassPage(Model model, @RequestParam Long userId) {
        Map<String, List<StudygroupDto>> userGroups = studygroupService.getUserStudyGroups(userId);
        model.addAttribute("myGroups", userGroups.get("myGroups"));
        model.addAttribute("joinedGroups", userGroups.get("joinedGroups"));

        List<SubjectDto> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);

        return "myclass";
    }

    // 스터디 그룹 목록 페이지
    @GetMapping(ApiUrls.View.STUDY_GROUPS)
    public String listStudyGroups(@RequestParam int page,
                                  @RequestParam int size,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));
        Content<List<StudygroupEntity>> content = studygroupService.articles(pageable);

        List<StudygroupDto> dtoList = content.getBody().stream()
                .map(studygroupConverter::toDto)
                .collect(Collectors.toList());

        var dtoContent = Content.<List<StudygroupDto>>builder()
                .body(dtoList)
                .pagination(content.getPagination())
                .build();

        model.addAttribute("boardArticle", dtoContent);
        return "board";
    }
}