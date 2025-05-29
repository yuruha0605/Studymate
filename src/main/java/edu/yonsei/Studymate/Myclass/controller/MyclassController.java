package edu.yonsei.Studymate.Myclass.controller;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.service.MyclassService;
import edu.yonsei.Studymate.Studygroup.dto.StudygroupDto;
import edu.yonsei.Studymate.common.ApiUrls;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiUrls.Myclass.BASE)
@RequiredArgsConstructor
public class MyclassController {
    private final MyclassService myclassService;

    @GetMapping(ApiUrls.Myclass.LIST)
    public List<MyclassEntity> getAllClasses() {
        return myclassService.getAllClasses();
    }

    @PostMapping(ApiUrls.Myclass.JOIN)
    public MyclassEntity joinClass(@PathVariable Long classId) {
        return myclassService.joinClass(classId);
    }

    @GetMapping(ApiUrls.Myclass.USER_CLASSES)
    public Map<String, List<StudygroupDto>> getUserClasses(@PathVariable Long userId) {
        return myclassService.getUserClasses(userId);
    }

}
