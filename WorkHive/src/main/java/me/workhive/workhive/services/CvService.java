package me.workhive.workhive.services;

import me.workhive.workhive.domain.dto.request.CreateCvRequest;
import me.workhive.workhive.domain.dto.request.UpdateCvRequest;
import me.workhive.workhive.domain.dto.response.CvResponse;
import me.workhive.workhive.domain.entities.User;

import java.util.UUID;

public interface CvService {
    CvResponse createCv(UUID userId, CreateCvRequest request);
    CvResponse getCvById(UUID id);
    CvResponse updateCv(UUID  id , UpdateCvRequest request, User user);
    CvResponse deleteCv(UUID id,  User user);
    CvResponse getCvByCandidate(User user);

    }
