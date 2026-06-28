package me.workhive.workhive.domain.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanySelection {

    private UUID companyId;

    private String companyName;
    private String location;
    private String sector;
}