import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class GPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public int scaleFactor;
	public Board b;

	public GPanel(int scaleFactor, Board b) {
		this.b = b;
		this.scaleFactor = scaleFactor;
	}

	@Override
	public void paintComponent(Graphics g) {
		this.removeAll();
		mainArea(g);
		optionsArea(g);
	}

	public void mainArea(Graphics g) {
		g.setColor(Color.RED);
		if (b.selX >= 0 && b.selY >= 0) {
			g.fillRect(b.selX * scaleFactor, b.selY * scaleFactor, scaleFactor, scaleFactor);
		}

		g.setColor(Color.BLUE);
		if (b.sel2X >= 0 && b.sel2Y >= 0) {
			g.fillRect(b.sel2X * scaleFactor, b.sel2Y * scaleFactor, scaleFactor, scaleFactor);
		}

		g.setColor(Color.BLACK);
		for (int y = 0; y <= b.gridHeight; y++) {
			g.drawLine(0, y * scaleFactor, b.gridWidth * scaleFactor, y * scaleFactor);
		}

		for (int x = 0; x <= b.gridWidth; x++) {
			g.drawLine(x * scaleFactor, 0, x * scaleFactor, b.gridHeight * scaleFactor);
		}

		for (int i = 0; i < b.gridHeight; i++) {
			for (int j = 0; j < b.gridWidth; j++) {
				if (!b.grid[i][j].isEmpty()) {
					g.setColor(Color.BLACK);
					g.fillOval(j * scaleFactor, i * scaleFactor, scaleFactor, scaleFactor);
					g.setColor(Color.GREEN);
					g.fillRect(j * scaleFactor, i * scaleFactor + 9 * scaleFactor / 10, scaleFactor, scaleFactor / 10);
					g.setColor(Color.RED);
					int healthPct = (int) (b.grid[i][j].health / b.grid[i][j].healthMax * scaleFactor);
					if (healthPct < 0) {
						healthPct = 0;
					} else if (healthPct > scaleFactor) {
						healthPct = scaleFactor;
					}
					g.fillRect(j * scaleFactor + healthPct, i * scaleFactor + 9 * scaleFactor / 10,
							scaleFactor - healthPct, scaleFactor / 10);
					g.setColor(Color.BLACK);
					g.drawRect(j * scaleFactor, i * scaleFactor + 9 * scaleFactor / 10, scaleFactor, scaleFactor / 10);

					JTextArea title = new JTextArea(b.grid[i][j].name);
					title.setWrapStyleWord(true);
					title.setEditable(false);
					if (b.grid[i][j].team == Team.PLAYER) {
						title.setBackground(Color.BLUE);
					} else {
						title.setBackground(Color.RED);
					}
					title.setForeground(Color.WHITE);
					title.setBounds(j * scaleFactor, i * scaleFactor, scaleFactor, scaleFactor / 5);
					this.add(title);
				}
			}
		}
	}

	public void optionsArea(Graphics g) {
		JButton delete = new JButton("Delete Unit");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty()) {
						b.delete(b.selY, b.selX);
						redo();
					}
				}
			}
		});
		delete.setBounds(b.gridWidth * scaleFactor, scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(delete);

		JButton add = new JButton("Add Unit");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1) {
					if (b.grid[b.selY][b.selX].isEmpty()) {
						b.add(b.selY, b.selX, new GridObject(100, CombatType.GENERIC, "Test Unit", Team.GENERIC));
						redo();
					}
				}
			}
		});
		add.setBounds(b.gridWidth * scaleFactor, 0, 2 * scaleFactor, scaleFactor);
		this.add(add);

		JTextArea info = new JTextArea();
		try {
			info.setText(b.grid[b.selY][b.selX].stringy());
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {

		}
		info.setBounds(b.gridWidth * scaleFactor, 9 * scaleFactor, 2 * scaleFactor, 1 * scaleFactor);
		info.setEditable(false);
		this.add(info);

		JButton edit = new JButton("Edit Unit");
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty()) {
						JPanel myPanel = new JPanel();
						myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

						JTextField newName = new JTextField(10);
						newName.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						JTextField newHealth = new JTextField(5);
						newHealth.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						JTextField newMaxHealth = new JTextField(5);
						newMaxHealth.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						CombatType[] s = CombatType.getAllTypes();
						JComboBox<CombatType> newCombatType = new JComboBox<CombatType>(s);
						newCombatType.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						Team[] s2 = Team.getAllTypes();
						JComboBox<Team> newTeam = new JComboBox<Team>(s2);
						newTeam.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						JTextField newMoves = new JTextField(5);
						newMoves.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						JTextField newMaxMoves = new JTextField(5);
						newMaxMoves.setAlignmentX(JLabel.LEFT_ALIGNMENT);

						JLabel n = new JLabel("Name:");
						n.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(n);
						myPanel.add(newName);

						JLabel h = new JLabel("Health:");
						h.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(h);
						myPanel.add(newHealth);

						JLabel m = new JLabel("Max Health:");
						m.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(m);
						myPanel.add(newMaxHealth);

						JLabel mo = new JLabel("Moves:");
						mo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(mo);
						myPanel.add(newMoves);

						JLabel mmo = new JLabel("Max Moves:");
						mmo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(mmo);
						myPanel.add(newMaxMoves);

						JLabel c = new JLabel("CombatType:");
						c.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(c);
						myPanel.add(newCombatType);

						JLabel t = new JLabel("Team:");
						t.setAlignmentX(JLabel.LEFT_ALIGNMENT);
						myPanel.add(t);
						myPanel.add(newTeam);

						int result = JOptionPane.showConfirmDialog(null, myPanel, "Edit Unit",
								JOptionPane.OK_CANCEL_OPTION);

						if (result == JOptionPane.OK_OPTION) {
							if (!newName.getText().isBlank()) {
								b.grid[b.selY][b.selX].name = newName.getText();
							}

							if (!newHealth.getText().isBlank()) {
								try {
									b.grid[b.selY][b.selX].health = Double.parseDouble(newHealth.getText());
								} catch (java.lang.NumberFormatException e) {

								}

							}

							if (!newMaxHealth.getText().isBlank()) {
								try {
									b.grid[b.selY][b.selX].healthMax = Double.parseDouble(newMaxHealth.getText());
								} catch (java.lang.NumberFormatException e) {

								}
							}

							if (!newMoves.getText().isBlank()) {
								try {
									b.grid[b.selY][b.selX].moves = Integer.parseInt(newMoves.getText());
								} catch (java.lang.NumberFormatException e) {

								}
							}

							if (!newMaxMoves.getText().isBlank()) {
								try {
									b.grid[b.selY][b.selX].maxMoves = Integer.parseInt(newMaxMoves.getText());
								} catch (java.lang.NumberFormatException e) {

								}
							}

							if (newCombatType.getSelectedItem() != null) {
								b.grid[b.selY][b.selX].combatType = (CombatType) newCombatType.getSelectedItem();

							}

							if (newTeam.getSelectedItem() != null) {
								b.grid[b.selY][b.selX].team = (Team) newTeam.getSelectedItem();

							}

						}
						redo();
					}
				}
			}
		});
		edit.setBounds(b.gridWidth * scaleFactor, 2 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(edit);

		JButton move = new JButton("Move Unit");
		move.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1 && b.sel2X != -1 && b.sel2Y != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty() && b.grid[b.sel2Y][b.sel2X].isEmpty()) {
						b.move(b.selY, b.selX, b.sel2Y, b.sel2X);
						b.selX = b.sel2X;
						b.selY = b.sel2Y;
						b.sel2X = -1;
						b.sel2X = -1;
						redo();
					}
				}
			}
		});
		move.setBounds(b.gridWidth * scaleFactor, 4 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(move);

		JButton attack = new JButton("Attack Unit");
		attack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1 && b.sel2X != -1 && b.sel2Y != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty() && b.grid[b.sel2Y][b.sel2X].isEmpty()) {
						b.move(b.selY, b.selX, b.sel2Y, b.sel2X);
						b.selX = b.sel2X;
						b.selY = b.sel2Y;
						b.sel2X = -1;
						b.sel2X = -1;
						redo();
					}
				}
			}
		});
		attack.setLayout(null);
		attack.setBounds(b.gridWidth * scaleFactor, 5 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(attack);

		JButton copy = new JButton("Copy Unit");
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1 && b.sel2X != -1 && b.sel2Y != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty() && b.grid[b.sel2Y][b.sel2X].isEmpty()) {
						b.grid[b.sel2Y][b.sel2X] = b.grid[b.selY][b.selX].copy();
						redo();
					}
				}
			}
		});
		copy.setLayout(null);
		copy.setBounds(b.gridWidth * scaleFactor, 3 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(copy);

		JButton save = new JButton("Save Board");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame parent = new JFrame();
				String fileName = JOptionPane.showInputDialog(parent, "Name of Board:", null);
				b.save(fileName);
			}
		});
		save.setLayout(null);
		save.setBounds(b.gridWidth * scaleFactor, 6 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(save);

		JButton load = new JButton("Load Board");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File workingDirectory = new File(System.getProperty("user.dir"));
					JFrame chooserHolder = new JFrame("Choose a file");
					JFileChooser j = new JFileChooser();
					j.setCurrentDirectory(workingDirectory);
					int returnVal = j.showOpenDialog(chooserHolder);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = j.getSelectedFile();
						FileInputStream fi = new FileInputStream(file);
						ObjectInputStream oi = new ObjectInputStream(fi);
						Board b1 = (Board) oi.readObject();
						oi.close();
						fi.close();
						chooserHolder.dispose();
						setBoard(b1);
					} else {
						chooserHolder.dispose();
					}

				} catch (FileNotFoundException e) {
					System.out.println("File not found");
				} catch (IOException e) {
					System.out.println("Error initializing stream");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		load.setLayout(null);
		load.setBounds(b.gridWidth * scaleFactor, 7 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(load);

		JButton newBoard = new JButton("New Board");
		newBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanel myPanel = new JPanel();
				myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

				JTextField newWidth = new JTextField(10);
				newWidth.setAlignmentX(JLabel.LEFT_ALIGNMENT);

				JTextField newHeight = new JTextField(10);
				newHeight.setAlignmentX(JLabel.LEFT_ALIGNMENT);

				JLabel nw = new JLabel("New Width: ");
				nw.setAlignmentX(JLabel.LEFT_ALIGNMENT);
				myPanel.add(nw);
				myPanel.add(newWidth);

				JLabel nh = new JLabel("New Height: ");
				nh.setAlignmentX(JLabel.LEFT_ALIGNMENT);
				myPanel.add(nh);
				myPanel.add(newHeight);

				int result = JOptionPane.showConfirmDialog(null, myPanel, "New Board", JOptionPane.OK_CANCEL_OPTION);

				if (result == JOptionPane.OK_OPTION) {
					try {
						int newHeightInt = Integer.parseInt(newHeight.getText());
						int newWidthInt = Integer.parseInt(newWidth.getText());
						if (newHeightInt < 10) {
							newHeightInt = 10;
						}
						b.replace(new Board(newHeightInt, newWidthInt));
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				repack();
			}
		});
		newBoard.setLayout(null);
		newBoard.setBounds(b.gridWidth * scaleFactor, 8 * scaleFactor, 2 * scaleFactor, scaleFactor);
		this.add(newBoard);

		JButton plusMove = new JButton("+Move");
		plusMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty()) {
						b.grid[b.selY][b.selX].moves++;
						redo();
					}
				}
			}
		});
		plusMove.setLayout(null);
		plusMove.setBounds((b.gridWidth + 2) * scaleFactor, scaleFactor, scaleFactor, scaleFactor);
		this.add(plusMove);

		JButton minusMove = new JButton("-Move");
		minusMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (b.selX != -1 && b.selY != -1) {
					if (!b.grid[b.selY][b.selX].isEmpty()) {
						b.grid[b.selY][b.selX].moves--;
						redo();
					}
				}
			}
		});
		minusMove.setLayout(null);
		minusMove.setBounds((b.gridWidth + 2) * scaleFactor, 0, scaleFactor, scaleFactor);
		this.add(minusMove);

		JButton turn = new JButton("Turn: " + b.turn);
		turn.setForeground(Color.WHITE);
		if (b.turn % 2 == 0) {
			turn.setBackground(Color.BLUE);
		} else {
			turn.setBackground(Color.RED);
		}
		turn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				b.turn++;
				turn.setText("Turn: " + b.turn);
				if (b.turn % 2 == 0) {
					turn.setBackground(Color.BLUE);
				} else {
					turn.setBackground(Color.RED);
				}
				redo();
			}
		});
		turn.setLayout(null);
		turn.setBounds((b.gridWidth + 2) * scaleFactor, scaleFactor * 9, scaleFactor, scaleFactor);
		this.add(turn);
	}

	public void redo() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.repaint();
	}

	public void repack() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		this.setPreferredSize(new Dimension(scaleFactor * (b.gridWidth + 2), scaleFactor * b.gridHeight));
		topFrame.pack();
		topFrame.repaint();
	}

	public void setBoard(Board b) {
		this.b.replace(b);
		repack();
	}
}