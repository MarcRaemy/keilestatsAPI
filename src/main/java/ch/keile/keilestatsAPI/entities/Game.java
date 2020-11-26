package ch.keile.keilestatsAPI.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
/*
 * @Entity = Annotation that marks the Class to JPA as a persistent Entity. And
 * indicates to the framework as a bean to instantiate for the application.
 */
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long gameId;

	private String gameDate;

	// Cascade attribute defines how changes on one side of the association are
	// "cascaded" to the other side.
	@OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
	private List<Goal> goalsKeile = new ArrayList<>();

	private Integer nbGoalsKeile;
	private Integer nbGoalsOpponent;

	@ManyToOne
	@JoinColumn(name = "OPPONENT_ID") // marks and names column, where the foreign key of the related entity is saved
	private Opponent opponent;

	@ManyToMany
	// Definition of the columns of the new table emerging from the many-to-many
	// relationship
	@JoinTable(joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "player_id"))
	@JsonIgnoreProperties("games")
	private List<Player> players = new ArrayList<>();

	public Game() {
	}

	public Game(String gameDate, Opponent opponent, Integer nbGoalsKeile, Integer goalsOpponent, List<Player> players,
			List<Goal> goalsKeile) {

		this.gameDate = gameDate;
		this.goalsKeile = goalsKeile;
		this.nbGoalsKeile = nbGoalsKeile;
		this.nbGoalsOpponent = goalsOpponent;
		this.opponent = opponent;
		this.players = players;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long id) {
		this.gameId = id;
	}

	public String getGameDate() {
		return gameDate;
	}

	public void setGameDate(String date) {

		this.gameDate = date;
	}

	@JsonManagedReference(value = "GoalinGame")
	/*
	 * Annotation used to indicate that annotated property is part of two-way
	 * linkage between fields; and that its role is "parent" (or "forward") link.
	 * Necessary to break inifite loop at serialisation and deserialisation
	 */
	public List<Goal> getGoalsKeile() {
		return goalsKeile;
	}

	public void setGoalsKeile(List<Goal> goals) {

		this.goalsKeile = goals;
	}

	public int getNbGoalsOpponent() {
		return nbGoalsOpponent;
	}

	public void setNbGoalsOpponent(Integer nbGoalsOpponent) {
		this.nbGoalsOpponent = nbGoalsOpponent;
	}

	@JsonManagedReference(value = "opponentInGame")
	public Opponent getOpponent() {
		return opponent;
	}

	public void setOpponent(Opponent opponent) {
		this.opponent = opponent;
	}

	@JsonManagedReference
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {

		this.players = players;
	}

	public Integer getNbGoalsKeile() {
		return nbGoalsKeile;
	}

	public void setNbGoalsKeile(Integer nbGoalsKeile) {
		this.nbGoalsKeile = nbGoalsKeile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameDate == null) ? 0 : gameDate.hashCode());
		result = prime * result + ((goalsKeile == null) ? 0 : goalsKeile.hashCode());
		result = prime * result + ((nbGoalsKeile == null) ? 0 : nbGoalsKeile.hashCode());
		result = prime * result + ((nbGoalsOpponent == null) ? 0 : nbGoalsOpponent.hashCode());
		result = prime * result + ((opponent == null) ? 0 : opponent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (gameDate == null) {
			if (other.gameDate != null)
				return false;
		} else if (!gameDate.equals(other.gameDate))
			return false;
		if (goalsKeile == null) {
			if (other.goalsKeile != null)
				return false;
		} else if (!goalsKeile.equals(other.goalsKeile))
			return false;
		if (nbGoalsKeile == null) {
			if (other.nbGoalsKeile != null)
				return false;
		} else if (!nbGoalsKeile.equals(other.nbGoalsKeile))
			return false;
		if (nbGoalsOpponent == null) {
			if (other.nbGoalsOpponent != null)
				return false;
		} else if (!nbGoalsOpponent.equals(other.nbGoalsOpponent))
			return false;
		if (opponent == null) {
			if (other.opponent != null)
				return false;
		} else if (!opponent.equals(other.opponent))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "Game [gameId = " + gameId + ", gameDate = " + gameDate + ", goals_keile=" + goalsKeile
				+ ", goals_opponent=" + nbGoalsOpponent + ", opponent=" + opponent + ", players=" + players + "]";
	}
}
