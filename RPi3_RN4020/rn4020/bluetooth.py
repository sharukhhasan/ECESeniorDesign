import RPi.GPIO as GPIO
import time
import serial
import os
import sys
import threading as thr

GPIO.setmode(GPIO.BCM)

WAKE_SW_PIN = 23
RX_PIN = 15
TX_PIN = 14



LOCK_MOVE = thr.Lock()

def init():
	GPIO.setup(WAKE_SW_PIN, GPIO.OUT)
	GPIO.setup(RX_PIN, GPIO.IN)
	GPIO.setup(TX_PIN, GPIO.OUT)


bluetoothSerial = serial.Serial(port = "/dev/ttyAMA0", baudrate = 115200, bytesize = EIGHTBITS, parity = PARITY_NONE, stopbits = STOPBITS_ONE, timeout = 0, writeTimeout = 0)
bluetoothSerial.open()

# Serial device setup
def serial_setup():
  global SER_PATH
  global ser_dev

  if os.path.exists(SER_PATH) == True:
    try:
      ser_dev = serial.Serial(SER_PATH, SER_RATE, timeout = 0)
      if ser_dev.isOpen():
        return True
    except serial.SerialException:
      ser_dev.close()
      sys.exit() 
  else:
    print SER_PATH + " does not exist"
    return False



