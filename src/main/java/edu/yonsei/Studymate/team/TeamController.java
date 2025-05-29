package edu.yonsei.Studymate.team;


import edu.yonsei.Studymate.team.dto.TeamDto;
import edu.yonsei.Studymate.team.entity.TeamEntity;
import edu.yonsei.Studymate.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("yonsei")
public class TeamController {

    private final TeamService teamService;

    @PostMapping(path = "/team/{name}")
    public TeamEntity func1 (
            @PathVariable String name,
            @RequestBody TeamDto teamDto
    ){
        return teamService.postTeam(teamDto);
    }



}
