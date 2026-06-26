package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByCreatedAtBetweenAndRoleIn(
            LocalDateTime from,
            LocalDateTime to,
            List<Role> roles
    );
}
