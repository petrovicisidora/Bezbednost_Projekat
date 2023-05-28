package com.main.app.service;

import com.main.app.domain.dto.SkillDto;
import com.main.app.domain.model.Skill;
import com.main.app.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService{

    @Autowired
    SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill addSkill(SkillDto skillDto) {
        Skill skill = new Skill();
        skill.setName(skillDto.getName());
        skill.setLevel(skillDto.getLevel());

        return skillRepository.save(skill);
    }


}
