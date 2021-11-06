import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	int gridWidth;
	int gridHeight;
	int selX;
	int selY;
	int sel2X;
	int sel2Y;
	int turn;
	GridObject[][] grid;

	public Board(int gridHeight, int gridWidth, GridObject[][] grid) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.grid = grid;
		this.selX = -1;
		this.selY = -1;
		this.sel2X = -1;
		this.sel2Y = -1;
		this.turn = 0;
	}

	public Board(int gridHeight, int gridWidth) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.grid = new GridObject[gridHeight][gridWidth];
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				grid[i][j] = new GridObject();
			}
		}
		this.selX = -1;
		this.selY = -1;
		this.sel2X = -1;
		this.sel2Y = -1;
		this.turn = 0;
	}

	public void printBoard() {
		for (GridObject[] g : grid) {
			for (GridObject g2 : g) {
				try {
					System.out.print(g2.toChar());
				} catch (java.lang.NullPointerException e) {
					System.out.print("O");
				}
			}
			System.out.print("\n");
		}
	}

	public GridObject delete(int gridYpos, int gridXpos) {
		GridObject g = grid[gridYpos][gridXpos];
		grid[gridYpos][gridXpos] = new GridObject();
		return g;
	}

	public void add(int gridYpos, int gridXpos, GridObject g) {
		grid[gridYpos][gridXpos] = g;
	}

	public void move(int gridYpos, int gridXpos, int gridYpos2, int gridXpos2) {
		GridObject g = delete(gridYpos, gridXpos);
		add(gridYpos2, gridXpos2, g);
	}

	public void save(String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(new File(fileName));
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(this);
			o.close();
			f.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			System.out.println(e);
		}
	}

	public void replace(Board b) {
		selX = -1;
		selY = -1;
		sel2X = -1;
		sel2Y = -1;
		gridHeight = b.gridHeight;
		gridWidth = b.gridWidth;
		grid = b.grid;
		turn = b.turn;
	}
}