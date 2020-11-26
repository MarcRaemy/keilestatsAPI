package ch.keile.keilestatsAPI.controller;


import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import ch.keile.keilestatsAPI.datatemplates.GoalTemplate;
import ch.keile.keilestatsAPI.entities.Goal;
import ch.keile.keilestatsAPI.entities.Player;
import ch.keile.keilestatsAPI.repositories.GoalRepository;
import ch.keile.keilestatsAPI.repositories.PlayerRepository;

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
public class GoalController { // handles calls on goal resources

	@Autowired /*
				 * Marks a constructor, field, setter method, or config method as to be
				 * autowired by Spring's dependency injection facilities.
				 */
	private GoalRepository goalRepository;
	@Autowired
	private PlayerRepository playerRepository;

	// handles GET-Requests on the "/goals" resource
	@GetMapping("/goals")
	public ResponseEntity<Object> getAllGoals() {

		return ResponseEntity.status(HttpStatus.OK).body(goalRepository.findAll());
	}

	@GetMapping("/goals/{goalId}")
	public ResponseEntity<Object> getGoalById(@PathVariable("goalId") Long goalId) {

		Optional<Goal> optionalGoal = goalRepository.findById(goalId);
		if (optionalGoal.isPresent()) {
			return new ResponseEntity<>(optionalGoal.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("goal not found");
		}
	}

	/*
	 * Returning a ResponseEntity with a header containing the URL of the created
	 * resource. @RequestBody indicates that the Body of the Request should be bound
	 * to the Goal object, not some Header Parameters
	 */
	@PostMapping(path = "/goals")
	public ResponseEntity<Object> addGoal(@RequestBody GoalTemplate goalTemplate) {

		Goal goal = new Goal();

		/*
		 * Check whether id's of goal scorers and assistants are valid. Check whether
		 * values are not null. Create goal with one, two or no assist accordingly.
		 * Assure that goal scorer and assistant or assistants are not the same player
		 */
		if (goalTemplate.getGoalScorerId() != null) {

			if (playerRepository.findById(goalTemplate.getGoalScorerId()).isPresent()) {
				Optional<Player> goalScorerOptional = playerRepository.findById(goalTemplate.getGoalScorerId());
				Player goalScorer = goalScorerOptional.get();
				goal.setGoalScorer(goalScorer);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid data: unknown player");
			}

			if (goalTemplate.getFirstAssistantId() != null) {

				if (playerRepository.findById(goalTemplate.getFirstAssistantId()).isPresent()
						&& !goalTemplate.getFirstAssistantId().equals(goalTemplate.getGoalScorerId())) {
					Optional<Player> firstAssistantOptional = playerRepository
							.findById(goalTemplate.getFirstAssistantId());
					Player firstAssistant = firstAssistantOptional.get();
					goal.setFirstAssistant(firstAssistant);
				}

				else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("invalid data: unknown or identical players provided");
				}

				if (goalTemplate.getSecondAssistantId() != null) {

					if (playerRepository.findById(goalTemplate.getSecondAssistantId()).isPresent()
							&& !(goalTemplate.getSecondAssistantId().equals(goalTemplate.getFirstAssistantId())
									|| goalTemplate.getSecondAssistantId().equals(goalTemplate.getGoalScorerId()))) {
						Optional<Player> secondAssistantOptional = playerRepository
								.findById(goalTemplate.getSecondAssistantId());
						Player secondAssistant = secondAssistantOptional.get();
						goal.setSecondAssistant(secondAssistant);
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("invalid data: identical or unknown player id's");
					}
				}
			} else {
				if (goalTemplate.getSecondAssistantId() != null) {

					if (playerRepository.findById(goalTemplate.getSecondAssistantId()).isPresent()
							&& !goalTemplate.getSecondAssistantId().equals(goalTemplate.getGoalScorerId())) {

						Optional<Player> secondAssistantOptional = playerRepository
								.findById(goalTemplate.getSecondAssistantId());
						Player secondAssistant = secondAssistantOptional.get();
						goal.setSecondAssistant(secondAssistant);
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("invalid data: identical or unknown player id's");
					}
				}
			}
			Goal savedGoal = goalRepository.save(goal);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{goalId}")
					.buildAndExpand(savedGoal.getGoalId()).toUri();
			
			return ResponseEntity.created(location).body(savedGoal);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("goal scorer missing");
		}
	}

