package me.workhive.workhive.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.workhive.workhive.common.mappers.CvMapper;
import me.workhive.workhive.common.mappers.EducationMapper;
import me.workhive.workhive.common.mappers.ExperienceMapper;
import me.workhive.workhive.common.mappers.LanguageMapper;
import me.workhive.workhive.domain.dto.request.*;
import me.workhive.workhive.domain.dto.response.CvResponse;
import me.workhive.workhive.domain.entities.*;
import me.workhive.workhive.exceptions.BusinessRuleException;
import me.workhive.workhive.exceptions.DeniedAccessException;
import me.workhive.workhive.exceptions.ResourceNotFoundException;
import me.workhive.workhive.repositories.*;
import me.workhive.workhive.services.CvService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CvServiceImpl implements CvService {
    private final CvRepository cvRepository;

    private final CandidateRepository candidateRepository;

    private final UserRepository userRepository;

    private final CvMapper cvMapper;

    private final ExperienceMapper experienceMapper;
    private final EducationMapper educationMapper;
    private final LanguageMapper languageMapper;
    private final SkillRepository skillRepository;
    private final LanguageRepository languageRepository;

    @Override
    @Transactional
    public CvResponse createCv(UUID userId, CreateCvRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        CandidateProfile candidateProfile =
                candidateRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"
                                )
                        );

        if (cvRepository.existsByCandidateProfile(candidateProfile)) {

            throw new BusinessRuleException(
                    "Candidate already has a CV"
            );
        }

        validateExperiences(request.getExperiences());

        List<Experience> experiences = request.getExperiences()
                .stream()
                .map(experienceMapper::toEntityCreate)
                .toList();

        List<Education> educations = request.getEducation()
                .stream()
                .map(educationMapper::toEntityCreate)
                .toList();

        List<CvLanguage> languages = request.getLanguages()
                .stream()
                .map(languageRequest -> {
                    Language language = languageRepository.findById(languageRequest.getId()
                    ).orElseThrow(() -> new ResourceNotFoundException("Language not found"));

                    return languageMapper.toCvLanguage(languageRequest, language
                    );
                })
                .toList();

        List<UUID> skillIds = request.getSkills()
                .stream()
                .map(SkillSelection::getId)
                .toList();

        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more skills do not exist"
            );
        }

        Cv cv = cvMapper.toCvCreate(
                request,
                candidateProfile,
                experiences,
                educations,
                languages,
                skills);

        educations.forEach(e -> e.setCv(cv));
        experiences.forEach(e -> e.setCv(cv));
        languages.forEach(l-> l.setCv(cv));

        cv.setEducation(educations);
        cv.setExperiences(experiences);
        cv.setCvLanguages(languages);

        Cv savedCv = cvRepository.save(cv);

        return cvMapper.toCvDto(savedCv);
    }

    @Override
    public CvResponse getCvByCandidate(User user) {

        CandidateProfile candidate = candidateRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        Cv cv = cvRepository
                .findByCandidateProfile(candidate)
                .orElseThrow(() -> new ResourceNotFoundException("CV not found"));
        return cvMapper.toCvDto(cv);
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

        List<Experience> experiences =
                request.getExperiences()
                        .stream()
                        .map(experienceMapper::toEntityCreate)
                        .toList();

        List<Education> educations =
                request.getEducation()
                        .stream()
                        .map(educationMapper::toEntityCreate)
                        .toList();

        List<CvLanguage> languages =
                request.getLanguages()
                        .stream()
                        .map(languageRequest -> {
                            Language language = languageRepository
                                    .findById(languageRequest.getId())
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "Language not found"
                                            ));

                            return languageMapper.toCvLanguage(
                                    languageRequest,
                                    language
                            );
                        })
                        .toList();

        List<UUID> skillIds = request.getSkills()
                .stream()
                .map(SkillSelection::getId)
                .toList();


        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more skills do not exist"
            );
        }

        educations.forEach(e -> e.setCv(existingCv));
        experiences.forEach(e -> e.setCv(existingCv));
        languages.forEach(l -> l.setCv(existingCv));

        cvMapper.toCvUpdate(existingCv, request, languages, educations, skills, experiences);

        Cv savedCv = cvRepository.save(existingCv);

        return cvMapper.toCvDto(savedCv);
    }

    @Override
    @Transactional
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

    private void validateExperiences(List<CreateExperienceRequest> experiences) {

        experiences.forEach(exp -> {

            if (exp.getHireDate().isAfter(exp.getEndDate())) {

                throw new BusinessRuleException(
                        "Hire date cannot be after end date"
                );
            }
        });
    }
}
