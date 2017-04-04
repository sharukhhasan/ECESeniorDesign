import RPi.GPIO as GPIO
import time
import serial
import os
import sys
import threading as thr


bluetoothSerial = serial.Serial("/dev/ttyAMA0", baudrate = 115200)

WAKE_SW_PIN = 23


LOCK_MOVE = thr.Lock()

def init():
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(WAKE_SW_PIN, GPIO.OUT)



# Serial device setup
def serial_setup():
  global SER_PATH
  global ser_dev 
  if os.path.exists(SER_PATH) == True:
    try:
      #timeout=0 turns on non-blocking mode
      ser_dev = serial.Serial(SER_PATH, SER_RATE, timeout = 0)
      if ser_dev.isOpen():
        return True
    except serial.SerialException:
      ser_dev.close()
      sys.exit() 
  else:
    print SER_PATH + " does not exist"
    return False



