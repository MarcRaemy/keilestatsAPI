package ch.keile.keilestatsAPI.datatemplates;


import java.util.Arrays;
import java.util.List;

/*Class to help to pass data to the POST- and PUT-methods of the game controller.
 * Template for collecting, saving and presenting game data*/
public class GameTemplate {

	String gameDate;
	String opponentName;
	Long[] playerIdList;
	List<GoalTemplate> goalTemplateList;
	Integer nbGoalsOpponent;
	Integer nbGoalsKeile;

	public GameTemplate() {

	}

	public GameTemplate(String gameDate, String opponent, Long[] playersList, List<GoalTemplate> goalsList,
			Integer nbGoalsOpponent, Integer nbGoalsKeile) {
		super();
		this.gameDate = gameDate;
		this.opponentName = opponent;
		this.playerIdList = playersList;
		this.goalTemplateList = goalsList;
		this.nbGoalsOpponent = nbGoalsOpponent;
		this.nbGoalsKeile = nbGoalsKeile;
	}

	public String getGameDate() {
		return gameDate;
	}

	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}

	public Long[] getPlayerIdList() {
		return playerIdList;
	}

	public void setPlayerIdList(Long[] playersList) {
		this.playerIdList = playersList;
	}

	public List<GoalTemplate> getGoalsList() {
		return goalTemplateList;
	}

	public void setGoalsList(List<GoalTemplate> goalsList) {
		this.goalTemplateList = goalsList;
	}

	public Integer getNbGoalsOpponent() {
		return nbGoalsOpponent;
	}

	public void setNbGoalsOpponent(Integer nbGoalsOpponent) {
		this.nbGoalsOpponent = nbGoalsOpponent;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public Integer getNbGoalsKeile() {
		return nbGoalsKeile;
	}

	public void setNbGoalsKeile(Integer nbGoalsKeile) {
		this.nbGoalsKeile = nbGoalsKeile;
	}

	@Override
	public String toString() {
		return "GameTemplate [gameDate=" + gameDate + ", opponentName=" + opponentName + ", playerIdList="
				+ Arrays.toString(playerIdList) + ", goalTemplateList=" + goalTemplateList + ", nbGoalsOpponent="
				+ nbGoalsOpponent + ", nbGoalsKeile=" + nbGoalsKeile + "]";
	}

}

