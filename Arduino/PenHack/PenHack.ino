/*
  PenHack - Crayola ColorStudio
 
  Hacked code to transmit data to a tablet through capcitive touch
  Code will send 3 possible bytes of data which represent Red, green and blue.
   
  Pins: 
    red = 8
    green = 7
    blue  = 6
    relay = 9
    
  hello@robhemsley.co.uk
*/
 
 
int rgb_pins[] = {8, 7, 6};    //RGB Pins for Pen
int cap_pin = 9;               //Relay Pin

int count = 0;
int colour_iter = 0;

int colour = 0;
int bits[] = {100, 100, 100, 100, 100, 100, 100, 100};

void setup() {               
  pinMode(cap_pin, OUTPUT);    
  reset_rgb();
  
  for (int i = 0; i < 15; i++){
    if (i <= 5 || i >= 10){
      pinMode(i, OUTPUT);
      digitalWrite(i, HIGH);
    }  
  }
}

void reset_rgb(){
  for (int i = 0; i < sizeof(rgb_pins)/2; i++){
    pinMode(rgb_pins[i], OUTPUT);
    digitalWrite(rgb_pins[i], HIGH);   // set the LED on
  } 
}

void loop() {
  
  // Pulse the current byte 
  digitalWrite(cap_pin, HIGH);
  delay(bits[count]);            //Delay binary true or false
  digitalWrite(cap_pin, LOW);
  delay(50);                     //Delay for relay to fall
  count += 1;                    //Count byte
  
  // Run when bytes been sent
  if (count == sizeof(bits)/2){
    count = 0; 
    digitalWrite(cap_pin, HIGH);
    delay(250);
    digitalWrite(cap_pin, LOW);
    delay(50);
    colour_iter += 1;
  }
  
  //Check to see if the code has been sent 10 times
  if(colour_iter % 10 == 0){
    reset_rgb();
    digitalWrite(rgb_pins[colour], LOW);
    
    //Set the byte array based on the current colour
    switch(colour){
      case 0:
        bits[0] = 100;
        bits[1] = 100;
        bits[2] = 100;
        bits[3] = 100;
        bits[4] = 100;
        bits[5] = 100;
        bits[6] = 100;
        bits[7] = 200;
        break;
      case 1:
        bits[0] = 100;
        bits[1] = 100;
        bits[2] = 100;
        bits[3] = 100;
        bits[4] = 100;
        bits[5] = 100;
        bits[6] = 200;
        bits[7] = 200;
        break;
      case 2:
        bits[0] = 100;
        bits[1] = 100;
        bits[2] = 100;
        bits[3] = 100;
        bits[4] = 100;
        bits[5] = 100;
        bits[6] = 200;
        bits[7] = 100;
        break;
    }
    
    colour += 1;
    if (colour == 3){
      colour = 0;
    }
  }
}
