package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.MessageService;

import static ai.inworld.minecraftsdk.utils.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.Logger.LogType;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpCommand extends CommandBase implements Command {

    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {

        this.permission = "inworld.command";
        this.description = "Get help for different Inworld commands";
        this.name = "help";
        this.version = "1.0";
        this.syntax = "/inworld help [command]";
        this.minArgs = 1;
        this.maxArgs = 2;

        this.commands = commands;

    }

    @Override
    protected void processCommand(CommandSender sender, String[] args) {

        LOG(LogType.Info, "HelpCommand processCommand");

        final List<Command> commandsWithPermission = new ArrayList<>();
        final List<String> commandsWithPermissionNames = new ArrayList<>();
        for (Command cmd : commands) {
            if (sender.hasPermission(cmd.getPermission())) {
                commandsWithPermission.add(cmd);
                commandsWithPermissionNames.add(cmd.getName());
            }
        }
        tabCompletes.put(1, commandsWithPermissionNames);
        if (args.length == 1) {
            for (Command cmd : commandsWithPermission) {
                cmd.performHelp(sender);
            }
        } else if (args.length == 2) {
            String inputCommand = args[1];
            boolean foundSubCommand = false;

            for (Command cmd : commandsWithPermission) {
                if (inputCommand.equalsIgnoreCase(cmd.getName())) {
                    foundSubCommand = true;
                    cmd.performHelp(sender);
                }
            }
            if (!foundSubCommand) {
                MessageService.sendPlayerMessage(sender, "Unknown command");
            }
        } else {
            performHelp(sender);
        }
    }
}
