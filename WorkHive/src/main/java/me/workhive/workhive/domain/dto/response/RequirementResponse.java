package me.workhive.workhive.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RequirementResponse {

    private UUID id;
    private String name;
}