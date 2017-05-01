#define BLUETOOTH_BAUDRATE 9600
#define SHUTOFF_COMMAND 72	// ASCII character 'H'
#define POWER_ON_COMMAND 74 // ASCII character 'J'
#define IMAGE_COMMAND 73	// ASCII character 'I'

#include <SoftwareSerial.h>

// Rx | Tx
SoftwareSerial arduinoSerial (10, 11);
int droidData;

int pwr_switch_pin = 12;
int image_pin = 5;

void setup() {
	arduinoSerial.begin(BLUETOOTH_BAUDRATE);
	arduinoSerial.println("Bluetooth ON");

	pinMode(pwr_switch_pin, OUTPUT);
	pinMode(image_pin, OUTPUT);
}

void loop() {
  boolean power_flag = false;

  if(power_flag) {
    digitalWrite(pwr_switch_pin, HIGH);
  }
  
	if(arduinoSerial.available()) {
		droidData = arduinoSerial.read();
		if(droidData == SHUTOFF_COMMAND) {
      power_flag = true;
			arduinoSerial.println("Shut off command received. Shutting off device...");
			digitalWrite(pwr_switch_pin, HIGH);
		} else if (droidData == IMAGE_COMMAND) {
			arduinoSerial.println("Image command received. Capturing image...");
			digitalWrite(image_pin, HIGH);
		} else {
      power_flag = false;
			//digitalWrite(pwr_switch_pin, LOW);
			//digitalWrite(image_pin, LOW);
		}
	}
	delay(100);
}
