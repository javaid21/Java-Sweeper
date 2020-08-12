import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class GUI extends JFrame{
	
	public final int TOP_BAR = 26;
	int neighs = 0;
	int spacing = 3;
	
	public int mouseX = -100;
	public int mouseY = -100;
	
	public int smileX = 505;
	public int smileY = 5;
	
	public boolean flagValue = true;
	public boolean win = false;
	public boolean lose = false;
	public boolean resetter = false;
	
	public int resetX = smileX + 25;
	public int resetY = smileY + 25;
	
	Random random = new Random();
	
	Date startTime = new Date();
	Date endTime;
	public int timeX = 940;
	public int timeY = 0;
	
	public int winMessageX = 20;
	public int winMessageY = -50;
	String message = "Still Playing!";
	
	public int seconds = 0;
	
	int [][] bombs = new int[16][9];
	int [][] neighbors = new int[16][9];
	boolean [][] revealed = new boolean[16][9];
	boolean [][] flagged = new boolean[16][9];
	
	public int flagX = 405;
	public int flagY = 5;
	
	public int flagCenterX = flagX + 25;
	public int flagCenterY = flagY + 25;
	public boolean pressedFlag = false;
	
	public GUI() {
		this.setTitle("Java Sweeper");
		this.setSize(1060, 709);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(random.nextInt(100) < 20) {
					bombs[i][j] = 1;
				}
				else {
					bombs[i][j] = 0;
				}
				revealed[i][j] = false;
			}
		}
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				neighs = 0;
				for(int k = 0; k < 16; k++) {
					for(int l = 0; l < 9; l++) {
						if(!(k == i && l == j)) {
							if(areNeighbors(i, j, k, l)) {
								neighs++;
							}
						}
					}
				neighbors[i][j] = neighs;
				}
			}
		}
		
		
		Board board  = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
		
	}
	
	public class Board extends JPanel {
		public void paintComponent(Graphics g) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 1050, 700);
			for(int i = 0; i < 16; i++) {
				for(int j = 0; j < 9; j++) {
					g.setColor(Color.GRAY);
					if(revealed[i][j] == true) {
						g.setColor(Color.WHITE);
						if(bombs[i][j] == 1) {
							g.setColor(Color.RED);
						}
					}
					if(mouseX >= spacing + (i * 65) && mouseX < spacing + (i * 65) + 65 - (2 * spacing) && mouseY >= spacing + (j * 65) + 65 + TOP_BAR && mouseY < spacing + (j * 65) + 65 + TOP_BAR + 65 - (2 * spacing)) {
						g.setColor(Color.GRAY);
					}
					g.fillRect(spacing + (i * 65), spacing + (j * 65) + 65, 65 - (2 * spacing), 65 - (2 * spacing));
					if(revealed[i][j] == true) {
						g.setColor(Color.BLACK);
						if(bombs[i][j] == 0 && neighbors[i][j] != 0) {
							if(neighbors[i][j] == 1) {
								g.setColor(Color.BLUE);
							}
							else if(neighbors[i][j] == 2) {
								g.setColor(Color.GREEN);
							}
							else if(neighbors[i][j] == 3) {
								g.setColor(Color.RED);
							}
							else if(neighbors[i][j] == 4) {
								g.setColor(new Color(0, 0, 128));
							}
							else if(neighbors[i][j] == 5) {
								g.setColor(new Color(178, 34, 34));
							}
							else if(neighbors[i][j] == 6) {
								g.setColor(new Color(72, 209, 204));
							}
							else if(neighbors[i][j] == 7) {
								g.setColor(Color.BLACK);
							}
							else if(neighbors[i][j] == 8) {
								g.setColor(Color.DARK_GRAY);
							}
							g.setFont(new Font("Sans Serif", Font.BOLD, 30));
							g.drawString(Integer.toString(neighbors[i][j]), (i * 65) + 25, (j * 65) + 65 + 45);
						}
						else if(bombs[i][j] == 1) {	
							g.fillRect((i * 65) + 10, (j * 65) + 80, 20, 40);
							g.fillRect((i * 65) + 10, (j * 65) + 80, 40, 20);
							g.fillRect((i * 65) + 10, (j * 65) + 80, 10, 10);
						}
					}
					
					//Placing flag value in a box
					if(flagged[i][j]) {
						g.setColor(Color.BLACK);
						g.fillRect((i * 65) + 30, (j * 65) + 70, 5, 40);
						g.fillRect((i * 65) + 10, (j * 65) + 70 + 35, 35, 15);
						g.setColor(Color.RED);
						g.fillRect((i * 65) + 15, (j * 65) + 70, 15, 15);
						g.setColor(Color.BLACK);
					}
				}
			}
			
			//Smile Value
			g.setColor(Color.YELLOW);
			g.fillOval(smileX, smileY, 50, 50);
			g.setColor(Color.BLACK);
			g.fillOval(smileX + 10, smileY + 10, 10, 10);
			g.fillOval(smileX + 30, smileY + 10, 10, 10);
			if(flagValue) {
				g.fillRect(smileX + 16, smileY + 37, 20, 5);
				g.fillRect(smileX + 14, smileY + 35, 5, 5);
				g.fillRect(smileX + 35, smileY + 35, 5, 5);
			}
			else {
				g.fillRect(smileX + 16, smileY + 35, 20, 5);
				g.fillRect(smileX + 14, smileY + 37, 5, 5);
				g.fillRect(smileX + 35, smileY + 37, 5, 5);
			}
			
			//Flag Value
			g.setColor(Color.BLACK);
			g.fillRect(flagX + 10, flagY, 5, 40);
			g.fillRect(flagX - 5, flagY + 35, 35, 15);
			g.setColor(Color.RED);
			g.fillRect(flagX - 5, flagY, 15, 15);
			g.setColor(Color.BLACK);
			
			if(pressedFlag) {
				g.setColor(Color.RED);
			}
			g.drawOval(flagX - 22, flagY - 4, 62, 62);
			g.drawOval(flagX - 21, flagY - 3, 60, 60);
		
		
			//Counter
			g.setColor(Color.BLACK);
			g.fillRect(timeX, timeY, 90, 60);
			if(!lose && !win) {
			seconds = (int) ((new Date().getTime() - startTime.getTime()) / 1000);
			}
			if(seconds > 999) {
				seconds = 999;
			}
			g.setColor(Color.WHITE);
			if(win) {
				g.setColor(Color.GREEN);
			}
			else if(lose) {
				g.setColor(Color.RED);
			}
			g.setFont(new Font("Sans Serif", Font.BOLD, 50));
			if(seconds < 10) {
				g.drawString("00" + Integer.toString(seconds), timeX + 5, timeY + 45);
			}
			else if(seconds < 100) {
				g.drawString("0" + Integer.toString(seconds), timeX + 5, timeY + 45);
			}
			else {
			g.drawString(Integer.toString(seconds), timeX + 5, timeY + 45);
			}
			
			//Game Over Message
			if(win) {
				g.setColor(Color.GREEN);
				message = "WINNNER!";
			}
			else if(lose) {
				g.setColor(Color.RED);
				message = "You exploded!";
			}
			
			if(win || lose) {
				winMessageY = -50 + (int) (new Date().getTime() - endTime.getTime()) / 10;
				if(winMessageY > 60) {
					winMessageY = 50;
				}
				g.drawString(message, winMessageX, winMessageY);
			}
			
		}
	}
	
	public class Move implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			
		}		
	}
	
	public class Click implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			mouseX = e.getX();
			mouseY = e.getY();
			
			if(insideBoxX() != -1 && insideBoxY() != -1) {
				if(pressedFlag && revealed[insideBoxX()][insideBoxY()] == false) {
					if(flagged[insideBoxX()][insideBoxY()] == false) {
						flagged[insideBoxX()][insideBoxY()] = true;
					}
					else {
						flagged[insideBoxX()][insideBoxY()] = false;
					}
				}
				else {
					if(!flagged[insideBoxX()][insideBoxY()])
					revealed[insideBoxX()][insideBoxY()] = true;
				}
			}
			else {}
			if(pressSmile()) {
				resetGame();
			}
			if(pressFlag()) {
				if(!pressedFlag) {
					pressedFlag = true;
				}
				else {
					pressedFlag = false;
				}
			}		
		}

		@Override
		public void mousePressed(MouseEvent e) {
		
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
		
	}
	
	public void checkWinner() {
		if(!lose) {
			for (int i = 0; i < 16; i++) {
				for(int j = 0; j < 9; j++) {
					if(revealed[i][j] && bombs[i][j] == 1) {
						lose = true;
						flagValue = false;
						endTime = new Date();
					}
				}
			}
		}
		if(totalBoxesRevealed() >= 144 - totalBombs() && !win) {
			win = true;
			endTime = new Date();
		}
	}
	
	public int totalBombs() {
		int total = 0;
		for (int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(bombs[i][j] == 1) {
					total++;
				}
			}
		}
		return total;
	}
	
	public int totalBoxesRevealed() {
		int total = 0;
		for (int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(revealed[i][j] == true) {
					total++;
				}
			}
		}
		return total;
	}
	
	public void resetGame() {
		pressedFlag = false;
		resetter = true;
		flagValue = true;
		win = false;
		lose = false;
		winMessageY = -50;
		startTime = new Date();
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(random.nextInt(100) < 20) {
					bombs[i][j] = 1;
				}
				else {
					bombs[i][j] = 0;
				}
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				neighs = 0;
				for(int k = 0; k < 16; k++) {
					for(int l = 0; l < 9; l++) {
						if(!(k == i && l == j)) {
							if(areNeighbors(i, j, k, l)) {
								neighs++;
							}
						}
					}
				neighbors[i][j] = neighs;
				}
			}
		}
		resetter = false;
	}
	
	public boolean pressSmile() {
		int difference = (int) Math.sqrt(Math.abs(mouseX - resetX)* Math.abs(mouseX - resetX) + Math.abs(mouseY - resetY) * Math.abs(mouseY - resetY));
		if(difference < 45) {
			return true;
		}
		return false;
	}
	
	public boolean pressFlag() {
		int difference = (int) Math.sqrt(Math.abs(mouseX - flagCenterX)* Math.abs(mouseX - flagCenterX) + Math.abs(mouseY - flagCenterY) * Math.abs(mouseY - flagCenterY));
		if(difference < 45) {
			return true;
		}
		return false;
	}
	
	public int insideBoxX() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(mouseX >= spacing + (i * 65) && mouseX < spacing + (i * 65) + 65 - (2 * spacing) && mouseY >= spacing + (j * 65) + 65 + TOP_BAR && mouseY < spacing + (j * 65) + 65 + TOP_BAR + 65 - (2 * spacing)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int insideBoxY() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 9; j++) {
				if(mouseX >= spacing + (i * 65) && mouseX < spacing + (i * 65) + 65 - (2 * spacing) && mouseY >= spacing + (j * 65) + 65 + TOP_BAR && mouseY < spacing + (j * 65) + 65 + TOP_BAR + 65 - (2 * spacing)) {
					return j;
				}
			}
		}
		return -1;
	}
	
	public boolean areNeighbors(int b1x, int b1y, int b2x, int b2y) {
		if(b1x - b2x < 2 && b1x - b2x > -2 && b1y - b2y < 2 && b1y - b2y > -2 && bombs[b2x][b2y] == 1) {
			return true;
		}
		return false;
	}
}
