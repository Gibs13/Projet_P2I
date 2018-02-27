import lejos .hardware.Button; import lejos .hardware.lcd .LCD;
public class TestButton {
public static void main(String [] args) {
LCD.drawString("Test Bouton", 0, 2);
String str="";
int id =0;
while(Button.ESCAPE.isUp() || Button.ENTER.isUp()){
// alternative : while( !Button.ESCAPE.isDown())
LCD.clear(4);
LCD.drawString(str, 4, 4);
Button.waitForAnyPress();
id = Button.getButtons(); // Pour récupérer l’ID du bouton
switch (id){
case 1 : str = "UP"; break;
case 2 : str = "ENTER"; break;
case 4 : str = "DOWN"; break;
case 8 : str = "RIGHT"; break;
case 16 : str = "LEFT"; break;
case 32 : str = "ESCAPE"; break;
}
}
}
}