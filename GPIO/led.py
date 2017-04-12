from RPi.GPIO as GPIO
from time import sleep

led = LED(18)

while True:
	led.on()
	sleep(1)
	led.off()
	sleep(1)
