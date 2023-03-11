package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import static ai.inworld.minecraftsdk.utils.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.Logger.LogType;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SceneCommand extends CommandBase implements Command {
    
    public SceneCommand() {
        
        this.permission = "inworld.command.scene";
        this.description = "Adds, Removes, Lists Scenes and Lists a Scenes Characters";
        this.name = "scene";
        this.version = "1.0";
        this.syntax = "/inworld scene [command] [sceneId]";
        this.minArgs = 2;
        this.maxArgs = 4;

        final List<String> environmentList = new ArrayList<>();
        environmentList.add("add");
        environmentList.add("remove");
        environmentList.add("list");
        environmentList.add("characters");
        tabCompletes.put(2, environmentList);

    }

    @Override
    protected void processCommand(CommandSender sender, String[] args) {
        
        LOG(LogType.Info, "The scene command has been run " + args.length);

        if ( args.length == minArgs ) {
            String command = args[1];
            if (command.equals("list")) {
                MessageService.sendPlayerMessage(sender, "Scenes in system:");
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    MessageService.sendPlayerMessage(sender, "- " + scene);
                }
            }
            return;
        }

        if ( args.length == 3 ) {
            String command = args[1];
            String name = args[2];
            if (command.equals("remove")) {
                ConfigService.getConfig().set("server.scenes." + name, null);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "Scene removed: " + name);
            }
            return;
        }

        if ( args.length == 3 ) {
            String command = args[1];
            String name = args[2];
            if (command.equals("characters")) {
                MessageService.sendPlayerMessage(sender, "Scene character listing:" + name);
                // ConfigService.getConfig().set("server.scenes." + name, null);
                // ConfigService.save();
                // MessageService.sendPlayerMessage(sender, "Scene removed: " + name);
            }
            return;
        }

        if ( args.length == maxArgs ) {
            String command = args[1];
            String name = args[2];
            String sceneId = args[3];
            LOG(LogType.Info, command + " " + name + " " + sceneId);
            if (command.equals("add")) {
                LOG(LogType.Info, command + " " + name + " " + sceneId);
                ConfigService.getConfig().set("server.scenes." + name, sceneId);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "Scene added: " + name + " " + sceneId);
            }
            return;
        }


    }

}