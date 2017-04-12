from gpiozero import LED, Button
from signal import pause

led = LED(12)
button = Button(16)

button.when_pressed = led.on
button.when_released = led.off

pause()
