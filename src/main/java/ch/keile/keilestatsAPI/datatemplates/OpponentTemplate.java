package ch.keile.keilestatsAPI.datatemplates;


/*Class to help to pass data to the POST- and PUT method of the opponent controller.
 * Template for collecting, saving and presenting Opponent data*/
public class OpponentTemplate {

		String opponentName;

		public OpponentTemplate() {
		}
		
		public OpponentTemplate(String opponentName) {
			super();
			this.opponentName = opponentName;
		}

		public String getOpponentName() {
			return opponentName;
		}

		public void setOpponentName(String opponentName) {
			this.opponentName = opponentName;
		}

		@Override
		public String toString() {
			return "OpponentTemplate [opponentName=" + opponentName + "]";
		}		
}
