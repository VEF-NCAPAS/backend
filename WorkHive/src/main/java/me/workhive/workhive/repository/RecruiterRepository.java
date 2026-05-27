package me.workhive.workhive.repository;

import me.workhive.workhive.model.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterProfile, UUID> {
}
