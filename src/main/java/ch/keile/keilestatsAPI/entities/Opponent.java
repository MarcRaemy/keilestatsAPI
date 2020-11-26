package ch.keile.keilestatsAPI.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Opponent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long opponentId;

	private String opponentName;

	@OneToMany(mappedBy = "opponent")
	private List<Game> games = new ArrayList<>();

	// Empty constructor needed by Spring Boot
	public Opponent() {
	}

	public Opponent(String name) {

		this.opponentName = name;
	}

	public Opponent(String name, List<Game> games) {

		this.opponentName = name;
		this.games = games;
	}

	public Long getOpponentId() {
		return opponentId;
	}

	public void setOpponentId(Long id) {
		this.opponentId = id;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String name) {
		this.opponentName = name;
	}

	@JsonBackReference(value = "opponentInGame")
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((opponentName == null) ? 0 : opponentName.hashCode());
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
		Opponent other = (Opponent) obj;
		if (opponentName == null) {
			if (other.opponentName != null)
				return false;
		} else if (!opponentName.equals(other.opponentName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Opponent [opponentId=" + opponentId + ", opponentName=" + opponentName + "games=" + games + "]";
	}
}
