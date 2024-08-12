# mIAuthd from Andreas Pschorn<br>
Attention: It's recommenend to use NetBeans to edit and compile the sources.<br>
<br>
First download following file, and copy it to the server: https://github.com/user-attachments/files/16578870/mIAuthd.zip<br>
Extract the zip to the server and make sure that you use jre17 or higher.<br>
Modify the config.json.<br>
[snircd](https://github.com/quakenet/snircd) needs for [qwebirc](https://github.com/qwebirc/qwebirc) the iauth-fix-webirc.patch and the configure.patch file,<br>
Go to the snircd sources directory, and enter<br>
"wget https://raw.githubusercontent.com/WarPigs1602/snircd-patches/main/iauth-fix-webirc.patch",<br>
then enter "git apply iauth-fix-webirc.patch" to fix the server with an IAuth bug and add the webchat feature,<br>
then enter "wget https://raw.githubusercontent.com/WarPigs1602/snircd-patches/main/configure.patch",
then enter "git apply configure.patch" to fix the MAXCONNECTIONS issue.
After that then enter "./configure", then "make" and "make install",<br>
and the ircd will now working with the Patch.<br>
Further configuration in the "ircd.conf" is needed or modified to:<br>
<br>
IAuth {<br>
 program = "java" "-jar" "path to mIAuthd.jar" "path to config.json";<br>
};<br>
<br>
And set in the features section:<br>
<br>
"HIS_STATS_IAUTH" = "TRUE";<br>
<br>
Have fun for the using of this program :)
