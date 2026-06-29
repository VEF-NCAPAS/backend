package me.workhive.workhive.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateScoreResponse {

    private UUID cvId;
    private UUID candidateProfileId;
    private UUID applicationId;
    private String name;
    private String email;
    private String location;
    private String city;
    private double score;
}