# Inworld-Minecraft-SDK

This project is the Inworld.AI integration for Minecraft Java Edition. It is a plugin to be used with Spigot Minecraft server.

Note: this documentation references specific version numbers for both Minecraft Java and Spigot server. The latest at this time is ```1.19.4```. You may need to update to the latest version numbers. Instructions are detailed below on how to do that.
<br/><br/>
***
<br/> 

## Table of Contents

1. [Requirements](#requirements)
2. [Setup](#installation)
    1. [Install OpenJDK 19+](#setup-java)
    2. [Install Maven 3.9.0+](#setup-maven)
    3. [Install VSCode](#setup-vscode)
    4. [Install Maincraft Java Edition](#setup-minecraft)
    5. [Install Spigot Minecraft Server](#setup-spigot)
    6. [Install Inworld REST API Proxy](#setup-rest)
3. [Building](#building)
4. [Running](#running)
5. [Character Management Commands](#commands)
<br/><br/>
***
<br/> 

## <a id="requirements" name="requirements"></a>Requirements

+ [OpenJDK 19+](#setup-java)
+ [Maven 3.9.0+](#setup-maven)
+ [VSCode](#setup-vscode)
+ [Maincraft Java Edition](#setup-minecraft)
+ [Spigot Minecraft Server](#setup-spigot)
+ [Inworld REST API Proxy](https://docs.inworld.ai/docs/tutorial-integrations/node-rest/)
<br/><br/>
***
<br/> 

## <a id="setup" name="setup"></a>Setup

### <a id="setup-java" name="setup-java"></a>Install OpenJDK 19+
+ [OpenJDK](https://jdk.java.net/19/)
+ [Installing OpenJDK on MacOSXs](https://www.codejava.net/java-se/install-openjdk-18-on-macos) 
+ [Installing OpenJDK on Windows](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note: The installation tutorials reference a previous version of OpenJDK. When following the instructions please ignore the version they list and use 19+.
<br/><br/>

### <a id="setup-maven" name="setup-maven"></a>Install Maven 3.9.0+
+ [Apache Maven](https://maven.apache.org/)
+ [Installing Maven on MacOSX](https://www.digitalocean.com/community/tutorials/install-maven-mac-os)
+ [Installing Maven on Windows](https://phoenixnap.com/kb/install-maven-windows)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note: The installation tutorials reference a previous version of Maven. When following the instructions please ignore the version they list and use 3.9.0+.
<br/><br/>

### <a id="setup-vscode" name="setup-vscode"></a>Install VSCode
+ [VSCode](https://code.visualstudio.com/)
<br/><br/>

#### Install the Following VSCode Extension(s)
+ [Extension Pack for Java]()
<br/><br/>

### <a id="setup-minecraft" name="setup-minecraft"></a>Install Maincraft Java Edition
+ [Minecraft Downloads](https://www.minecraft.net/en-us/download)
<br/><br/>

### <a id="setup-spigot" name="setup-spigot"></a>Install Spigot Minecraft Server 1.19.4
+ [Spigot Minecraft Server Downloads](https://getbukkit.org/download/spigot#google_vignette)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note: For this project we are using the 1.19.4 version. If a later version is needed please see the instructions below for updating the project to the latest. The commands listed can have the version number changed to the latest.

Once you download the Spigot jar file copy it to the ```server/``` folder at the root of this project. If the folder doesn't exist then create it.

Open your terminal program and navigate to the ```server/``` folder. Type in the following command to start the server ```java -jar spigot-1.19.4.jar nogui``` The first time you start the server it will install itself and then shut down. You need to then open a newly created file ```eula.txt``` in a text editor and find the line ```eula=false``` Change that to ```eula=true``` save the file and close the file.
<br/>

#### Updating Spigot Server to Newer Version

This plugin is based on Spigot 1.19.4 which was the latest at the time of it's release. If a newer version is needed open the ```pom.xml``` file located at ```inworld-plugin/pom.xml``` from the root of this project. Scroll down to the ```<dependencies>``` node and find the ```<dependency>``` with the groupId ```<groupId>org.spigotmc</groupId>```. Update the version node ```<version>1.19.4-R0.1-SNAPSHOT</version>``` to represent the latest version number and build the plugin following the instructions below.
<br/><br/>
***
<br/> 

### <a id="setup-spigot" name="setup-rest"></a>Install Inworld REST API Proxy

The Inworld Minecraft integration uses the Inworld REST API Proxy to process conversations with characters. Installation and setup documentation for it can be found [here](https://docs.inworld.ai/docs/tutorial-integrations/node-rest/).
<br/><br/>
***
<br/> 


## <a id="building" name="building"></a>Building

In VSCode in the Explorer pane, expand the MAVEN section then expand inworld-sdk->Lifecycle and run the ```package``` command. It will build the plugin and also copy the compiled file directly to the ```server\plugins``` folder. 
<br/><br/>
***
<br/> 

## <a id="running" name="running"></a>Running

Open your terminal program and navigate to the ```server/``` folder. Type in the following command to start the server ```java -jar spigot-1.19.4.jar nogui``` to start the server.

To simplify the starting of the server you may wish to create a script file ```start.bat``` for Windows or ```start.sh``` for Mac and Linux. Create the file in the ```server/``` folder and paste the ```java -jar spigot-1.19.4.jar nogui``` command into it. Save and close the file. For Mac and Linux you need to set the file as executable by running ```

If you modify the plugin you can [build](#building) it while the server is running. In the terminal window running the Spigot server type in the command ```reload``` and this will reload the plugin. Note: Doing this clears any open sessions.
<br/><br/>
***
<br/> 

## <a id="commands" name="commands"></a>Character Management Commands

This plugin uses a custom set of Minecraft commands to manage scenes, characters and configure the hosts to the Inworld REST API Proxy. These commands are issued by entering ```/inworld``` into the chat window. The commands are tab autocompletion ready to make things easier. Below are a list of the commands you can issue 

### API - These commands allow you to set, remove and list the hosts to your Inworld REST API Proxy servers. There are two API hosts available. ```dev``` - Which is used when you are running the server locally and ```prod``` which is used when the server is being run remotely.
+ #### Commands
```/inworld api [dev|prod] [host]``` - This c