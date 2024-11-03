package com.teammanagement.footballteam.repo;

import com.teammanagement.footballteam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
