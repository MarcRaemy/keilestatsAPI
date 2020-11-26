package ch.keile.keilestatsAPI.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity /*
		 * Annotation that marks the Class to JPA as a persistent Entity. And indicates
		 * to the framework as a bean to instantiate for the application.
		 */
public class Goal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long goalId;

	@ManyToOne
	@JoinColumn(name = "GAME_ID") // marks and names column, where the foreign key of the related entity is saved
	private Game game;

	@ManyToOne
	@JoinColumn(name = "SCORER_ID")
	private Player goalScorer;

	@ManyToOne
	@JoinColumn(name = "ASSISTANT1_ID")
	private Player firstAssistant;

	@ManyToOne
	@JoinColumn(name = "ASSISTANT2_ID")
	private Player secondAssistant;

	public Goal() {}; 	// Empty constructor required by the framework

	public Goal(Player goalScorer, Game game) {

		this.goalScorer = goalScorer;
		this.game = game;
	}

	public Goal(Player goalScorer, Player firstAssistant, Game game) {

		this.goalScorer = goalScorer;
		this.firstAssistant = firstAssistant;
		this.game = game;
	}

	public Goal(Player goalScorer, Player firstAssistant, Player secondAssistant, Game game) {

		this.goalScorer = goalScorer;
		this.firstAssistant = firstAssistant;
		this.secondAssistant = secondAssistant;
		this.game = game;
	}

	public Long getGoalId() {
		return goalId;
	}

	public void setGoalId(Long id) {
		this.goalId = id;
	}

	@JsonBackReference(value = "gameInGoal")
	/*
	 * Annotation used to indicate that associated property is part of two-way
	 * linkage between fields; and that its role is "child" (or "back") link. Used
	 * to prevent infinite loop at serialisation and deserialisation
	 */
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@JsonManagedReference(value = "goalScorerInGoal")
	/*
	 * Annotation used to indicate that annotated property is part of two-way
	 * linkage between fields; and that its role is "parent" (or "forward") link.
	 * Necessary to break inifite loop at serialisation and deserialisation
	 */
	public Player getGoalScorer() {
		return goalScorer;
	}

	public void setGoalScorer(Player goalScorer) {
		this.goalScorer = goalScorer;

	}

	@JsonManagedReference(value = "firstAssistantInGoal")
	public Player getFirstAssistant() {
		return firstAssistant;
	}

	public void setFirstAssistant(Player firstAssistant) {
		this.firstAssistant = firstAssistant;
	}

	@JsonManagedReference(value = "secondAssistantInGoal")
	public Player getSecondAssistant() {
		return secondAssistant;
	}

	public void setSecondAssistant(Player assistant2) {
		this.secondAssistant = assistant2;

	}

	@Override
	public String toString() {
		return "Goal [goalId=" + goalId + ", game=" + game + ", scorer=" + goalScorer + ", assist1=" + firstAssistant
				+ ", assist2=" + secondAssistant + "]";
	}

}
