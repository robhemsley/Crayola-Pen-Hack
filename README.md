Crayola-Pen-Hack
================

Demo application showing capacitive communication between a hacked Crayola ColorStudio HD iMarker and an Android tablet. PenPaint application provides a touch based canvas drawing app that decodes colour codes sent from the Crayola Pen. 

Full build instructions can be found at http://www.robhemsley.co.uk

## Installation ##

* Android PenPaint App:
User defined variables for capacitive touch based comms can be found within the res/values/Strings.xml file.
<integer name="tap_timeout">1000</integer>	-  Timeout delay (ms) 
<integer name="binary_true">200</integer>	-  Binary True delay (ms)
<integer name="binary_false">100</integer>	-  Binary False delay (ms)
<integer name="tap_tollerance">40</integer>	-  Tap Tollerance delay (ms)


* Arduino App (Attiny 44):
	Uses Attiny 44 Arduino Library - Install instruction (http://hlt.media.mit.edu/?p=1695)

hello at robhemsley.co.uk

