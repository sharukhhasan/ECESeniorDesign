import bluetooth
import time
import subprocess

IMAGE_PIN = 17;
SHUTDOWN_PIN = 27;


GPIO.setmode(GPIO.BCM)

GPIO.setup(IMAGE_PIN, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.add_event_detect(IMAGE_PIN, GPIO.RISING)
def image_callback():
    if GPIO.input(IMAGE_PIN):
        takeImage()
        sendPhotoToAndroid()
GPIO.add_event_callback(IMAGE_PIN, image_callback())



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
    camera.stop_preview()
	camera.capture('/home/pi/Desktop/image.jpg')


takeImage()
sendPhotoToAndroid()