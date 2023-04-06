# Inworld-Minecraft-SDK

This project is the Inworld.AI integration for Minecraft Java Edition. It is a plugin to be used with Spigot Minecraft server. 

This tutorial documents the development and building the plugin however if you already have a Minecraft server and wish to only deploy the plugin, you may skip to the [Deploying](#deploying) section of this documentation.

Note: this documentation references specific version numbers for both Minecraft Java and Spigot server. The latest at this time is ```1.19.4```. You may need to update to the latest version numbers. Instructions are detailed below on how to do that.
<br/><br/>
***
<br/> 

## Table of Contents

1. [Development Requirements](#requirements)
2. [Development Setup](#installation)
    1. [Install OpenJDK 19+](#setup-java)
    2. [Install Maven 3.9.0+](#setup-maven)
    3. [Install VSCode](#setup-vscode)
    4. [Install Maincraft Java Edition](#setup-minecraft)
    5. [Install Spigot Minecraft Server](#setup-spigot)
    6. [Install Inworld REST API Proxy](#setup-rest)
3. [Building](#building)
4. [Running](#running)
5. [Deploying](#deploying)
6. [SDK Admin Commands](#commands)
    1. [API Host Commands](#commands-api)
    2. [Inworld Scene Commands](#commands-scene)
    3. [Inworld Character Commands](#commands-character)
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
Note: The following installation tutorials reference a previous version of OpenJDK. When following the instructions please ignore the version they list and use 19+.

+ [OpenJDK](https://jdk.java.net/19/)
+ [Installing OpenJDK on MacOSX](https://www.codejava.net/java-se/install-openjdk-18-on-macos) 
+ [Installing OpenJDK on Windows](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)
<br/><br/>

### <a id="setup-maven" name="setup-maven"></a>Install Maven 3.9.0+
Note: The following installation tutorials reference a previous version of Maven. When following the instructions please ignore the version they list and use 3.9.0+.
+ [Apache Maven](https://maven.apache.org/)
+ [Installing Maven on MacOSX](https://www.digitalocean.com/community/tutorials/install-maven-mac-os)
+ [Installing Maven on Windows](https://phoenixnap.com/kb/install-maven-windows)
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

Open your terminal program and navigate to the ```server/``` folder you installed the Spigot server into. Type in the following command to start the server ```java -jar spigot-1.19.4.jar nogui``` to start the server.

To simplify the starting of the server you may wish to create a script file ```start.bat``` for Windows or ```start.sh``` for Mac and Linux. Create the file in the ```server/``` folder and paste the ```java -jar spigot-1.19.4.jar nogui``` command into it. Save and close the file. For Mac and Linux you will need to set the file as executable by running this command in your terminal window: ```chmod +x start.sh```. Examples of these scripts can be located in the ```scripts/``` folder located at the root of this project. You can copy them into the ```server/``` folder but if a newer version from ```1.19.4``` has been released since the last release of this documention you will need to update them to reflect the latest version number.

If you modify the plugin you can [build](#building) it while the server is running. In the terminal window running the Spigot server type in the command ```reload``` hit enter and this will reload the plugin. Note: Doing this closes any open sessions.
<br/><br/>
***
<br/> 

## <a id="deploying" name="deploying"></a>Deploying

To deploy the plugin to an existing server you can choose a prebuilt version ```inworld-sdk-1.19.4.jar``` contained within the ```builds/``` folder located at the root of this project or if you wish to deploy one you've built yourself you may location the build within the ```server/plugins``` folder located at the root of this project.

Note: After you add the plugin to the server you will need to either reload or restart it for it to activate.
<br/><br/>
***
<br/> 

## <a id="commands" name="commands"></a>SDK Admin Commands

This plugin uses a custom set of Minecraft commands to manage scenes, characters and configure the API hosts to the Inworld REST API Proxy. These commands are issued by entering ```/inworld``` into the chat window. The commands are tab autocompletion ready to make things easier. 

Note: With the API host configuration there are two options, `dev` and `prod`. The `dev` host is used when no IP address have been entered into the Spigot configuration file ```server.properties``` located in the ```server/``` folder at the root of this project. Within the file if the ```server-ip=``` property has been set this plugin will use the `prod` API host. If it left blank it will use the `dev` API host. If you are using a server rental service the IP may be set automatically.

Below are a list of the commands you can issue using the internal Minecraft Java chat window:

### <a id="commands-api" name="commands-api"></a>API - These commands allow you to set, remove and list the hosts to your Inworld REST API Proxy servers. 
`set`&nbsp;&nbsp;&nbsp;Syntax ```/inworld api set [dev|prod] [host]``` - This command sets the API host for either dev or prod to the Inworld API REST service.
`list`&nbsp;&nbsp;&nbsp;Syntax ```/inworld api list``` - This command lists the API hosts for both `dev` and `prod`.<br/>
`clear`&nbsp;&nbsp;&nbsp;Syntax ```/inworld api clear [dev|prod]``` - This command will remove the API host set for either `dev` or `prod`.<br/>

### <a id="commands-scene" name="commands-scene"></a>Scene - These commands allow you to set, remove and list the hosts to your Inworld REST API Proxy servers. 
`add`&nbsp;&nbsp;&nbsp;Syntax ```/inworld scene add [sceneId]``` - This command adds an Inworld scene to the Minecraft server. `[sceneId]`.<br/>
`characters`&nbsp;&nbsp;&nbsp;Syntax ```/inworld scene characters [sceneId]``` - This command lists the Inworld characters within a `[sceneId]`.<br/>
`list`&nbsp;&nbsp;&nbsp;Syntax ```/inworld scene list``` - This command lists all the Inworld scenes that have been added to the Minecraft server.<br/>
`remove`&nbsp;&nbsp;&nbsp;Syntax ```/inworld scene remove [sceneId]``` - This command will remove an Inworld scene from the Minecraft server.<br/>

### <a id="commands-character" name="commands-character"></a>Character - These commands allow you to set, remove and list the characters to the Minecraft game that will add them as Villagers. It also allows you to toggle a character from being stationary vs have Minecraft Villager AI enabled.
`add`&nbsp;&nbsp;&nbsp;Syntax ```/inworld character add [character]``` - This command adds an Inworld scene to the Minecraft server. The `[character]` id will be tab autocompleted.<br/>
`list`&nbsp;&nbsp;&nbsp;Syntax ```/inworld character list``` - This command lists all the Inworld character that have been added to the Minecraft server.<br/>
`remove`&nbsp;&nbsp;&nbsp;Syntax ```/inworld character remove [character]``` - This command will remove an Inworld character from the Minecraft server.<br/>
`toggleAware`&nbsp;&nbsp;&nbsp;Syntax ```/inworld character toggleAware [character]``` - This command will toggle the AI awareness of the character allowing it to be stationary or act like a regular Minecraft Villager.<br/>