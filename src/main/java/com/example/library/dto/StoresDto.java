package com.example.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoresDto {
    
    private String name;
    private boolean active;
    private boolean deleted;
    private boolean whitelisted;
    private String branchName;
}
