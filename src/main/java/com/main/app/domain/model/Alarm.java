package com.main.app.domain.model;

import com.main.app.domain.dto.AlarmDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@Setter

public class Alarm extends AbstractEntity {

    private String date;
    private String alarm;
    private String agent;

    public Alarm(String date, String alarm, String agent) {
        this.date = date;
        this.alarm = alarm;
        this.agent = agent;
    }

    public Alarm(AlarmDTO alarm) {
        this.id = alarm.getId();
        this.date = alarm.getDate();
        this.alarm = alarm.getAlarm();
        this.agent = alarm.getAgent();
    }
}
