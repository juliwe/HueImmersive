_**HueImmersive is no longer in development and currently out of date**_

## Description
HueImmersive is an application that tries to generate the ambient light from your screen with the Philips hue lights. It should create a similar atmosphere, as in a picture, movie or game that your screen displays.

If you’ve found a bug or have a problem create an [issue](https://github.com/Blodjer/HueImmersive/issues/new) or contact me via [Email](mailto: mail@blodjer.com) or [Reddit](https://www.reddit.com/message/compose/?to=Blodjer). Also feel free to send me suggestions and feedback.
You can also download or fork this project to make it even better. Any help is welcome!
 
If you you want to support me with a donation: [Thank you!](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=BVVY8L9TTPQFJ)

<br>
## How To Use
When you start HueImmersive.jar for the first time you must press the link button on your bridge to register a new profile. If the program has successfully connected the 'control' window pops up.

One important thing here is the 'chunk' slider. It determines how fine or coarse the 'color grid' should be. For example if you watch a movie with small objects I recommend to use more chunks so that the chunks are small enough to get a clear color from that object. If you watch something with large colored surfaces like landscapes you should use less chunks. I got the best results with 70-170 chunk (it has no effect on performance). To make it easier you can check 'show color grid'. This will open a new window that shows you the color grid resolution.

You can also set the brightness and aspect ratio for the screen capture. Advanced options are available under the menu settings -> options. There you can set individual settings for each light. Also you can set which extracted color (average, bright, dark, saturated) should be sent to a light.

### Requirements (User)
* HueImmersive.jar: https://github.com/Blodjer/HueImmersive/releases
* Java: https://www.java.com/download/index.jsp
* Moderately Computer

##### Tested Platforms:
* Windows
* OS X

### Requirements (Developer)
* HueImmersive Development Branch: https://github.com/Blodjer/HueImmersive/tree/develop
* Java Development Kit: http://www.oracle.com/technetwork/java/javase/downloads/index.html

##### Libraries:
* Gson: http://repo1.maven.org/maven2/com/google/code/gson/gson/
* JGoodies Common: http://www.jgoodies.com/downloads/archive/
* JGoodies Forms: http://www.jgoodies.com/downloads/archive/

<br>
## How It Works
The application takes several shots in the second of your screen. To get a good result the program pixelate each screenshot into a color grid.
<br> The color grid looks similar to this:
<br><img src="http://i.imgur.com/NW88UgE.png" width="550px"/>

In the further process the program will analyze these chunks and extract specific colors. Actually available are the average color, bright and dark areas and the most saturated color. Each light can pick one of these extracted colors.

<br>
-
### Additional  Links
* Philips hue: http://www.meethue.com
* Reddit discussion (archived): http://redd.it/2e3vq9

-
###### *<sub>sorry for my english</sub>*
