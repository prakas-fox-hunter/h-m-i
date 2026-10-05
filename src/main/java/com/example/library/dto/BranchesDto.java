package com.example.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BranchesDto {
    // private Long id;
    private String name;
    private String provinceName;
    private boolean deleted;
    private boolean active;
}
