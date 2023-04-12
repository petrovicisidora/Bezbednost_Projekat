package com.main.app.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Entities<T> {

    private List<T> entities = new ArrayList<T>();
    private long total;
}
