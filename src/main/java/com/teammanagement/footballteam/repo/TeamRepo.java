package com.teammanagement.footballteam.repo;

import com.teammanagement.footballteam.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {
}
