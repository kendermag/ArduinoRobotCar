#include <WiFi.h>
#include <Adafruit_PWMServoDriver.h>
#include <Wire.h>

const int MIN_PULSE_WIDTH = 600;
const int MAX_PULSE_WIDTH = 2600;
const int FREQUENCY = 50;

// L298N pins
const int IN1 = 27; 
const int IN2 = 26; 
const int ENA = 14; 

// ESP32 specific PWM setup 
const int freq = 100;
const int pwmChannel = 5;
const int resolution = 10; // this determines the pwm range, is specified in bits, 10 bits mean 0-1023, 8 would mean 0-255 etc.

// WiFi network name and password:
// substitute with the appropriate network properties where it is used
const char* ssid = "network_name";
const char* password = "network_password";

WiFiServer wifiServer(80);

Adafruit_PWMServoDriver pca9685 = Adafruit_PWMServoDriver(0x40);

void setup()
{

  //l298n setup
  pinMode(ENA, OUTPUT);
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);

  // L298N PWM setup
  ledcSetup(pwmChannel, freq, resolution);
  ledcAttachPin(ENA, pwmChannel);

  // Serial monitor setup
  Serial.begin(115200);

  // Initialize PCA9685
  pca9685.begin();
  // Set PWM Frequency to 50Hz
  pca9685.setPWMFreq(FREQUENCY);
  
  WiFi.begin(ssid, password);

  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());

  delay(2000);
  
  wifiServer.begin();
  
}

void loop() {

  Serial.println("In the loop");
   
  WiFiClient client = wifiServer.available();
 
  if (client) {
    
    while (client.connected()) {
      
      bool flagF = false;
      bool flagB = false;
      bool flagL = false;
      bool flagR = false;

      // stop main engine
      bool flagX = false;

      // turn back to middle
      bool flagM = false;

      // client turnoff
      bool flagSTOP = false;

      Serial.println("Client is connected!");
      
      while (client.available()>0) {

        int num_ = client.read();
        char ch_ = num_;

        ////////// FORWARD
        if(ch_ == 'F'){
          flagF = true;
        }        
        if(flagF){
          if(ch_ == 'W'){
            Serial.println("Forward!");
            forwardAcceleration();
            flagF = false;
          }  
        }

        ////////// BACKWARD
        if(ch_ == 'B'){
          flagB = true;
        }        
        if(flagB){
          if(ch_ == 'W'){
            Serial.println("Backward!");
            backwardAcceleration();
            flagB = false;
          }  
        }

        ////////// DISCONNECT
        if(ch_ == 'D'){
          flagSTOP = true;
        }        
        if(flagSTOP){
          if(ch_ == 'C'){
            goto STOP;
          }  
        }

        ////////// LEFT
        if(ch_ == 'L'){
          flagL = true;
        }
        if(flagL){
          if(ch_ == 'T'){
            Serial.println("Left!");
            turnByDegree(60);
            flagL = false;
          }
        }

        ////////// RIGHT
        if(ch_ == 'R'){
          flagR = true;
        }
        if(flagR){
          if(ch_ == 'T'){
            Serial.println("Right!");
            turnByDegree(130);
            flagR = false;
          }
        }

        ////////// SATU = STOP MOTOR
        if(ch_ == 'X'){
          flagX = true;  
        }
        if(flagX){
          if(ch_ == 'Z'){
            satu();
          }
        }

        ////////// STEER BACK TO MIDDLE 
        if(ch_ == 'M'){
          flagM = true;
        }
        if(flagM){
          if(ch_ == 'D'){
            turnBackMiddle();
          }  
        }
        
        Serial.print(ch_);
      }
    }

    STOP:
    //// if client is killed
    client.stop();

    Serial.println("Client disconnected");

    delay(100);
  }
}


void turnByDegree(int degree){
  /* here i set up the desired angle*/
  pca9685.setPWM(0, 0, pulseWidth(degree));
  /* i give some time for the servo to setup, this depends on voltage, and the servo itself */
  delay(150);

}

void turnBackMiddle(){
  /* in my case, the "100" given to the pulseWidth means middle, it depends on the setup */
  pca9685.setPWM(0, 0, pulseWidth(100));
  delay(150);
}



/* this fn. returns an appropriate analog value according to the given angle
   asszem full felesleges TODO-megoldani enélkül 
*/
int pulseWidth(int angle){
  int pulse_wide, analog_value;
  pulse_wide = map(angle, 0, 180, MIN_PULSE_WIDTH, MAX_PULSE_WIDTH);
  analog_value = int(float(pulse_wide) / 1000000 * FREQUENCY * 4096);
  return analog_value;
}

void forwardAcceleration(){
  // motor turn ON
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);

  ledcWrite(pwmChannel, 600);
}

void backwardAcceleration(){
  // motor turn ON
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);

  ledcWrite(pwmChannel, 600);
}

void satu(){
  ledcWrite(pwmChannel, 0);
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
}
