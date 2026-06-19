package me.workhive.workhive.repositories;

import me.workhive.workhive.domain.entities.User;
import me.workhive.workhive.domain.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);

    @Query("""
            SELECT u FROM User u
            WHERE (:role IS NULL OR u.role = :role)
            AND (
                :search IS NULL OR :search = '' OR
                LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    Page<User> findAllForAdmin(
            @Param("role") Role role,
            @Param("search") String search,
            Pageable pageable
    );

    List<User> findByCreatedAtBetweenAndRoleIn(
            LocalDateTime from,
            LocalDateTime to,
            List<Role> roles
    );
}