#define BLUETOOTH_BAUDRATE 9600
#define SHUTOFF_COMMAND 72	// ASCII character 'H'
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
	if(arduinoSerial.available()) {
		droidData = arduinoSerial.read();
		if(droidData == SHUTOFF_COMMAND) {
			arduinoSerial.println("Shut off command received. Shutting off device...");
			digitalWrite(pwr_switch_pin, HIGH);
		} else if (droidData == IMAGE_COMMAND) {
			arduinoSerial.println("Image command received. Capturing image...");
			digitalWrite(image_pin, HIGH);
		} else {
			arduinoSerial.print("Command: ");
			arduinoSerial.print(droidData, DEC);
			arduinoSerial.println(" --- INVALID COMMAND");

			digitalWrite(pwr_switch_pin, LOW);
			digitalWrite(image_pin, LOW);
		}
	}
	delay(100);
}