import lejos.utility.TimerListener;

public class Robot implements TimerListener{
	
	final double dist = 0;
	
	int posX;
	int posY;
	double phi;
	double rayon;
	int nbBalle;
	
	public void timedOut() {
		
	}
	
	public void askPos (){
		this.posX = posX;
		this.posY = posY;
	}
	
	public void convert () {
		rayon = Math.sqrt (posX*dist*posX*dist+posY*dist*posY*dist);
		phi = Math.atan2 (posX, posY);
	}
	

}
