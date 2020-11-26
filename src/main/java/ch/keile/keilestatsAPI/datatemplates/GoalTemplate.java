package ch.keile.keilestatsAPI.datatemplates;

/*
 * Class to help to pass data to the POST and PUT-methods of the
 *  goal controller. Template for collecting, saving and 
 *  presenting goal data*
 */
public class GoalTemplate {

	Long goalScorerId;
	Long firstAssistantId;
	Long secondAssistantId;

	public GoalTemplate() {
	}

	public Long getGoalScorerId() {
		return goalScorerId;
	}

	public void setGoalScorerId(Long goalScorerId) {
		this.goalScorerId = goalScorerId;
	}

	public Long getFirstAssistantId() {
		return firstAssistantId;
	}

	public void setFirstAssistantId(Long firstAssistantId) {
		this.firstAssistantId = firstAssistantId;
	}

	public Long getSecondAssistantId() {
		return secondAssistantId;
	}

	public void setSecondAssistantId(Long secondAssistantId) {
		this.secondAssistantId = secondAssistantId;
	}

	@Override
	public String toString() {
		return "GoalTemplate [goalScorerId=" + goalScorerId + ", firstAssistantId=" + firstAssistantId
				+ ", secondAssistangId=" + secondAssistantId + "]";
	}

}
