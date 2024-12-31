package com.photowebsite.repository;

import com.photowebsite.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Page<User> findByUsernameContainingOrEmailContaining(
        String username, 
        String email, 
        Pageable pageable
    );
    Optional<User> findByVerificationToken(String token);
}