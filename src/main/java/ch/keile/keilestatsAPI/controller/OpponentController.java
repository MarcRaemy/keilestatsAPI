package ch.keile.keilestatsAPI.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import ch.keile.keilestatsAPI.datatemplates.OpponentTemplate;
import ch.keile.keilestatsAPI.entities.Opponent;
import ch.keile.keilestatsAPI.repositories.OpponentRepository;

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
public class OpponentController {

	@Autowired
	/*
	 * Marks a constructor, field, setter method, or config method as to be
	 * autowired by Spring's dependency injection facilities.
	 */
	private OpponentRepository opponentRepository;

	@GetMapping("/opponents")
	public ResponseEntity<Object> getAllOpponents() {

		return ResponseEntity.status(HttpStatus.OK).body(opponentRepository.findAll());
	}

	@GetMapping("/opponents/{opponentId}")
	public ResponseEntity<Object> getOpponentById(@PathVariable("opponentId") Long opponentId) {

		if (!opponentRepository.findById(opponentId).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Opponent with id " + opponentId + " found");
		}

		else {
			return new ResponseEntity<>(opponentRepository.findById(opponentId).get(), HttpStatus.OK);
		}
	}

	@DeleteMapping("/opponents/{opponentId}")
	public ResponseEntity<Object> deleteOpponent(@PathVariable("opponentId") Long opponentId) {

		Optional<Opponent> opponentOptional = opponentRepository.findById(opponentId);
		if (opponentOptional.isPresent()) {
			// checks, if opponent is already saved in a game. if yes. opponent shall not be
			// deleted.
			if (opponentOptional.get().getGames().isEmpty()) {
				opponentRepository.deleteById(opponentId);
				return ResponseEntity.status(HttpStatus.OK).body("Opponent deleted");
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"Opponent with id " + opponentId + " has a non-empty list of games and therfore cannot be deleted");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opponent with id " + opponentId + " not found");
	}

	@PostMapping(path = "/opponents", consumes = { MediaType.APPLICATION_JSON_VALUE, "application/json" })
	public ResponseEntity<Object> addOpponent(@Valid @RequestBody OpponentTemplate opponentTemplate) {

		if (opponentTemplate.getOpponentName() != null && !opponentTemplate.getOpponentName().isEmpty()) {

			String opponentName = opponentTemplate.getOpponentName();

			Optional<Opponent> opponentOptional = opponentRepository.findByOpponentName(opponentName);

			if (!opponentOptional.isPresent()) {
				Opponent opponent = new Opponent(opponentName);
				Opponent savedOpponent = opponentRepository.save(opponent);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{opponentId}")
						.buildAndExpand(savedOpponent.getOpponentId()).toUri();

				return ResponseEntity.status(HttpStatus.CREATED).location(location).body(savedOpponent);

			}

			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opponent already exists");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No opponent name provided");
	}

	@PutMapping(path = "/opponents/{opponentId}")
	public ResponseEntity<Object> updateOpponent(@RequestBody OpponentTemplate opponentTemplate,
			@PathVariable("opponentId") Long opponentId) {

		Optional<Opponent> opponentOptional = opponentRepository.findById(opponentId);
		if (!opponentOptional.isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opponent with id " + opponentId + " not found");

		if (opponentTemplate.getOpponentName() != null)

			opponentOptional.get().setOpponentName(opponentTemplate.getOpponentName());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.buildAndExpand(opponentOptional.get().getOpponentId()).toUri();

		opponentRepository.save(opponentOptional.get());
		return ResponseEntity.status(HttpStatus.OK).location(location).body(opponentOptional.get());
	}
}
