package me.workhive.workhive.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEducationRequest {
    private String institution;

    private String major;

    //private String description;//quitar
}
