package me.workhive.workhive.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalTestResponse {
    private UUID id;
    private String link;
    private LocalDateTime deadline;
}
