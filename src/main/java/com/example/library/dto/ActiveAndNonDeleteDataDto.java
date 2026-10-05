package com.example.library.dto;

public interface ActiveAndNonDeleteDataDto {
     Long getId();
    Boolean getActiveBranch();
    Boolean getDeletedBranch();
    String getNameBranch();
    Boolean getActiveStore();
    Boolean getDeletedStore();
    String getNameStore();
    Boolean getActiveProvinces();
    Boolean getDeletedProvinces();
    String getNameProvinces();
}
