package com.main.app.controller;

import com.main.app.domain.dto.SkillDto;
import com.main.app.domain.model.Skill;
import com.main.app.service.ProjectService;
import com.main.app.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skill")
public class SkillController {

    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto) {
        Skill skill = skillService.addSkill(skillDto);
        SkillDto createdSkillDto = convertToDto(skill);
        createdSkillDto.setId(skill.getId()); // Dodavanje ID kreiranog skilla
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkillDto);
    }

    private SkillDto convertToDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setName(skill.getName());
        skillDto.setLevel(skill.getLevel());
        return skillDto;
    }
}
