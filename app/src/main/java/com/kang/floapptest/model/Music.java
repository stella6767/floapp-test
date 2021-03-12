package com.kang.floapptest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Music {
    private Integer id;
    private String name;
    private String artist;
    private String url;
}
