package ch.keile.keilestatsAPI.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.keile.keilestatsAPI.entities.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
