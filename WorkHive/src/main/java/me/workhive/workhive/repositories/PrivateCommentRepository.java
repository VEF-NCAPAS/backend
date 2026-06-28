package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.Application;
import me.workhive.workhive.domain.entities.PrivateComment;
import me.workhive.workhive.domain.entities.RecruiterProfile;
import me.workhive.workhive.domain.entities.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateCommentRepository extends JpaRepository<PrivateComment, UUID> {

    Page<PrivateComment> findByApplication(
            Application application,
            Pageable pageable
    );

    Page<PrivateComment> findByApplicationAndStatusAtCreation(
            Application application,
            ApplicationStatus statusAtCreation,
            Pageable pageable
    );

}
