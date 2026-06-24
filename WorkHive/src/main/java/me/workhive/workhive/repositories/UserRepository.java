package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u.gender, COUNT(u) " +
           "FROM User u " +
           "WHERE u.role = me.workhive.workhive.domain.entities.enums.Role.CANDIDATE " +
           "OR u.role = me.workhive.workhive.domain.entities.enums.Role.RECRUITER " +
           "GROUP BY u.gender")
    List<Object[]> countAllUsersByGender();
}
