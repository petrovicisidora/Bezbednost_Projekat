package com.main.app.controller;

import com.main.app.domain.dto.AlarmDTO;
import com.main.app.domain.dto.Entities;
import com.main.app.domain.dto.UserDTO;
import com.main.app.domain.model.Alarm;
import com.main.app.domain.model.User;
import com.main.app.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {

    private AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping
    public ResponseEntity<Entities> getAlarms(Pageable page) {
        Entities result = new Entities();

        Page<Alarm> alarms = alarmService.getAlarms(page);

        result.setEntities(alarms.getContent());
        result.setTotal(alarms.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(consumes ="application/json",produces = "application/json")
    public ResponseEntity<AlarmDTO> add(@RequestBody AlarmDTO alarmDTO) {

        Alarm savedAlarm = alarmService.add(alarmDTO);
        return new ResponseEntity<>(new AlarmDTO(savedAlarm), HttpStatus.OK);
    }
}
