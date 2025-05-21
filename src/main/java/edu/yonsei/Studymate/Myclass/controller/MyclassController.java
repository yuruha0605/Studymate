package edu.yonsei.Studymate.Myclass.controller;

import edu.yonsei.Studymate.Myclass.entity.MyclassEntity;
import edu.yonsei.Studymate.Myclass.service.MyclassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myclass")
@RequiredArgsConstructor
public class MyclassController {

    private final MyclassService service;

    @GetMapping
    public List<MyclassEntity> getAll() {
        return service.getAllClasses();
    }

    @PutMapping("/join/{id}")
    public ResponseEntity<MyclassEntity> join(@PathVariable Long id) {
        return ResponseEntity.ok(service.joinClass(id));
    }
}
