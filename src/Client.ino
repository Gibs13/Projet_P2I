#include <Bridge.h>
#include <YunServer.h>
#include <YunClient.h>

#include <Wire.h>
#define SLAVE_ADDRESS 0x04
#define inX A0 // analog input for x-axis
#define inY A1 // analog input for y-axis
#define inPressed 2 // input for detecting whether the joystick/button is pressed
int xValue = 0; // variable to store x value
int yValue = 0; // variable to store y value
int notPressed = 0; // variable to store the button's state => 1 if not pressed
int val, flag = 0;
byte data0[5] = {0,0,0,0,0};

int LEDPIN = 3; // your LED PIN

IPAddress ip(192,168,43,242);
//IPAddress ip(192,168,43,94);
const int port = 5001;
BridgeServer server(port);
BridgeClient client;
char data[1000];
int ind=0;

static boolean clientActive = false;
String incomingByte;

void setup() {
 
  // Start our connection
  Serial.begin(9600);
  Bridge.begin();  

/*
  // Show a fancy flash pattern once connected
  digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);                       // wait for a second
  digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);                       // wait for a second
*/  
  // Disable for some connections:
  // Start listening for connections  

//  server.begin();
  Wire.begin(SLAVE_ADDRESS);
  Wire.onReceive(receiveData);
  Wire.onRequest(sendData);
  pinMode(inX, INPUT); // setup x input
  pinMode(inY, INPUT); // setup y input
  pinMode(inPressed, INPUT_PULLUP); // we use a pullup-resistor for the button functionality
  client=server.accept();
  client.connect(ip,port);
//  while (!Console);
}

void loop() 
{
  xValue = analogRead(inX); // reading x value [range 0 -> 1023]
  yValue = analogRead(inY); // reading y value [range 0 -> 1023]
  notPressed = digitalRead(inPressed); // reading button state: 1 = not pressed, 0 = pressed
  
  data0[0] = (byte)(xValue & 0xff);
  data0[1] = (byte)((xValue >> 8) & 0xff);
  data0[2] = (byte)(yValue & 0xff);
  data0[3] = (byte)((yValue >> 8) & 0xff);
  data0[4] = (byte) notPressed;
  
  client=server.accept();
  if (client.connected())   // Returns true if the client is connected, false if not
  { 
    Console.println("Conected");
//    client.println("Ping !");
    Console.println("Envoye");
/*    if (!clientActive)
    Console.println("New Client connection.");
    clientActive = true;
    */
    if (client.available()>=0) 
    { // Returns the number of bytes available for reading
//     incomingByte == client.read(); // Read the next byte recieved from the server the client is connected to
//     Console.print("From client: \"");
//     Console.print(incomingByte);
//     Console.println("\"");
       while (client.available())
       {
        char myChar = client.read();
        Console.println(myChar);
        data[ind] = myChar;
        ind++;
       }
     String str(data);
     int phi = str.toInt();
     Console.println(str);
     Console.println(phi);
     ind = 0;   
     client.flush();

/*      for(int j=0;j < ind; j++)
      {
        Console.print(data[j]);
      }
      */
    }
  }
else 
  {
    Console.println("Else");
    if (clientActive) 
    {
      client.stop();       // Disconnected from the server
      Console.println("Client disconnected.");
    }
    client.connect(ip,port);  // Connects to a specified IP address and port
    client = server.accept();
    clientActive=false;
    Console.println("clientActive = false");
  }
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
  Wire.write(data0, sizeof(data0));
}

/*
void loop() {
// Attente d'un client
//  client = server.accept();
client.connect(ip,port);


  if(client.connect(ip,port))
  {
    client.println("Ping !");
    Console.println("aa");

  }
  
  if(client.available()>0)
    { 
      Console.println("J'ai un client");
      Console.println(client.read());
      while(client.available())
      {
        Console.println(""+client.read());
        Console.println("aa");
//        char myChar = client.read();
//        data[ind] = myChar;
//        ind++;
      } 
      client.flush();
      //Serial.println(data);
      for(int j=0;j < ind; j++)
      {
        Console.print(data[j]);
      }
      ind = 0;
      client.print("Ok!");     
    
   
    }
  client.stop();
// delay(500);
  
}
*/

/*
void process(BridgeClient client) {
  // Collect user commands
 String command = client.readStringUntil('\\'); // load whole string
//   String* command = client.read();

  // Enable HTML
  client.println("Status: 200");
  client.println("Content-type: text/html");
  client.println();
  
  // Show UI
  client.println("<B><Center>");
  client.println("<a href='http://yun.local/arduino/on\\'>Turn ON LED</a><br>");
  client.println("<a href='http://yun.local/arduino/off\\'>Turn OFF LED</a><br>");
  client.print("Command: ");
  client.println(command);
  client.println("</B></Center>");

  // Check what the user entered ...
  
  // Turn on
  if (command.equals("Pong !")) {
    digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
    delay(1000);                       // wait for a second
    digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
    delay(1000);                       // wait for a second
  }

}*/
