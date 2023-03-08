# Inworld-Minecraft-SDK

This is a Java based project using VSCode for the.

Note: this documentation references specific version numbers such as 1.19.3 in file names. You may need to update those numbers with the latest version numbers.

## Requirements
+ Java
+ Maven
+ Git
+ Minecraft

## Setup

Note: This SDK is currently based on the 1.19.3 version of Minecraft. For different versions you will need to 

## Install OpenJDK 19+
+ [OpenJDK](https://jdk.java.net/19/)
+ [Installing OpenJDK on MacOSXs](https://www.codejava.net/java-se/install-openjdk-18-on-macos) 
+ [Installing OpenJDK on Windows](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)


Note the installation tutorials reference a previous version of OpenJDK. Please use the current one.

## Install Maven 3.9.0+
+ [Apache Maven](https://maven.apache.org/)
+ [Installing Maven on MacOSX](https://www.digitalocean.com/community/tutorials/install-maven-mac-os)
+ [Installing Maven on Windows](https://phoenixnap.com/kb/install-maven-windows)

Note the installation tutorials reference a previous version of Maven. Please use the current one.


## Install VSCode
+ [VSCode](https://code.visualstudio.com/)

#### Install the Following VSCode Extension(s)
+ [Extension Pack for Java]()


## Install Maincraft
+ [Minecraft Downloads](https://www.minecraft.net/en-us/download)

When you install the game make sure to use the Java Edition


+ [Minecraft Plugin Setup](https://www.spigotmc.org/wiki/creating-a-blank-spigot-plugin-in-vs-code/)

## Install Spigot Minecraft Server 1.19.3
+ [Spigot Minecraft Server Downloads](https://getbukkit.org/download/spigot#google_vignette)

Note that for this project we are using the 1.19.3 version. If you wish you use a different version you need to edit the ```pom.xml``` file located in ```inworld-plugin/``` directory found at the root of this project.

Once you download the spigot jar file copy it to the ```server/``` folder at the root of this project. If no folder exists then create one.

Open your terminal program and navigate to the ```server/``` folder. Type in the following command to start the server ```java -jar spigot-1.19.3.jar nogui``` The first time you start the server it will install itself and then shut down. You need to then open a newly created file ```eula.txt``` in a text editor and find the line ```eula=false``` Change that to ```eula=true``` save the file and close it then rerun ```java -jar spigot-1.19.3.jar nogui``` to launch the server.

To simplify the starting of the server you may wish to create a script file ```start.bat``` for windows or ```start.sh``` for Mac and Linux. 



## Building

In VSCode in the Explorer expand the MAVEN section then expand inworld-sdk->Lifecycle and run the ```package``` command. It will build the plugin and also copy the compiled file directly to the ```server\plugins``` folder. 