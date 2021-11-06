public enum Team {
	PLAYER(0), MISTEMPIRE(1), BANDIT(2), GENERIC(3), BLANK(4);

	public int teamCode;

	private Team(int teamCode) {
		this.teamCode = teamCode;
	}

	public static Team[] getAllTypes() {
		Team[] t = { null, PLAYER, MISTEMPIRE, BANDIT, GENERIC, BLANK };
		return t;
	}
}