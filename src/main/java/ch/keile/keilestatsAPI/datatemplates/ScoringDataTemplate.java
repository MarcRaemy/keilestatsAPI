package ch.keile.keilestatsAPI.datatemplates;


/*Class to help presenting scoring data overwiew for the endpoint GET players/scoringtable*/
public class ScoringDataTemplate {

	Long playerId;
	String playerFirstName;
	String playerLastName;
	Integer assistsScored;
	Integer goalsScored;
	Integer totalPoints;
	Integer gamesPlayed;

	public ScoringDataTemplate() {
	}

	public ScoringDataTemplate(Long playerId, String playerFirstName, String playerLastName, Integer assistsScored,
			Integer goalsScored, Integer totalPoints, Integer gamesPlayed) {
		this.playerId = playerId;
		this.playerFirstName = playerFirstName;
		this.playerLastName = playerLastName;
		this.assistsScored = assistsScored;
		this.goalsScored = goalsScored;
		this.totalPoints = totalPoints;
		this.gamesPlayed = gamesPlayed;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public String getPlayerLastName() {
		return playerLastName;
	}

	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	public Integer getAssistsScored() {
		return assistsScored;
	}

	public void setAssistsScored(Integer assistsScored) {
		this.assistsScored = assistsScored;
	}

	public Integer getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(Integer goalsScored) {
		this.goalsScored = goalsScored;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Integer getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(Integer gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	@Override
	public String toString() {
		return "ScoringDataTemplate [playerId=" + playerId + ", playerFirstName=" + playerFirstName
				+ ", playerLastName=" + playerLastName + ", assistsScored=" + assistsScored + ", goalsScored="
				+ goalsScored + ", totalPoints=" + totalPoints + ", gamesPlayed=" + gamesPlayed + "]";
	}

}
