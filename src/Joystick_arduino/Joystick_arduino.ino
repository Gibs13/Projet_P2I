#include <Wire.h>
#define SLAVE_ADDRESS 0x04
void setup()
{
  Serial.begin(9600); // start serial for output
  Wire.begin(SLAVE_ADDRESS);
  Wire.onReceive(receiveData);
  Wire.onRequest(sendData);
  Serial.println("Ready!");
}
int val, flag = 0;
void loop()
{
  if (flag == 1)
  {

    flag = 0;
  }
}
void receiveData(int byteCount)
{
  Serial.println("Received!");
  while (Wire.available() > 0)
  { ;
    val = Wire.read();  //On reçoit d'abord la longueur de la chaîne; cette fonction est appelée à nouveau avec le register

    Serial.println(val);
  }
}
// callback for sending data
void sendData()
{
  Serial.println("Request!");
  Wire.flush();
  Wire.write(0x46);
}
