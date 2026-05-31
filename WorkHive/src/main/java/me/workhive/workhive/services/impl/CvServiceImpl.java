package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.CvMapper;
import me.workhive.workhive.common.mappers.EducationMapper;
import me.workhive.workhive.common.mappers.ExperienceMapper;
import me.workhive.workhive.domain.dto.request.CreateCvRequest;
import me.workhive.workhive.domain.dto.request.SkillSelection;
import me.workhive.workhive.domain.dto.request.UpdateCvRequest;
import me.workhive.workhive.domain.dto.response.CvResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.CandidateRepository;
import me.workhive.workhive.repositories.CvRepository;
import me.workhive.workhive.repositories.SkillRepository;
import me.workhive.workhive.repositories.UserRepository;
import me.workhive.workhive.services.CvService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CvServiceImpl implements CvService {
    private final CvRepository cvRepository;

    private final CandidateRepository candidateRepository;

    private final UserRepository userRepository;

    private final CvMapper cvMapper;

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;

    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public CvResponse createCv(UUID userId, CreateCvRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        CandidateProfile candidateProfile =
                candidateRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate profile not found"
                                )
                        );

        if (cvRepository.existsByCandidateProfile(candidateProfile)) {

            throw new RuntimeException(
                    "Candidate already has a CV"
            );
        }

        List<UUID> skillIds = request.getSkills()
                .stream()
                .map(SkillSelection::getId)
                .toList();

        List<Skill> skills = skillRepository.findAllById(skillIds);

        List<Experience> experiences = request.getExperiences()
                .stream()
                .map(experienceMapper::toEntityCreate)
                .toList();

        List<Education> educations = request.getEducation()
                .stream()
                .map(educationMapper::toEntityCreate)
                .toList();

        Cv cv = cvMapper.toCvCreate(request, candidateProfile, experiences, educations, skills);

        educations.forEach(e -> e.setCv(cv));
        experiences.forEach(e -> e.setCv(cv));

        cv.setEducation(educations);
        cv.setExperiences(experiences);

        Cv savedCv = cvRepository.save(cv);

        return cvMapper.toCvDto(savedCv);
    }

    @Override
    public CvResponse getCvById(UUID id){
        return cvMapper.toCvDto(
                this.findCvById(id)
        );
    }

    @Override
    @Transactional
    public CvResponse updateCv(UUID id, UpdateCvRequest request, User user) {

        Cv existingCv = findCvById(id);

        CandidateProfile candidate = this.findCandidate(user);

        if (!existingCv.getCandidateProfile().getId()
                .equals(candidate.getId())) {
            throw new DeniedAccessException("You cannot edit another candidate's cv");
        }
        List<UUID> skillIds = request.getSkills()
                .stream()
                .map(SkillSelection::getId)
                .toList();

        List<Skill> skills =
                skillRepository.findAllById(skillIds);

        List<Experience> experiences =
                request.getExperiences()
                        .stream()
                        .map(experienceMapper::toEntityCreate)
                        .collect(Collectors.toList());

        List<Education> educations =
                request.getEducation()
                        .stream()
                        .map(educationMapper::toEntityCreate)
                        .collect(Collectors.toList());

        educations.forEach(e -> e.setCv(existingCv));
        experiences.forEach(e -> e.setCv(existingCv));

        existingCv.getExperiences().clear();
        existingCv.getExperiences().addAll(experiences);

        existingCv.getEducation().clear();
        existingCv.getEducation().addAll(educations);

        existingCv.setSkills(new ArrayList<>(skills));


        Cv savedCv = cvRepository.save(existingCv);

        return cvMapper.toCvDto(savedCv);
    }

    @Override
    public CvResponse deleteCv(UUID id,  User user){
        CvResponse existsCv = this.getCvById(id);
        CandidateProfile candidate = this.findCandidate(user);

        if (!existsCv.getCandidateProfileId()
                .equals(candidate.getId())) {
            throw new DeniedAccessException(
                    "You cannot delete cvs from another candidate"
            );
        }
        cvRepository.deleteById(id);
        return existsCv;
    }

    public Cv findCvById(UUID id){
        return cvRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cv not found")
                );
    }

    public CandidateProfile findCandidate(User user){
        return candidateRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
    }
}
