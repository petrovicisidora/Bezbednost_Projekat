package com.main.app.service;

import com.main.app.domain.dto.SkillDto;
import com.main.app.domain.model.Skill;

public interface SkillService {

    Skill addSkill(SkillDto skillDto);

}
