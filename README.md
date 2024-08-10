# mIAuthd
 The midiandmore IAuthd<br>
 This is an early release of the IAuthd for snircd, it works.<br>
 To compile it, use NetBeans.<br>
 Then upload it to the server (Don't forget the libs)<br>
 Then just modify the ircd.conf and add or modify:<br>
 IAuth {<br>
   program = "java" "-jar" "path to mIAuthd.jar" "path to config.json";<br>
 };<br>
