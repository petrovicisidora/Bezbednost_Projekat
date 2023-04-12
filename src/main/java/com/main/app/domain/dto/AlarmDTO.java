package com.main.app.domain.dto;

import com.main.app.domain.model.Alarm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AlarmDTO {

    private Long id;
    private String date;
    private String alarm;
    private String agent;

    public AlarmDTO(Alarm alarm) {
        this.id = alarm.getId();
        this.date = alarm.getDate();
        this.alarm = alarm.getAlarm();
        this.agent = alarm.getAgent();
    }
}
