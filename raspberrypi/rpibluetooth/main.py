import bluetooth
import time
import serial
import subprocess

GPIO.setmode(GPIO.BCM)
GPIO.setup(11, GPIO.IN)
GPIO.setup(12, GPIO.OUT)


def sendPhotoToAndroid():
	
	sendPhoto = subprocess.Popen('ussp-push 14:1F:78:EB:8E:3C@12 /home/pi/Desktop/image.jpg piImage.jpg', shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
	output = sendPhoto.stdout.read()
	errors = sendPhoto.stderr.read()
	
	if 'connection established' in output.lower() and 'error' not in errors.lower():
		return True
	else:
		print 'There was an error:', '\033[91m', errors
		return False
		
		
		
def takeImage():
	camera.resolution = (600, 400)
	camera.start_preview()
	sleep(3)
	camera.capture('/home/pi/Desktop/image.jpg')
