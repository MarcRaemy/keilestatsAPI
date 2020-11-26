package ch.keile.keilestatsAPI;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.keile.keilestatsAPI.entities.Game;
import ch.keile.keilestatsAPI.entities.Goal;
import ch.keile.keilestatsAPI.entities.Opponent;
import ch.keile.keilestatsAPI.entities.Player;
import ch.keile.keilestatsAPI.repositories.GameRepository;
import ch.keile.keilestatsAPI.repositories.GoalRepository;
import ch.keile.keilestatsAPI.repositories.OpponentRepository;
import ch.keile.keilestatsAPI.repositories.PlayerRepository;

/* Annotation @SpringBootApplication: Indicates a configuration class that
 * declares one or more @Bean methods and also triggers auto-configuration,
 * component scanning, and configuration properties scanning. This is a convenience 
 * annotation that is equivalent to declaring @Configuration, 
 * @EnableAutoConfiguration, @ComponentScan.
 */
@SpringBootApplication
/*
 * Class SpringApplication bootstraps the Application: -creates an
 * ApplicationContext instance -registers a CommandLinePropertySource to expose
 * command line arguments as spring properties -refresh application context,
 * loading all singleton beans -Trigger Any command line runner beans
 */
public class KeileStatsApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(getClass());

	// Repositories used for setUpData()-Method in order to create sample-Data to test the API
	@Autowired
	GameRepository gameRepository;

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	OpponentRepository opponentRepository;

	@Autowired
	GoalRepository goalRepository;

	public static void main(String[] args) {

		SpringApplication.run(KeileStatsApplication.class, args);
		System.out.println("Hello");
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		setUpData();
	}

	/* Create some Data for testing purpose and save in Database */
	public void setUpData() {

		// create and 3 Players
		Player player1 = new Player("Mueller", "Max");
		Player player2 = new Player("Meier", "Erich");
		Player player3 = new Player("Hugentobler", "Daniel");

		// Create Set of Players that played each game (both the same here)
		List<Player> playerKeileGame1 = new ArrayList<>();

		playerKeileGame1.add(player1);
		playerKeileGame1.add(player2);
		playerKeileGame1.add(player3);

		List<Player> playerKeileGame2 = new ArrayList<>();

		playerKeileGame2.add(player1);
		playerKeileGame2.add(player2);
		playerKeileGame2.add(player3);

		// save players to the database
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);

		// Create 2 Opponents
		Opponent opponent1 = new Opponent("HC Gurmels Senioren");
		Opponent opponent2 = new Opponent("HC Tiletz");

		// Save Opponents to the database
		opponentRepository.save(opponent1);
		opponentRepository.save(opponent2);

		// Create two games
		Game game1 = new Game();
		Game game2 = new Game();

		gameRepository.save(game1);
		gameRepository.save(game2);

		// Create Goals
		Goal goal1 = new Goal(player1, player2, player3, game1);
		Goal goal2 = new Goal(player2, player1, game1);
		Goal goal3 = new Goal(player1, game1);
		Goal goal4 = new Goal(player3, player1, player2, game2);
		Goal goal5 = new Goal(player2, player3, player1, game2);
		Goal goal6 = new Goal(player1, game2);
		Goal goal7 = new Goal(player1, player2, player3, game2);

		// Assign Goals to 2 different Games
		List<Goal> goalsKeileGame1 = new ArrayList<>();

		goalsKeileGame1.add(goal1);
		goalsKeileGame1.add(goal2);
		goalsKeileGame1.add(goal3);

		List<Goal> goalsKeileGame2 = new ArrayList<>();

		goalsKeileGame2.add(goal4);
		goalsKeileGame2.add(goal5);
		goalsKeileGame2.add(goal6);
		goalsKeileGame2.add(goal7);

		// Save Goals to the database
		goalRepository.save(goal1);
		goalRepository.save(goal2);
		goalRepository.save(goal3);
		goalRepository.save(goal4);
		goalRepository.save(goal5);
		goalRepository.save(goal6);
		goalRepository.save(goal7);

		// Create 2 Games
		game1.setGameDate("15.10.2017");
		game1.setOpponent(opponent1);
		game1.setGoalsKeile(goalsKeileGame1);
		game1.setPlayers(playerKeileGame1);
		game1.setNbGoalsOpponent(2);
		game1.setNbGoalsKeile(goalsKeileGame1.size());

		game2.setGameDate("7.11.2018");
		game2.setOpponent(opponent2);
		game2.setGoalsKeile(goalsKeileGame2);
		game2.setPlayers(playerKeileGame2);
		game2.setNbGoalsOpponent(1);
		game2.setNbGoalsKeile(goalsKeileGame2.size());

		// Save games to database
		gameRepository.save(game1);
		gameRepository.save(game2);

	}
}
