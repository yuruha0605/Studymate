package edu.yonsei.Studymate;


import edu.yonsei.Studymate.dto.TeamDto;
import edu.yonsei.Studymate.entity.TeamEntity;
import edu.yonsei.Studymate.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("yonsei")
public class CrudController {

    private final TeamService teamService;

    @PostMapping(path = "/team/{name}")
    public TeamEntity func1 (
            @PathVariable String name,
            @RequestBody TeamDto teamDto
    ){
        return teamService.postTeam(teamDto);
    }



}
