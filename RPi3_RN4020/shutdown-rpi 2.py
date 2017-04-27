import RPi.GPIO as GPIO
import subprocess

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

button = 26
GPIO.setup(button, GPIO.IN)
GPIO.wait_for_edge(button, GPIO.FALLING)

subprocess.Popen(['sudo', 'halt'])