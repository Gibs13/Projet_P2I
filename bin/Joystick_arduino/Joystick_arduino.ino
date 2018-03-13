#include <Wire.h>
#define SLAVE_ADDRESS 0x04
#define inX A0 // analog input for x-axis
#define inY A1 // analog input for y-axis
#define inPressed 2 // input for detecting whether the joystick/button is pressed
int xValue = 0; // variable to store x value
int yValue = 0; // variable to store y value
int notPressed = 0; // variable to store the button's state => 1 if not pressed
int val, flag = 0;
byte data[5] = {0,0,0,0,0};

void setup()
{
  Serial.begin(9600); // start serial for output

  Wire.begin(SLAVE_ADDRESS);
  Wire.onReceive(receiveData);
  Wire.onRequest(sendData);

  pinMode(inX, INPUT); // setup x input
  pinMode(inY, INPUT); // setup y input
  pinMode(inPressed, INPUT_PULLUP); // we use a pullup-resistor for the button functionality

  Serial.println("Ready!");
}


void loop()
{
  xValue = analogRead(inX); // reading x value [range 0 -> 1023]
  yValue = analogRead(inY); // reading y value [range 0 -> 1023]
  notPressed = digitalRead(inPressed); // reading button state: 1 = not pressed, 0 = pressed
  
  data[0] = (byte)(xValue & 0xff);
  data[1] = (byte)((xValue >> 8) & 0xff);
  data[2] = (byte)(yValue & 0xff);
  data[3] = (byte)((yValue >> 8) & 0xff);
  data[4] = (byte) notPressed;
}

void receiveData(int byteCount)
{
  Serial.println("Received!");
  while (Wire.available() > 0)
  {
    val = Wire.read();  //On reçoit d'abord la longueur de la chaîne; cette fonction est appelée à nouveau avec le register

    Serial.println(val);
  }
}
// callback for sending data
void sendData()
{
  Serial.println("Request!");
  Wire.flush();
  Wire.write(data, sizeof(data));
}
