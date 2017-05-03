#define BLUETOOTH_BAUDRATE 9600
#define SHUTOFF_COMMAND 72	// ASCII character 'H'
#define POWER_ON_COMMAND 74 // ASCII character 'J'
#define IMAGE_COMMAND 73	// ASCII character 'I'
#define BATTERY_COMMAND 75 // ASCII character 'K'

#include <SoftwareSerial.h>

// Rx | Tx
SoftwareSerial arduinoSerial (10, 11);

int droidData;
int piData;

int pwr_switch_pin = 12;
int image_pin = 5;

const float voltage = 6.64;
const int measurepin = 0;

void setup() {
	arduinoSerial.begin(BLUETOOTH_BAUDRATE);
  Serial.begin(9600);
  
	pinMode(pwr_switch_pin, OUTPUT);
  digitalWrite(pwr_switch_pin, HIGH);
  
	pinMode(image_pin, OUTPUT);
  digitalWrite(image_pin, LOW);
}

void loop() {
  boolean power_off_flag = true;

  if(power_off_flag) {
    digitalWrite(pwr_switch_pin, HIGH);
  }
  
	if(arduinoSerial.available()) {
		droidData = arduinoSerial.read();
   
		if(droidData == SHUTOFF_COMMAND) {
      power_off_flag = false;
			digitalWrite(pwr_switch_pin, LOW);
		} 
		else if (droidData == IMAGE_COMMAND) {
      Serial.write("I");
			digitalWrite(image_pin, HIGH);
      delay(100);
      digitalWrite(image_pin, LOW);
		} 
		else if(droidData == POWER_ON_COMMAND) {
		  power_off_flag = true;
      digitalWrite(pwr_switch_pin, HIGH);
		} 
		else if(droidData == BATTERY_COMMAND) {
      batteryLevel();
		}
	}
	delay(100);
}

void batteryLevel() {
  int val = analogRead(measurepin);
  float volts = (val/1023.0)*voltage;
  float battery_life = (volts*1.5)/(voltage*0.93);
  int returnVal = battery_life * 100;
  arduinoSerial.print(returnVal);
}