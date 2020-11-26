package ch.keile.keilestatsAPI.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity /*
		 * Annotation that marks the Class to JPA as a persistent Entity and indicates
		 * it to the framework as a bean to instantiate for the application.
		 */
@JsonIgnoreProperties(value = { "goalsScored", "firstAssists", "secondAssists" })
/*
 * Annotation used to avoid inifinite loop at serialisation and deserialisation
 */
public class Player {

	@Id // marks the attribute primary key to the persistence layer
	@GeneratedValue(strategy = GenerationType.IDENTITY) // strategy to generate the primary key
	@Column(nullable = false)
	private Long playerId;
	private String lastname;
	private String firstname;
	private String position;
	private String email;
	private String address;
	private String phone;

	/*
	 * Indicates the relationship, many-to-many, between the entities player and
	 * game
	 */
	@ManyToMany(mappedBy = "players")
	/* Annotation to break the infinite loop occurring at serialisation */
	@JsonIgnoreProperties("players")
	private List<Game> games = new ArrayList<>();

	@OneToMany(mappedBy = "goalScorer")
	/*
	 * Indicates the relation between the entity goal and player and that the
	 * relationship is mapped by the attribute "goalScorer" of the "Goal"-table.
	 * foreign key for the goal scorer is saved in the "Goal"-Table
	 */
	private List<Goal> goalsScored = new ArrayList<>();

	@OneToMany(mappedBy = "firstAssistant")
	private List<Goal> firstAssists = new ArrayList<>();

	@OneToMany(mappedBy = "secondAssistant")
	private List<Goal> secondAssists = new ArrayList<>();

	// Empty constructor required by the framework
	public Player() {
	}

	public Player(String lastname, String firstname) {

		this.lastname = lastname;
		this.firstname = firstname;
	}

	public Player(String lastname, String firstname, String position, String email, String address, String phone) {

		this.lastname = lastname;
		this.firstname = firstname;
		this.position = position;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@JsonBackReference
	/*
	 * Annotation used to indicate that associated property is part of two-way
	 * linkage between fields; and that its role is "child" (or "back") link. Used
	 * to prevent infinite loop at serialisation and deserialisation
	 */
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	@JsonBackReference(value = "goalScorerInGoal")
	public List<Goal> getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(List<Goal> goalsScored) {
		this.goalsScored = goalsScored;
	}

	@JsonBackReference(value = "firstAssistantInGoal")
	public List<Goal> getFirstAssists() {
		return firstAssists;
	}

	public void setFirstAssists(List<Goal> firstAssists) {
		this.firstAssists = firstAssists;
	}

	@JsonBackReference(value = "secondAssistantInGoal")
	public List<Goal> getSecondAssists() {
		return secondAssists;
	}

	public void setSecondAssists(List<Goal> secondAssists) {
		this.secondAssists = secondAssists;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
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
		Player other = (Player) obj;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", lastname=" + lastname + ", firstname=" + firstname + ", position="
				+ position + ", address=" + address + ", phone=" + phone + ", email=" + email + ", games=" + games
				+ "Goals=" + goalsScored + "Assist1=" + firstAssists + "Assist2=" + secondAssists + "]";
	}

}
