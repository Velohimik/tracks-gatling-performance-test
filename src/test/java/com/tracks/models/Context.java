package com.tracks.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Context {

    private Integer id;
    private LocalDateTime createAt;
    private String name;
    private Integer position;
    private String state;
    private LocalDateTime updateAt;
    private String userId;
}
