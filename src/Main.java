
public class Main implements Runnable {
	
	GUI object = new GUI();
	
	public static void main(String [] args) {
		new Thread(new Main()).start();
	}
	
	@Override
	public void run() {
		while(true) {
			object.repaint();
			if(object.resetter == false) {
				object.checkWinner();
			}
		}
	}

}
