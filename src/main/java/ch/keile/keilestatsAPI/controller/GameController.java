package ch.keile.keilestatsAPI.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import ch.keile.keilestatsAPI.datatemplates.GameTemplate;
import ch.keile.keilestatsAPI.datatemplates.GoalTemplate;
import ch.keile.keilestatsAPI.entities.Game;
import ch.keile.keilestatsAPI.entities.Goal;
import ch.keile.keilestatsAPI.entities.Opponent;
import ch.keile.keilestatsAPI.entities.Player;
import ch.keile.keilestatsAPI.repositories.GameRepository;
import ch.keile.keilestatsAPI.repositories.GoalRepository;
import ch.keile.keilestatsAPI.repositories.OpponentRepository;
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
public class GameController { // handles calls on game resources

	@Autowired /*
				 * Marks a constructor, field, setter method, or config method as to be
				 * autowired by Spring's dependency injection facilities.
				 */
	private GameRepository gameRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private OpponentRepository opponentRepository;
	@Autowired
	private GoalRepository goalRepository;

	// Annotation for mapping HTTP GET requests onto specific handler methods.
	@GetMapping("/games") // URI on which the get request is called.
	public ResponseEntity<Object> getAllGames() {

		return ResponseEntity.status(HttpStatus.OK).body(gameRepository.findAll());
	}

