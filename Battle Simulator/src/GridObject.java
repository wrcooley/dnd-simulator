import java.io.Serializable;

public class GridObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public double health;
	public double healthMax;
	public CombatType combatType;
	public String name;
	public Team team;
	public int moves;
	public int maxMoves;

	public GridObject(double healthMax, CombatType combatType, String name, Team team) {
		this.healthMax = healthMax;
		this.health = healthMax;
		this.combatType = combatType;
		this.name = name;
		this.team = team;
		this.moves = 2;
		this.maxMoves = 2;
	}

	public char toChar() {
		return combatType.typeChar;
	}

	public GridObject() {
		this.team = Team.BLANK;
	}

	public boolean isEmpty() {
		return team == Team.BLANK;
	}

	public String stringy() {
		if (team == Team.BLANK) {
			return "";
		} else {
			return "Name: " + name + "\nType: " + combatType + "\nHealth: " + health + "/" + healthMax + "\nTeam: "
					+ team + "\nMoves: " + moves + "/" + maxMoves;
		}
	}

	public GridObject copy() {
		GridObject g = new GridObject();
		g.health = health;
		g.healthMax = healthMax;
		g.combatType = combatType;
		g.name = name;
		g.team = team;
		g.moves = moves;
		g.maxMoves = maxMoves;
		return g;
	}
}