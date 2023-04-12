package com.main.app.service;

import com.main.app.domain.dto.AlarmDTO;
import com.main.app.domain.model.Alarm;
import com.main.app.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl implements AlarmService {

    private AlarmRepository alarmRepository;

    @Autowired
    public AlarmServiceImpl(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public Page<Alarm> getAlarms(Pageable page) {
        return alarmRepository.findAll(page);
    }

    @Override
    public Alarm add(AlarmDTO alarmDTO) {

        Alarm alarm = new Alarm(alarmDTO);

        return alarmRepository.save(alarm);
    }
}
