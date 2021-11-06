public enum CombatType {
	HEAVYINFANTRY(0, 'H'), LIGHTINFANTRY(1, 'L'), ARCHERS(2, 'A'), ARTILLERY(3, 'B'), MAGES(4, 'M'), COMMAND(5, 'C'),
	GENERIC(6, 'X'), BLANK(7, 'O');

	public int typeCode;
	public char typeChar;

	private CombatType(int typeCode, char typeChar) {
		this.typeCode = typeCode;
		this.typeChar = typeChar;
	}

	public static CombatType[] getAllTypes() {
		CombatType[] ct = { null, HEAVYINFANTRY, LIGHTINFANTRY, ARCHERS, ARTILLERY, MAGES, COMMAND, GENERIC, BLANK };
		return ct;
	}
}