	// "ResponseEntity" used to return HTTP-StatusCodes and custom messages in the
	// response body
	@GetMapping("/games/{gameId}")
	public ResponseEntity<Object> getGameById(@PathVariable("gameId") Long gameId) {

		// check if game with id gameId is present in the database and return it or
		// return error message
		Optional<Game> optionalGame = gameRepository.findById(gameId);
		if (optionalGame.isPresent()) {
			return new ResponseEntity<>(optionalGame.get(), HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No game with id " + gameId + " found");

	}

	@DeleteMapping("/games/{gameId}")
	public ResponseEntity<Object> deleteGame(@PathVariable("gameId") Long gameId) {

		if (gameRepository.findById(gameId).isPresent()) {
			gameRepository.deleteById(gameId);
			return ResponseEntity.status(HttpStatus.OK).body("game with id " + gameId + " deleted");
		}

		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No game with id " + gameId + " found");
		}
	}

	/*
	 * Returning a ResponseEntity with a header containing the URL of the created
	 * resource. @RequestBody indicates that the Body of the Request should be bound
	 * to the Goal object, not some Header Parameters
	 */
	@PostMapping(path = "/games", consumes = { MediaType.APPLICATION_JSON_VALUE, "application/json" })
	public ResponseEntity<Object> addGame(@RequestBody GameTemplate gameTemplate) {

		Game game = new Game();
		gameRepository.save(game);

		// Handle Opponent: if Opponent with the given Name present in Database, take
		// existing. Else create new Opponent.

		if (gameTemplate.getOpponentName() != null) {

			String opponentName = gameTemplate.getOpponentName();
			// (for debugging purposes:)
			System.out.println(opponentName);

			Optional<Opponent> opponentOptional = opponentRepository.findByOpponentName(opponentName);
			if (opponentOptional.isPresent()) {

				game.setOpponent(opponentOptional.get());
			} else {
				Opponent opponent = new Opponent(opponentName);
				game.setOpponent(opponentRepository.save(opponent));

			}
		}
		// handle Game Date
		if (!gameTemplate.getGameDate().isEmpty()) {
			game.setGameDate(gameTemplate.getGameDate());
		}
		// handle Number of Goals Opponent
		if (gameTemplate.getNbGoalsOpponent() != null) {
			Integer nbGoalsOpponent = new Integer(gameTemplate.getNbGoalsOpponent());
			System.out.println(nbGoalsOpponent);
			game.setNbGoalsOpponent(nbGoalsOpponent);
		}
		// handle Number of Goals Keile
		if (gameTemplate.getNbGoalsKeile() != null) {
			Integer nbGoalsKeile = new Integer(gameTemplate.getNbGoalsKeile());
			System.out.println(nbGoalsKeile);
			game.setNbGoalsKeile(nbGoalsKeile);
		}

		// handle list of playerId's representing players that participated in the game.
		Long[] idsOfPlayersAtGame = gameTemplate.getPlayerIdList();
		List<Player> playersAtGame = new ArrayList<>();

		if (idsOfPlayersAtGame != null) {

			for (int i = 0; i < idsOfPlayersAtGame.length; i++) {

				// test whether player exists in Database, if yes, load and save it to List of
				// players of the game
				Optional<Player> player = playerRepository.findById(idsOfPlayersAtGame[i]);

				if (player.isPresent()) {
					playersAtGame.add(player.get());
				} else {
					return ResponseEntity.badRequest().body("player with id " + idsOfPlayersAtGame[i] + " not found");
				}
			}
			game.setPlayers(playersAtGame);
		}

		// handle list of goals: check for each goal in a for-loop, whether it has a
		// scorer and zero or one or two
		// assistants. Create goal accordingly, save it to game. rule out that scorer
		// and assistant
		// or two assistants can be the same player

		List<GoalTemplate> goalTemplateList = gameTemplate.getGoalsList();
		List<Goal> goalsAtGame = new ArrayList<>();

		if (goalTemplateList != null) {
			for (int i = 0; i < goalTemplateList.size(); i++) {

				GoalTemplate currentGoalTemplate = goalTemplateList.get(i);
				Long scorerId = currentGoalTemplate.getGoalScorerId();

				if (scorerId != null) {

					Optional<Player> goalScorerOptional = playerRepository.findById(scorerId);

					if (goalScorerOptional.isPresent()) {

						Long firstAssistantId = currentGoalTemplate.getFirstAssistantId();

						if (firstAssistantId != null) {

							if (firstAssistantId.equals(scorerId)) {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body("Goalscorer and first Assistant cannot be the same player");
							}

							Optional<Player> firstAssistantOptional = playerRepository.findById(firstAssistantId);

							if (firstAssistantOptional.isPresent()) {

								Long secondAssistantId = currentGoalTemplate.getSecondAssistantId();

								if (secondAssistantId != null) {
									if (secondAssistantId.equals(firstAssistantId)
											|| secondAssistantId.equals(scorerId)) {
										return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
												"Second Assistant cannot be the same player as first Assistant or Goalscorer");
									}

									Optional<Player> secondAssistantOptional = playerRepository
											.findById(secondAssistantId);

									if (secondAssistantOptional.isPresent()) {

										Player secondAssistant = secondAssistantOptional.get();
										Player firstAssistant = firstAssistantOptional.get();
										Player goalScorer = goalScorerOptional.get();

										Goal goal = new Goal(goalScorer, firstAssistant, secondAssistant, game);
										goalRepository.save(goal);
										goalsAtGame.add(goal);

										if (!playersAtGame.contains(goalScorer)) {
											playersAtGame.add(goalScorer);
										}
										if (!playersAtGame.contains(firstAssistant)) {
											playersAtGame.add(firstAssistant);
										}
										if (!playersAtGame.contains(secondAssistant)) {
											playersAtGame.add(secondAssistant);
										}
										game.setPlayers(playersAtGame);
									}

									else {
										return ResponseEntity.status(HttpStatus.BAD_REQUEST)
												.body("Player with id " + secondAssistantId + " not found");
									}
								}

								else {
									Player firstAssistant = firstAssistantOptional.get();
									Player goalScorer = goalScorerOptional.get();

									Goal goal = new Goal(goalScorer, firstAssistant, game);
									goalRepository.save(goal);
									goalsAtGame.add(goal);

									if (!playersAtGame.contains(goalScorer)) {
										playersAtGame.add(goalScorer);
									}
									if (!playersAtGame.contains(firstAssistant)) {
										playersAtGame.add(firstAssistant);
									}
									game.setPlayers(playersAtGame);
								}
							} else {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body("Game contains Goal with invalid first Assistant, Player with id "
												+ firstAssistantId + " not found");
							}
						}

						else {
							if (currentGoalTemplate.getSecondAssistantId() != null) {

								Long secondAssistantId = currentGoalTemplate.getSecondAssistantId();
								Optional<Player> secondAssistantOptional = playerRepository.findById(secondAssistantId);

								if (secondAssistantOptional.isPresent()) {

									Player secondAssistant = secondAssistantOptional.get();
									Player goalScorer = goalScorerOptional.get();

									Goal goal = new Goal(goalScorer, secondAssistant, game);
									goalRepository.save(goal);
									goalsAtGame.add(goal);

									if (!playersAtGame.contains(goalScorer)) {
										playersAtGame.add(goalScorer);
									}
									if (!playersAtGame.contains(secondAssistant)) {
										playersAtGame.add(secondAssistant);
									}
									game.setPlayers(playersAtGame);
								}

								else {
									return ResponseEntity.status(HttpStatus.BAD_REQUEST)
											.body("Game contains Goal with invalid second Assistant, Player with id "
													+ secondAssistantId + " not found");
								}
							}

							else {
								Player goalScorer = goalScorerOptional.get();
								Goal goal = new Goal(goalScorer, game);
								goalRepository.save(goal);
								goalsAtGame.add(goal);

								if (!playersAtGame.contains(goalScorer)) {
									playersAtGame.add(goalScorer);
								}
								game.setPlayers(playersAtGame);

							}
						}
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								"Game contains goal with invalid scorer. Player with id " + scorerId + " not found");
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game contains goal with missing scorer");
				}
			}
			game.setGoalsKeile(goalsAtGame);
		}

		// to return a ResponseEntity with a header containing the URI of the created
		// resource
		Game savedGame = gameRepository.save(game);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameId}")
				.buildAndExpand(savedGame.getGameId()).toUri();
		return ResponseEntity.created(location).body(game);

	}

	@PutMapping("/games/{gameId}")
	public ResponseEntity<Object> updateGame(@RequestBody GameTemplate gameTemplate,
			@PathVariable("gameId") Long gameId) {

		Optional<Game> gameOptional = gameRepository.findById(gameId);

		if (!gameOptional.isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid game id");

		Game game = gameOptional.get();
		//in order to update List of goals and to cascade changes
		for(int i = 0; i < game.getGoalsKeile().size(); i++) {
			System.out.println(game.getGoalsKeile().get(i).getGoalId());
			goalRepository.delete(game.getGoalsKeile().get(i));
		}
		

		// Handle opponent. update opponent if new value provided
		if (gameTemplate.getOpponentName() != null) {

			String opponentName = gameTemplate.getOpponentName();
			Optional<Opponent> opponentOptional = opponentRepository.findByOpponentName(opponentName);
			if (opponentOptional.isPresent()) {

				game.setOpponent(opponentOptional.get());
			} else {
				Opponent opponent = new Opponent(opponentName);
				game.setOpponent(opponentRepository.save(opponent));
			}
		}
		// handle game date
		if (!gameTemplate.getGameDate().isEmpty()) {
			game.setGameDate(gameTemplate.getGameDate());
		}
		// handle number of goals of the opponent
		if (gameTemplate.getNbGoalsOpponent() != null) {
			Integer nbGoalsOpponent = gameTemplate.getNbGoalsOpponent();
			game.setNbGoalsOpponent(nbGoalsOpponent);
		}
		// handle Number of goals of HC Keile
		if (gameTemplate.getNbGoalsKeile() != null) {
			Integer nbGoalsKeile = gameTemplate.getNbGoalsKeile();
			game.setNbGoalsKeile(nbGoalsKeile);
		}

		// handle List of playerId's representing players that participated in the game
		Long[] idsOfPlayersAtGame = gameTemplate.getPlayerIdList();
		List<Player> playersAtGame = new ArrayList<>();

		if (idsOfPlayersAtGame != null) {

			for (int i = 0; i < idsOfPlayersAtGame.length; i++) {

				// test whether player exists in Database, if yes, load and save it to List of
				// players of the game
				Optional<Player> player = playerRepository.findById(idsOfPlayersAtGame[i]);

				if (player.isPresent()) {
					playersAtGame.add(player.get());
				} else {
					return ResponseEntity.badRequest().body("player with id " + idsOfPlayersAtGame[i] + " not found");
				}
			}
			game.setPlayers(playersAtGame);
		}

		// handle list of Goals: Check, whether it has a scorer and zero or one or two
		// assistants. Create Goal, save it to game.

		List<GoalTemplate> goalTemplateList = gameTemplate.getGoalsList();
		List<Goal> goalsAtGame = new ArrayList<>();

		if (goalTemplateList != null) {
			for (int i = 0; i < goalTemplateList.size(); i++) {

				GoalTemplate currentGoalTemplate = goalTemplateList.get(i);
				Long scorerId = currentGoalTemplate.getGoalScorerId();

				if (scorerId != null) {

					Optional<Player> goalScorerOptional = playerRepository.findById(scorerId);

					if (goalScorerOptional.isPresent()) {

						Long firstAssistantId = currentGoalTemplate.getFirstAssistantId();

						if (firstAssistantId != null) {

							if (firstAssistantId.equals(scorerId)) {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body("goalscorer and first assistant cannot be the same player");
							}

							Optional<Player> firstAssistantOptional = playerRepository.findById(firstAssistantId);

							if (firstAssistantOptional.isPresent()) {

								Long secondAssistantId = currentGoalTemplate.getSecondAssistantId();

								if (secondAssistantId != null) {
									if (secondAssistantId.equals(firstAssistantId)
											|| secondAssistantId.equals(scorerId)) {
										return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
												"assistant cannot be the same player as other assistant or goalscorer");
									}

									Optional<Player> secondAssistantOptional = playerRepository
											.findById(secondAssistantId);

									if (secondAssistantOptional.isPresent()) {

										Player secondAssistant = secondAssistantOptional.get();
										Player firstAssistant = firstAssistantOptional.get();
										Player goalScorer = goalScorerOptional.get();

										Goal goal = new Goal(goalScorer, firstAssistant, secondAssistant, game);
										goalRepository.save(goal);
										goalsAtGame.add(goal);
										

										if (!playersAtGame.contains(goalScorer)) {
											playersAtGame.add(goalScorer);
										}
										if (!playersAtGame.contains(firstAssistant)) {
											playersAtGame.add(firstAssistant);
										}
										if (!playersAtGame.contains(secondAssistant)) {
											playersAtGame.add(secondAssistant);
										}
										game.setPlayers(playersAtGame);
									}

									else {
										return ResponseEntity.status(HttpStatus.BAD_REQUEST)
												.body("Player with id " + secondAssistantId + " not found");
									}
								}

								else {
									Player firstAssistant = firstAssistantOptional.get();
									Player goalScorer = goalScorerOptional.get();

									Goal goal = new Goal(goalScorer, firstAssistant, game);
									goalsAtGame.add(goal);
									goalRepository.save(goal);

									if (!playersAtGame.contains(goalScorer)) {
										playersAtGame.add(goalScorer);
									}
									if (!playersAtGame.contains(firstAssistant)) {
										playersAtGame.add(firstAssistant);
									}
									game.setPlayers(playersAtGame);
								}
							} else {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body("Game contains Goal with invalid first Assistant, Player with id "
												+ firstAssistantId + " not found");
							}
						}

						else {
							if (currentGoalTemplate.getSecondAssistantId() != null) {

								Long secondAssistantId = currentGoalTemplate.getSecondAssistantId();
								Optional<Player> secondAssistantOptional = playerRepository.findById(secondAssistantId);

								if (secondAssistantOptional.isPresent()) {

									Player secondAssistant = secondAssistantOptional.get();
									Player goalScorer = goalScorerOptional.get();

									Goal goal = new Goal(goalScorer, secondAssistant, game);
									goalsAtGame.add(goal);
									goalRepository.save(goal);

									if (!playersAtGame.contains(goalScorer)) {
										playersAtGame.add(goalScorer);
									}
									if (!playersAtGame.contains(secondAssistant)) {
										playersAtGame.add(secondAssistant);
									}
									game.setPlayers(playersAtGame);
								}

								else {
									return ResponseEntity.status(HttpStatus.BAD_REQUEST)
											.body("Game contains Goal with invalid second Assistant, Player with id "
													+ secondAssistantId + " not found");
								}
							}

							else {
								Player goalScorer = goalScorerOptional.get();
								Goal goal = new Goal(goalScorer, game);
								goalRepository.save(goal);
								goalsAtGame.add(goal);

								if (!playersAtGame.contains(goalScorer)) {
									playersAtGame.add(goalScorer);
								}
								game.setPlayers(playersAtGame);

							}
						}
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								"Game contains goal with invalid scorer. Player with id " + scorerId + " not found");
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game contains goal with missing scorer");
				}
			}
			game.setGoalsKeile(goalsAtGame);
		}
	
		// to return a ResponseEntity with a header containing the URI of the created
		// resource
		Game savedGame = gameRepository.save(game);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedGame.getGameId()).toUri();
		return ResponseEntity.created(location).body(game);

	}

	// Return list of players that participated in a certain game
	@GetMapping("/games/{gameId}/players")
	public ResponseEntity<Object> getPlayersOfGame(@PathVariable("gameId") Long gameId) {

		if (gameRepository.findById(gameId).isPresent()) {

			List<Player> players = gameRepository.findById(gameId).get().getPlayers();
			if (!players.isEmpty()) {
				return new ResponseEntity<>(players, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("No players saved for game with id " + gameId);
			}
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game with id " + gameId + " not found");
	}

}
