import base64
import re
import subprocess
import pygtk
pygtk.require('2.0')
import gtk
import gtk.gdk
import gobject
from gimpfu import *

def plugin_main(img) :
    file = img.filename

    if not file:
      message = gtk.MessageDialog(type=gtk.MESSAGE_ERROR, buttons=gtk.BUTTONS_OK)
      message.set_markup("Please save your file first!")
      message.run()

    (prefix, sep, suffix) = file.rpartition('.')

    output_file = prefix + '.base64'

    opened_file = open(output_file, "w")
    base64_f = base64.b64encode(open(file, "rb").read())
    opened_file.write(base64_f)

    for clip_target in [gtk.gdk.SELECTION_PRIMARY, gtk.gdk.SELECTION_CLIPBOARD]:
      clipboard = gtk.clipboard_get(clip_target)
      clipboard.set_can_store(None)
      clipboard.set_text(base64_f, -1)
      clipboard.store()

    gtk.main()

    opened_file.close()

register(
    "base64_file_encoder",
    "Base64 encoding",
    "Encodes current image to base64 and stores it in a text file",
    "Sharukh Hasan <sharukh-hasan@uiowa.edu>",
    "Marcin Karpezo <marcin@karpezo.pl>",
    "2015",
    "Encode to base64",
    "*",
    [
      (PF_IMAGE, "image", "Input image", None),
    ],
    [],
    plugin_main,
    menu = "<Image>/File/Save/"
)

main()