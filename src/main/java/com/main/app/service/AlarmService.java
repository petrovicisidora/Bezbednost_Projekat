package com.main.app.service;

import com.main.app.domain.dto.AlarmDTO;
import com.main.app.domain.model.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AlarmService {

    Page<Alarm> getAlarms(Pageable page);
    Alarm add(AlarmDTO alarmDTO);
}