	@PutMapping("/goals/{goalId}")
	public ResponseEntity<Object> updateGoal(@RequestBody GoalTemplate goalTemplate,
			@PathVariable("goalId") Long goalId) {

		/*
		 * In addition to the checks in the post method, assure that updated goal scorer
		 * or assistant is not identical with value in the existing resource
		 */
		Goal goal;
		if (goalRepository.findById(goalId).isPresent())
			goal = goalRepository.findById(goalId).get();
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("goal to update not found");

		Long scorerId = goalTemplate.getGoalScorerId();
		Long firstAssistantId = goalTemplate.getFirstAssistantId();
		Long secondAssistantId = goalTemplate.getSecondAssistantId();

		// handle different cases (none, one, two or three null values), validation of
		// values
		if (scorerId == null && firstAssistantId == null && secondAssistantId == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no values to update provided");

		else if (scorerId != null && firstAssistantId != null && secondAssistantId != null) {
			if (scorerId.equals(firstAssistantId) || scorerId.equals(secondAssistantId)
					|| firstAssistantId.equals(secondAssistantId)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("assistant and goal scorer or first and second assistant cannot be the same player");
			}

			if (!playerRepository.findById(scorerId).isPresent()
					|| !playerRepository.findById(firstAssistantId).isPresent()
					|| !playerRepository.findById(secondAssistantId).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("invalid playerId provided: player does not exist");
			} else {
				goal.setGoalScorer(playerRepository.findById(scorerId).get());
				goal.setFirstAssistant(playerRepository.findById(firstAssistantId).get());
				goal.setSecondAssistant(playerRepository.findById(secondAssistantId).get());
				goalRepository.save(goal);
				return ResponseEntity.status(HttpStatus.OK).body(goal);
			}
		} else if (scorerId == null) {
			if (firstAssistantId == null) {
				if (playerRepository.findById(secondAssistantId).isPresent()) {
					goal.setSecondAssistant(playerRepository.findById(secondAssistantId).get());
					goalRepository.save(goal);
					return ResponseEntity.status(HttpStatus.OK).body(goal);
				} else
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("invalid playerId provided: player with id " + secondAssistantId + " does not exist");
			} else {
				if (secondAssistantId == null) {
					if (playerRepository.findById(firstAssistantId).isPresent()) {
						if (!goal.getSecondAssistant().getPlayerId().equals(firstAssistantId)
								&& !goal.getGoalScorer().getPlayerId().equals(firstAssistantId)) {
							goal.setFirstAssistant(playerRepository.findById(firstAssistantId).get());
							goalRepository.save(goal);
							return ResponseEntity.status(HttpStatus.OK).body(goal);
						} else
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body("two assistants or assistant and goal scorer cannot be the same player");
					} else
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								"invalid playerId provided: player with id " + firstAssistantId + " does not exist");
				} else {
					if (!playerRepository.findById(firstAssistantId).isPresent()
							|| !playerRepository.findById(secondAssistantId).isPresent()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid playerId provided");
					}
					if (!firstAssistantId.equals(secondAssistantId)
							&& !(goal.getGoalScorer().getPlayerId().equals(firstAssistantId)
									|| goal.getGoalScorer().getPlayerId().equals(secondAssistantId))) {
						goal.setFirstAssistant(playerRepository.findById(firstAssistantId).get());
						goal.setSecondAssistant(playerRepository.findById(secondAssistantId).get());
						goalRepository.save(goal);
						return ResponseEntity.status(HttpStatus.OK).body(goal);
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								"first and second assistant or assistant and goal scorer cannot be the same player");
					}
				}
			}
		} else { // meaning if scorerId != null
			if (firstAssistantId == null && secondAssistantId == null) {
				if (!playerRepository.findById(scorerId).isPresent())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("invalid playerId provided player with id " + scorerId + " does not exist");
				else {
					if (!(goal.getFirstAssistant().getPlayerId().equals(scorerId)
							|| goal.getSecondAssistant().getPlayerId().equals(scorerId))) {
						goal.setGoalScorer(playerRepository.findById(scorerId).get());
						goalRepository.save(goal);
						return ResponseEntity.status(HttpStatus.OK).body(goal);
					} else
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								"first and second assistant or assitant and goal scorer cannot be the same player");
				}
			} else {
				if (firstAssistantId == null) {
					if (!playerRepository.findById(scorerId).isPresent()
							|| !playerRepository.findById(secondAssistantId).isPresent()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid playerId provided");
					} else {
						if (!(secondAssistantId.equals(scorerId)
								|| goal.getFirstAssistant().getPlayerId().equals(secondAssistantId))) {
							goal.setSecondAssistant(playerRepository.findById(secondAssistantId).get());
							goal.setGoalScorer(playerRepository.findById(scorerId).get());
							goalRepository.save(goal);
							return ResponseEntity.status(HttpStatus.OK).body(goal);
						} else {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body("goal scorer and assistant or two assistants cannot be the same player");
						}
					}
				} else {// second assistant == null
					if (!playerRepository.findById(scorerId).isPresent()
							|| !playerRepository.findById(firstAssistantId).isPresent()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid playerId provided");
					} else {
						if (!(firstAssistantId.equals(scorerId)
								|| scorerId.equals(goal.getSecondAssistant().getPlayerId())
								|| firstAssistantId.equals(goal.getSecondAssistant().getPlayerId()))) {
							goal.setFirstAssistant(playerRepository.findById(firstAssistantId).get());
							goal.setGoalScorer(playerRepository.findById(scorerId).get());
							goalRepository.save(goal);
							return ResponseEntity.status(HttpStatus.OK).body(goal);
						} else {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body("goal scorer and assistant cannot be the same player");
						}
					}
				}
			}
		}
	}

	@DeleteMapping("/goals/{goalId}")
	public ResponseEntity<Object> deleteGoal(@PathVariable("goalId") Long goalId) {
		try {
			goalRepository.deleteById(goalId);
			return ResponseEntity.status(HttpStatus.OK).body("goal with id " + goalId + " deleted");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("goal not found");
		}
	}
}
