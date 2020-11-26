package ch.keile.keilestatsAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.keile.keilestatsAPI.entities.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

}