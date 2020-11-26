package ch.keile.keilestatsAPI.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ch.keile.keilestatsAPI.datatemplates.PlayerTemplate;
import ch.keile.keilestatsAPI.datatemplates.ScoringDataTemplate;
import ch.keile.keilestatsAPI.entities.*;
import ch.keile.keilestatsAPI.repositories.*;

@RestController
/*
 * A convenience annotation that is itself annotated with @Controller
 * and @ResponseBody. @Controller is a specification of @Component to indicate
 *  to the framework that the container should instantiate the class as a bean
 */
@RequestMapping("/keilestats")
/*
 * Annotation for mapping web requests onto methods in request-handling classes
 * with flexible method signatures
 */
public class PlayerController {

	@Autowired /* Annotation to tell the framework to inject this dependency */
	private PlayerRepository playerRepository;

	// Return list of all players
	@GetMapping("/players")
	public ResponseEntity<Object> getAllPlayers() {

		return ResponseEntity.status(HttpStatus.OK).body(playerRepository.findAll());
	}

	@DeleteMapping("/players/{playerId}")
	public ResponseEntity<Object> deletePlayer(@PathVariable("playerId") Long playerId) {

		if (playerRepository.findById(playerId).isPresent()) {

			Player playerToBeDeleted = playerRepository.findById(playerId).get();

			if (playerToBeDeleted.getGames().isEmpty()) {
				playerRepository.deleteById(playerId);
				return new ResponseEntity<>("Player deleted..", HttpStatus.OK);
			} else
				return new ResponseEntity<>("Player with id: " + playerId + " has a non-empty List of games "
						+ "and therefore cannot be deleted", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Player with id " + playerId + " not found", HttpStatus.BAD_REQUEST);
	}

	// Return values of one Player
	@GetMapping("/players/{playerId}")
	public ResponseEntity<Object> getPlayerById(@PathVariable("playerId") Long playerId) {

		Optional<Player> playerOptional = playerRepository.findById(playerId);

		if (playerOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(playerOptional.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("player with id " + playerId + " not found");
		}
	}

	// Returning a ResponseEntity with a header containing the URL of the created
	// resource
	@PostMapping(path = "/players")
	public ResponseEntity<Object> addPlayer(@RequestBody PlayerTemplate playerTemplate) {

		Player player = new Player();

		player.setFirstname(playerTemplate.getFirstname());
		player.setLastname(playerTemplate.getLastname());
		player.setAddress(playerTemplate.getAddress());
		player.setPosition(playerTemplate.getPosition());
		player.setEmail(playerTemplate.getEmail());
		player.setPhone(playerTemplate.getPhone());

		if (playerRepository.findAll().contains(player)) {
			return new ResponseEntity<>(
					"Player with name \"" + player.getFirstname() + " " + player.getLastname() + "\" already exists",
					HttpStatus.BAD_REQUEST);

		}

		Player savedPlayer = playerRepository.save(player);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{playerId}")
				.buildAndExpand(savedPlayer.getPlayerId()).toUri();

		return ResponseEntity.created(location).body(savedPlayer);
	}

	@PutMapping("/players/{playerId}")
	public ResponseEntity<Object> updatePlayer(@RequestBody PlayerTemplate playerTemplate,
			@PathVariable("playerId") Long playerId) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("status1", "new player created or player " + playerId + " updated");

		// Check whether playerId exists, update player, or else create new one
		return new ResponseEntity<Object>(playerRepository.findById(playerId).map(player -> {

			if (playerTemplate.getAddress() != null)
				player.setAddress(playerTemplate.getAddress());
			if (playerTemplate.getEmail() != null) {
				if (playerTemplate.getEmail() != null)
					player.setEmail(playerTemplate.getEmail());
			}
			if (playerTemplate.getFirstname() != null)
				player.setFirstname(playerTemplate.getFirstname());
			if (playerTemplate.getLastname() != null)
				player.setLastname(playerTemplate.getLastname());
			if (playerTemplate.getPhone() != null)
				player.setPhone(playerTemplate.getPhone());
			if (playerTemplate.getPosition() != null)
				player.setEmail(playerTemplate.getPosition());
			return playerRepository.save(player);
		}).orElseGet(() -> {
			Player player = new Player();
			if (playerTemplate.getAddress() != null)
				player.setAddress(playerTemplate.getAddress());
			if (playerTemplate.getEmail() != null)
				player.setEmail(playerTemplate.getEmail());
			if (playerTemplate.getFirstname() != null)
				player.setFirstname(playerTemplate.getFirstname());
			if (playerTemplate.getLastname() != null)
				player.setLastname(playerTemplate.getLastname());
			if (playerTemplate.getPhone() != null)
				player.setPhone(playerTemplate.getPhone());
			if (playerTemplate.getPosition() != null)
				player.setPosition(playerTemplate.getPosition());
			if (playerId != null)
				player.setPlayerId(playerId);
			return playerRepository.save(player);
		}), headers, HttpStatus.OK);
	}

	// Return goals where player is goalScorer (or number of Goals)
	@GetMapping("/palyers/{player_id}/goals")
	public ResponseEntity<Object> getGoalsByPlayerId(@PathVariable("player_id") Long playerId) {

		int result = 0;

		Optional<Player> optionalPlayer = playerRepository.findById(playerId);

		if (optionalPlayer.isPresent()) {
			result = optionalPlayer.get().getGoalsScored().size();
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player with id " + playerId + " not found");
		}
	}

	// Return goals, where player did the first assist
	@GetMapping("/palyers/{player_id}/assists")
	public ResponseEntity<Object> getPlayerAssistsByPlayerId(@PathVariable("player_id") Long playerId) {

		int result = 0;

		Optional<Player> optionalPlayer = playerRepository.findById(playerId);
		if (optionalPlayer.isPresent()) {
			result = optionalPlayer.get().getFirstAssists().size() + optionalPlayer.get().getSecondAssists().size();
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player with id " + playerId + " not found");
		}

	}

	// Return number of games that a player played
	@GetMapping("/palyers/{player_id}/games")
	public ResponseEntity<Object> getGamesByPlayerId(@PathVariable("player_id") Long playerId) {

		int result = 0;

		Optional<Player> optionalPlayer = playerRepository.findById(playerId);

		if (optionalPlayer.isPresent()) {
			result = optionalPlayer.get().getGames().size();
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player with id " + playerId + " not found");
		}
	}

	// Return complete scoringtable
	@GetMapping("/palyers/scoringtable")
	public ResponseEntity<Object> getScoringdataForEachPlayer() {

		List<ScoringDataTemplate> scoringtable = new ArrayList<>();

		List<Player> allPlayers = playerRepository.findAll();

		for (int i = 0; i < allPlayers.size(); i++) {

			ScoringDataTemplate entry = new ScoringDataTemplate();
			Player player = allPlayers.get(i);

			entry.setAssistsScored(player.getFirstAssists().size() + player.getSecondAssists().size());
			entry.setPlayerFirstName(player.getFirstname());
			entry.setGamesPlayed(player.getGames().size());
			entry.setGoalsScored(player.getGoalsScored().size());
			entry.setPlayerLastName(player.getLastname());
			entry.setPlayerId(player.getPlayerId());
			entry.setTotalPoints(entry.getAssistsScored() + entry.getGoalsScored());

			scoringtable.add(entry);
		}

		return ResponseEntity.status(HttpStatus.OK).body(scoringtable);
	}
}
